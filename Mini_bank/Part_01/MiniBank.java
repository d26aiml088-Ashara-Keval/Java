import java.util.Scanner;

public class MiniBank{

    enum MenuOption{
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
    }

    record BankInfo(String name, String branch){

    }

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        BankInfo bank = new BankInfo("SBI" , "JAMNAGAR");

        System.out.println(" ");
        System.out.println(bank.name());
        System.out.println(bank.branch());
        System.out.println(" ");
        while (true){

            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");

            System.out.print("Enter Choice : ");
            int choice = sc.nextInt();

            MenuOption menuOption = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (menuOption == null){
                System.out.println("Invalid Choice");
                continue;
            }

            switch (menuOption){
                case OPEN_ACCOUNT:
                    System.out.println("Open Account");
                    break;

                case DEPOSIT:
                    System.out.println("DEPOSIT DONE");
                    break;

                case WITHDRAW:
                    System.out.println("WITHDRAW DONE");
                    break;

                case TRANSFER:
                    System.out.println("TRANSFERED");
                    break;

                case EXIT:
                    System.out.println("Thanks For Using Our Service");
                    sc.close();
                    return;
            }
        }

    }
}