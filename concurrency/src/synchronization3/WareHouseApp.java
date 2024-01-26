package synchronization3;

import java.util.Random;

public class WareHouseApp {
    private static final Random random = new Random();

    public static void main(String[] args) {
        ShoeWarehouse warehouse = new ShoeWarehouse();
        Thread producerThread = new Thread(
                () -> {
                    for (int i = 0; i < 10; i++) {
                        warehouse.receiveOrder(new Order(
                                random.nextInt(1000000, 9999999),
                                ShoeWarehouse.PRODUCT_LIST[random.nextInt(0, 5)],
                                random.nextInt(1, 4)
                        ));
                    }
                }
        );
        producerThread.start();

        for (int i = 0; i < 2; i++) {
            Thread consumerThread = new Thread(
                    () -> {
                        for (int j = 0; j < 5; j++) {
                            Order item = warehouse.fulfillOrder();
                        }
                    }
            );
            consumerThread.start();
        }
    }
}

record Order(long orderId, String item, int qty){

}

