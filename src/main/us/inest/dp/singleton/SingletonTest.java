package us.inest.dp.singleton;

public class SingletonTest {
    public static void main(String[] args) {
        System.out.println("--- Starting Multi-threaded Singleton Test ---");

        Thread threadAlpha = new Thread(() -> {
            Singleton singleton = Singleton.getInstance("Alpha");
            System.out.print("Thread Alpha: ");
            singleton.display();
        });

        Thread threadBeta = new Thread(() -> {
            Singleton singleton = Singleton.getInstance("Beta");
            System.out.print("Thread Beta: ");
            singleton.display();
        });

        threadAlpha.start();
        threadBeta.start();
    }
}
