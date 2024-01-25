package thread2;

public class ThreadJoinApp {
    public static void main(String[] args) {
        Thread downloadThread = new Thread(
                () -> {
                    try {
                        for (int i = 0; i < 10; i++) {
                            Thread.sleep(500);
                            System.out.println("Thread downloading a file");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        , "DownloadThread");
        System.out.println(downloadThread.getName() + " starting");
        downloadThread.start();

        Thread installThread = new Thread(
                () -> {
                    try {
                        for (int i = 0; i < 5; i++) {
                            Thread.sleep(1000);
                            System.out.println("Installation step " + (i + 1)
                                    + " is completed");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        , "InstallThread");

        try {
            downloadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!downloadThread.isInterrupted()) {
            installThread.start();
        } else {
            System.out.println("Download thread was interrupted, "
                + installThread.getName() + " can't run");
        }

    }
}
