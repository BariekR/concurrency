package streams2;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        var persons = Stream.generate(Person::new)
                .limit(10)
                .sorted(Comparator.comparing(Person::lastName))
                .toArray();

        for (var person : persons) {
            System.out.println(person);
        }

        System.out.println("---------------------");

        Arrays.stream(persons)
                .limit(10)
                .parallel()
        //        .sorted(Comparator.comparing(Person::lastName))
                .forEachOrdered(System.out::println);

        System.out.println("---------------------");

        int sum = IntStream.range(1, 101)
                .parallel()
                .reduce(0, Integer::sum);
        System.out.println("The sum: " + sum);

        System.out.println("---------------------");

        String lines = """
                first line abcd
                second line efgh
                third line ijkl
                """;
        var words = new Scanner(lines).tokens().toList();
        words.forEach(System.out::println);

        System.out.println("---------------------");

        /*var backTogether = words
                .stream()
                .reduce(new StringJoiner(" "),
                        StringJoiner::add,
                        StringJoiner::merge);*/
        var backTogether = words
                .parallelStream()
                .collect(Collectors.joining(" "));

        System.out.println(backTogether);

        System.out.println("---------------------");

        Map<String, Long> lastNameCounts =
                Stream.generate(Person::new)
                        .limit(10_000)
                        .parallel()
                        .collect(Collectors.groupingByConcurrent(Person::lastName, Collectors.counting()));

        lastNameCounts.entrySet().forEach(System.out::println);
        System.out.println(lastNameCounts.getClass().getName());


    }
}
