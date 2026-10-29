package service;

import exception.AccountNotFoundException;
import exception.BankException;
import model.Account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor {
    public record TransactionRequest(Type type, String accountNumber, long amount) {
        public enum Type {
            DEPOSIT,
            WITHDRAW
        }

        public TransactionRequest {
            if (type == null || accountNumber == null || accountNumber.isBlank()) {
                throw new IllegalArgumentException("Transaction type and account are required");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("Transaction amount must be positive");
            }
        }
    }

    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final BankService bankService;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private boolean stopped;

    public TransactionProcessor(BankService bankService) {
        if (bankService == null) {
            throw new IllegalArgumentException("Bank service must not be null");
        }
        this.bankService = bankService;
        for (Account account : bankService.snapshot()) {
            accounts.put(account.getAccountNumber(), account);
        }
    }

    public synchronized void register(Account account) {
        if (stopped) {
            throw new IllegalStateException("Transaction processor has stopped");
        }
        if (account == null) {
            throw new IllegalArgumentException("Account must not be null");
        }
        accounts.put(account.getAccountNumber(), account);
    }

    public void processBatch(List<TransactionRequest> requests)
            throws BankException, IOException, InterruptedException {
        if (requests == null) {
            throw new IllegalArgumentException("Transaction batch must not be null");
        }
        for (TransactionRequest request : requests) {
            if (request == null) {
                throw new IllegalArgumentException("Transaction batch contains null");
            }
            if (!accounts.containsKey(request.accountNumber())) {
                throw new AccountNotFoundException(request.accountNumber());
            }
        }

        List<Future<?>> futures = new ArrayList<>(requests.size());
        synchronized (this) {
            if (stopped) {
                throw new IllegalStateException("Transaction processor has stopped");
            }
            for (TransactionRequest request : requests) {
                futures.add(executor.submit(() -> {
                    if (request.type() == TransactionRequest.Type.DEPOSIT) {
                        bankService.deposit(request.accountNumber(), request.amount());
                    } else {
                        bankService.withdraw(request.accountNumber(), request.amount());
                    }
                    return null;
                }));
            }
        }

        BankException bankFailure = null;
        IOException ioFailure = null;
        RuntimeException runtimeFailure = null;
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException exception) {
                for (Future<?> pending : futures) {
                    pending.cancel(true);
                }
                throw exception;
            } catch (ExecutionException exception) {
                Throwable cause = exception.getCause();
                if (cause instanceof BankException failure) {
                    if (bankFailure == null) {
                        bankFailure = failure;
                    } else {
                        bankFailure.addSuppressed(failure);
                    }
                } else if (cause instanceof IOException failure) {
                    if (ioFailure == null) {
                        ioFailure = failure;
                    } else {
                        ioFailure.addSuppressed(failure);
                    }
                } else if (cause instanceof RuntimeException failure) {
                    if (runtimeFailure == null) {
                        runtimeFailure = failure;
                    } else {
                        runtimeFailure.addSuppressed(failure);
                    }
                } else {
                    BankException failure = new BankException("Batch transaction failed", cause);
                    if (bankFailure == null) {
                        bankFailure = failure;
                    } else {
                        bankFailure.addSuppressed(failure);
                    }
                }
            }
        }
        if (bankFailure != null) {
            if (ioFailure != null) {
                bankFailure.addSuppressed(ioFailure);
            }
            throw bankFailure;
        }
        if (ioFailure != null) {
            throw ioFailure;
        }
        if (runtimeFailure != null) {
            throw runtimeFailure;
        }
    }

    public synchronized void stop() throws InterruptedException {
        if (!stopped) {
            stopped = true;
            executor.shutdown();
        }
        try {
            while (!executor.awaitTermination(1, TimeUnit.DAYS)) {
                // Continue waiting until all submitted transactions finish.
            }
        } catch (InterruptedException exception) {
            executor.shutdownNow();
            throw exception;
        }
    }
}
