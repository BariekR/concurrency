package thread;

import java.util.concurrent.TimeUnit;

public class CustomThreadApp {
    public static void main(String[] args) {
        CustomThread customThread = new CustomThread();
        customThread.start();

        Runnable myRunnable = () -> {
            for (int i = 0; i < 8; i++) {
                System.out.print(" 2 ");
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread myThread = new Thread(myRunnable);
        myThread.start();

        for (int i = 0; i < 3; i++) {
            System.out.print(" 0 ");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
