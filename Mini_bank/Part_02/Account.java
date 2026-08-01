public class Account{
    private final String accountNumber;
    private String ownerName;
    private long balance;
    private boolean active;

    private static int accountCounter = 0;

    private static String generateAccountNumber(){
        accountCounter++ ;
        return "Acc" + accountCounter;
    }

    public Account(String ownerName, long openingBalance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    public void deposit(long amount){
        if(amount<0){
            return;
        }
        balance += amount;
    }

    public boolean withdraw(long amount){
        if(amount>balance){
            return false;
        }

        if(amount<= balance && amount>0){
            balance -= amount;
            return true;
        }

        return false;
    }

    // Getter Methods
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public static void transfer(Account from, Account to, long amount) {
        if (from.withdraw(amount)) {
            to.deposit(amount);
        }
    }
}