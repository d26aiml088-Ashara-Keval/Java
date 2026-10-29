package test;

import exception.InsufficientFundsException;
import model.Account;
import service.BankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MiniBankTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void depositIncreasesBalance() throws Exception {
        BankService bank = new BankService(temporaryDirectory);
        Account account = bank.openAccount("Asha", "9876543210",
                "asha@example.com", 1_000);

        bank.deposit(account.getAccountNumber(), 250);

        assertEquals(1_250, account.getBalance());
    }

    @Test
    void withdrawalOverBalanceThrowsInsufficientFunds() throws Exception {
        BankService bank = new BankService(temporaryDirectory);
        Account account = bank.openAccount("Asha", "9876543210",
                "asha@example.com", 100);

        assertThrows(InsufficientFundsException.class,
                () -> bank.withdraw(account.getAccountNumber(), 101));
        assertEquals(100, account.getBalance());
    }

    @Test
    void concurrentDepositsPreserveTheCorrectBalance() throws Exception {
        Account account = new Account("Asha", "9876543210",
                "asha@example.com", 0);
        int workerCount = 10;
        int depositsPerWorker = 5_000;
        ExecutorService executor = Executors.newFixedThreadPool(workerCount);
        CountDownLatch ready = new CountDownLatch(workerCount);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> workers = new ArrayList<>();

        try {
            for (int worker = 0; worker < workerCount; worker++) {
                workers.add(executor.submit(() -> {
                    ready.countDown();
                    start.await();
                    for (int deposit = 0; deposit < depositsPerWorker; deposit++) {
                        account.deposit(1);
                    }
                    return null;
                }));
            }
            ready.await();
            start.countDown();
            for (Future<?> worker : workers) {
                worker.get();
            }
        } finally {
            executor.shutdownNow();
        }

        assertEquals((long) workerCount * depositsPerWorker, account.getBalance());
    }
}
