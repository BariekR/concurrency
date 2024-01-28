package scheduling1;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class SchedulingApp {
    public static void main(String[] args) {
        var dtf = DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.MEDIUM,
                FormatStyle.LONG
        );

        Callable<ZonedDateTime> waitThenDoIt = () -> {
            ZonedDateTime zdt = null;
            try {
                TimeUnit.SECONDS.sleep(2);
                zdt = ZonedDateTime.now();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return zdt;
        };

        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        List<Callable<ZonedDateTime>> list = Collections.nCopies(4, waitThenDoIt);

        System.out.println("---> " + ZonedDateTime.now().format(dtf));
        try {
            List<Future<ZonedDateTime>> futureList = threadPool.invokeAll(list);
            for (Future<ZonedDateTime> result : futureList) {
                try {
                    System.out.println(result.get(1, TimeUnit.SECONDS).format(dtf));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }

        System.out.println("---> " + ZonedDateTime.now().format(dtf));
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

        var scheduledTask = executor.scheduleWithFixedDelay(
                ()-> System.out.println(ZonedDateTime.now().format(dtf)),
                2,
                2,
                TimeUnit.SECONDS);

        long time = System.currentTimeMillis();
        while (!scheduledTask.isDone()) {
            try {
                TimeUnit.SECONDS.sleep(2);
                if ((System.currentTimeMillis() - time) / 1000 > 10) {
                    scheduledTask.cancel(true);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        executor.shutdown();
    }
}
