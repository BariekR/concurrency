package thread3;

public class ThreadCApp {
    public static void main(String[] args) {
        EvenThread evenThread = new EvenThread();
        Thread oddThread = new Thread(
                () -> {
                    for (int i = 0; i < 5; i++) {
                        System.out.println(2 * i + 1);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println("OddThread interrupted");
                            return;
                        }
                    }
                }
        );

        evenThread.start();
        oddThread.start();

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        evenThread.interrupt();
    }
}
