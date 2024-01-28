package executorservice1;

import java.util.List;
import java.util.concurrent.*;

public class App {
    public static void main(String[] args) {
        ExecutorService multiExecutor = Executors.newCachedThreadPool();
        List<Callable<Integer>> taskList = List.of(
                () -> App.sum(1, 10, 1, "red"),
                () -> App.sum(10, 100, 10, "blue"),
                () -> App.sum(2, 20, 2, "green")
        );
        try {
            var results = multiExecutor.invokeAll(taskList);
            for (var result : results) {
                System.out.println(result.get(500, TimeUnit.SECONDS));
            }
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            multiExecutor.shutdown();
        }
    }

    public static void cachedMain(String[] args) {
        ExecutorService multiExecutor = Executors.newCachedThreadPool();
        try {
            Future<Integer> redVal = multiExecutor.submit(() -> App.sum(1, 10, 1, "red"));
            Future<Integer> blueVal = multiExecutor.submit(() -> App.sum(10, 100, 10, "blue"));
            Future<Integer> greenVal = multiExecutor.submit(() -> App.sum(2, 20, 2, "green"));

            try {
                System.out.println(redVal.get(500, TimeUnit.SECONDS));
                System.out.println(blueVal.get(500, TimeUnit.SECONDS));
                System.out.println(greenVal.get(500, TimeUnit.SECONDS));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } finally {
            multiExecutor.shutdown();
        }
    }

    public static void fixedMain(String[] args) {
        int count = 3;
        ExecutorService multiExecutor = Executors.newFixedThreadPool(count,
                new ColorThreadFactory(ThreadColor.ANSI_BLUE));

        for (int i = 0; i < count; i++) {
            multiExecutor.execute(App::countDown);
        }
        multiExecutor.shutdown();
    }

    public static void secondAnotherMain(String[] args) {
        ExecutorService blueExecutor = Executors.newSingleThreadExecutor(
                new ColorThreadFactory(ThreadColor.ANSI_BLUE)
        );
        blueExecutor.execute(App::countDown);
        blueExecutor.shutdown();

        boolean isDone = false;
        try {
            isDone = blueExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (isDone) {
            System.out.println("Blue finished, starting Yellow");
            ExecutorService yellowExecutor = Executors.newSingleThreadExecutor(
                    new ColorThreadFactory(ThreadColor.ANSI_YELLOW)
            );
            yellowExecutor.execute(App::countDown);
            yellowExecutor.shutdown();

            try {
                isDone = yellowExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (isDone) {
                System.out.println("Yellow finished, starting Red");
                ExecutorService redExecutor = Executors.newSingleThreadExecutor(
                        new ColorThreadFactory(ThreadColor.ANSI_RED)
                );
                redExecutor.execute(App::countDown);
                redExecutor.shutdown();

                try {
                    isDone = redExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (isDone) {
                    System.out.println("All processes completed");
                }
            }
        }
    }

    public static void anotherMain(String[] args) {
        Thread blue = new Thread(App::countDown, ThreadColor.ANSI_BLUE.name());
        Thread yellow = new Thread(App::countDown, ThreadColor.ANSI_YELLOW.name());
        Thread red = new Thread(App::countDown, ThreadColor.ANSI_RED.name());

        blue.start();
        try {
            blue.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        yellow.start();
        try {
            yellow.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        red.start();
        try {
            red.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void countDown() {
        String threadName = Thread.currentThread().getName();
        ThreadColor threadColor = ThreadColor.ANSI_RESET;

        try {
            threadColor = ThreadColor.valueOf(threadName.toUpperCase());
        } catch (IllegalArgumentException e) {
            // User may pass a bad color name, will just ignore this error.
        }

        String color = threadColor.color();
        for (int i = 20; i >= 0; i--) {
            System.out.println(color + " " +
                    threadName.replace("ANSI_", "") + "  " + i);
        }
    }

    private static int sum(int start, int end, int delta, String colorString) {
        ThreadColor threadColor = ThreadColor.ANSI_RESET;
        try {
            threadColor = ThreadColor.valueOf("ANSI_" +
                    colorString.toUpperCase());
        } catch (IllegalArgumentException ignore) {
            // User may pass a bad color name, will just ignore this error.
        }

        String color = threadColor.color();
        int sum = 0;
        for (int i = start; i <= end; i += delta) {
            sum += i;
        }
        System.out.println(color + Thread.currentThread().getName() + ", "
                + colorString + " " + sum);
        return sum;
    }
}

class ColorThreadFactory implements ThreadFactory {
    private String threadName;

    public ColorThreadFactory(ThreadColor color) {
        this.threadName = color.name();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(threadName);
        return thread;
    }
}
