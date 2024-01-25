package synchronization1;

public class BankAccount {
    private double balance;
    private String name;
    private final Object lockName = new Object();
    private final Object lockBalance = new Object();

    public BankAccount(String name, double balance) {
        this.balance = balance;
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        synchronized (lockName) {
            this.name = name;
            System.out.println("Updated name = " + this.name);
        }
    }

    public void deposit(double amount) {
        try {
            System.out.println("-- Talking at the counter (deposit)..");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        synchronized (lockBalance) {
            double origBalance = balance;
            balance += amount;
            System.out.printf("STARTING BALANCE: %.0f, DEPOSIT (%.0f)"
                    + " : NEW BALANCE = %.0f%n", origBalance, amount, balance);
            addPromoDollars(amount);
        }
    }

    private void addPromoDollars (double amount) {
        if (amount >= 5000) {
            synchronized (lockBalance) {
                System.out.println("Congrats, you earned a promotional deposit");
                balance += 25;
            }
        }
    }

    public void withdraw(double amount) {
        try {
            System.out.println("-- Talking at the counter (withdrawal)..");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        synchronized (this) {
            double origBalance = balance;
            if (amount <= balance) {
                balance -= amount;
                System.out.printf("STARTING BALANCE: %.0f, WITHDRAWAL (%.0f)"
                        + " : NEW BALANCE = %.0f%n", origBalance, amount, balance);
            } else {
                System.out.printf("STARTING BALANCE: %.0f, WITHDRAWAL (%.0f)"
                        + " : INSUFFICIENT FUNDS!", origBalance, amount);
            }
        }
    }


}
