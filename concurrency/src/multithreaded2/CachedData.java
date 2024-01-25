package multithreaded2;

import java.util.concurrent.TimeUnit;

public class CachedData {
    private volatile boolean flag = false;

    public void toggleFlag() {
        flag = !flag;
    }

    public boolean isReady() {
        return flag;
    }

    public static void main(String[] args) {
        CachedData cachedData = new CachedData();

        Thread writerThread = new Thread(
                () -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cachedData.toggleFlag();
                    System.out.println("A. Flag set to " + cachedData.isReady());
                }
        );

        Thread readerThread = new Thread(
                () -> {
                    while(!cachedData.isReady()) {
                        // waiting
                    }
                    System.out.println("B. Flag is " + cachedData.isReady());
                }
        );

        writerThread.start();
        readerThread.start();
    }
}
