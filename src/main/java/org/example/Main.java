package org.example;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/* Diana Vigdorova, started @ 27.03.24.
[DONE] Create a new project "Banking" and create a class "BankAccount" with property "balance" (should be decimal number)
[DONE] create an empty default constructor for it
[DONE] create a constructor with parameter for setting balance.
[DONE] create a method "deposit" with one parameter "amount" (decimal number)
[DONE] create a method "withdraw" with one parameter "amount"(decimal number)
[DONE] create a method "printBalance" which displays the current balance to user
[DONE] method to transfer balance from one bank account to another
[DONE] Optionally make the program interactive with user e.g. using Scanner
[DONE] data structure
 +account reports in .txt
*/
// TODO add the username transfer functionality

import java.util.Scanner;
import java.util.*;

class BankAccount {
    static Map<String, BankAccount> accounts = new HashMap<>(); //String is username
    private String username;
    static boolean isLoggedIn=false;
    static BankAccount loggedInAccount=null;
    private String password;
    private double balance = 0;
    private static int lastAccountDigits = 0;
    private int accountDigits;
    private String accountNumber;

    BankAccount() {
    } //empty default constructor //LV93HABA0551031375321

    BankAccount(double balance) {
        this.balance = balance;
    }//constructor with a parameter

    BankAccount(String username, String password) { //this one actually gets used
        this.username = username;
        this.password = password;
        accountDigits = ++lastAccountDigits;
        accountNumber = String.format("LV93HABA055%08d", accountDigits); //%08 is "8 zeros" if needed in-between
    }

    public static void addAccount(String username, BankAccount account) {
        accounts.put(username, account);
    }

    public void deposit(double amount) { //here would need to check max 2 digits after comma
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful. Current balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount >= 0 && amount <= balance) {
            balance -= amount;
            System.out.println("You have successfully taken out "+amount+" Eur. Remaining balance: " + balance);
            return true;
        } else {
            System.out.println("Insufficient funds. Current balance: " + balance);
            return false;
        }
    }

    public void printBalance() {
        System.out.println("Current balance " + balance);
    }

    /*public void transfer(double amount, String username){
        //  this.balance=balance-amount;
        if(withdraw(amount)){
           String accountTo=getAccountNumberByUsername(username);
                deposit(amount);
        }
    }*/
    public String getInfo() {
        return "\nAccount number: " + accountNumber + "\nBalance: " + balance + "\n";
    }

    public String getUsername() {
        return username;
    }

    public Double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void report() {
        BankAccount.accounts.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.comparing(BankAccount::getAccountNumber)))
            .forEach(entry -> {
                String username = entry.getKey();
                BankAccount account = entry.getValue();
                System.out.println("Username: " + username + ", Account Number: " + account.getAccountNumber() + ", Balance: " + account.getBalance());
            });
    }

    static void register(Scanner scanner) {
        System.out.println("Please enter your desired username");
        String username = scanner.nextLine();
        while (checkUsername(username)) {
            System.out.println("Username already exists, try a different one.");
            username = scanner.nextLine();
        }
        System.out.println("Please enter your desired password");
        String password = scanner.nextLine();
        BankAccount.addAccount(username, new BankAccount(username, password));
        System.out.println("Your account has been successfully created!");
    }

    public static boolean checkUsername(String username) {
        return BankAccount.accounts.containsKey(username);
    }

    public static boolean login(Scanner scanner) {
        System.out.println("Enter your username!");
        String username = scanner.nextLine();

        System.out.println("Enter password");
        String password = scanner.nextLine();

        BankAccount account = accounts.get(username);
        if (account != null && account.checkPassword(password)) {
            System.out.println("Login successful!");
            loggedInAccount=account;
            isLoggedIn=true;
            System.out.println(loggedInAccount.getInfo()); // Debug print
            return true;
        } else {
            System.out.println("Login failed. Please try again!");
            return false;
        }
    }

    public String getAccountNumberByUsername(String username) {
        BankAccount account = accounts.get(username);
        if (account != null) {
            return account.getAccountNumber();
        } else {
            return null;
        }
    }

    public BankAccount getAccountByUsername(String username) {
        return accounts.get(username);
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public static void menu(Scanner scanner, BankAccount account) {
        Byte choice;
        do {
            System.out.println("Select your activity:\nPress: 1 to view balance, 2 to deposit, 3 to withdraw, 4 to transfer, 5 to view account details, 6 to exit");
            choice = scanner.nextByte();
            switch (choice) {
                case 1: {
                    loggedInAccount.printBalance();
                    break;
                }
                case 2: {
                    System.out.println("Enter the amount you wish to deposit.");
                    loggedInAccount.deposit(scanner.nextDouble());
                    break;
                }
                case 3: {
                    System.out.println("Enter the amount you wish to withdraw.");
                    double amount = scanner.nextDouble();
                    loggedInAccount.withdraw(amount);
                    break;
                }
                case 4: {
                     /*   System.out.println("Enter the username of the person you wish to transfer funds to.");
                        String username= scanner.nextLine();
                        String accountNumber = account.getAccountNumberByUsername(username);
                        if (accountNumber != null) {
                            System.out.println("Found account number: " + accountNumber);
                        } else {
                            System.out.println("Account not found.");
                        }
                        System.out.println("Enter the amount you wish to transfer.");
                        double amount=scanner.nextDouble();
                        accountNumber.transfer(amount,username); break;*/
                }
                case 5: {
                    System.out.println("The results: ");
                    System.out.println(loggedInAccount.getInfo());
                    break;
                }
            }
        }
        while (choice != 6);
    }
}
public class Main {
   // protected static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        BankAccount loggedInAccount = new BankAccount();
        //      logger.info("Hello World!");

        System.out.println("Welcome! Click 1 to login, click 2 to register, click 3 to exit!");
        Scanner scanner = new Scanner(System.in);
        Byte choice = scanner.nextByte();
        scanner.nextLine(); //reads the rest of the line (for it to work in login method)
        switch (choice) {
            case 1: {
                loggedInAccount.login(scanner); break;
            }
            case 2: {
                loggedInAccount.register(scanner);
                System.out.println("Please login to continue!");
                loggedInAccount.login(scanner);
                break;
            }
            case 3: {
                System.out.println("You have exited!");
                return;
            }
            default:
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
        }
        if(BankAccount.isLoggedIn){
                loggedInAccount.menu(scanner, loggedInAccount);}
       // else System.out.println("No one is logged in.");

     scanner.close();}
    }
