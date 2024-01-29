package streams3;

import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Stream;

record Person(String firstName, String lastName, int age) {
    private final static String[] firsts =
            {"Able", "Bob", "Charlie", "Donna", "Eve", "Fred"};
    private final static String[] lasts =
            {"Norton", "Ohara", "Petersen", "Quincy", "Richardson", "Smith"};
    private final static Random random = new Random();
    public Person() {
        this(firsts[random.nextInt(firsts.length)],
                lasts[random.nextInt(lasts.length)],
                random.nextInt(18, 100));
    }
    @Override
    public String toString() {
        return "%s, %s (%d)".formatted(lastName, firstName, age);
    }
}

public class StreamsApp {
    public static void main(String[] args) {
        var threadMap = new ConcurrentSkipListMap<String, Long>();

        var persons = Stream.generate(Person::new)
                .limit(10_000)
                .parallel()
                .peek((p) -> {
                    var threadName = Thread.currentThread().getName();
                    threadMap.merge(threadName, 1L, Long::sum);
                })
                .toArray(Person[]::new);

        System.out.println(threadMap);
    }
}
