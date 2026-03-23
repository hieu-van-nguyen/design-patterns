package us.inest.dp.singleton;

public class Singleton {
    /*
     * volatile ensure that a variable's value is always read from and written to the main memory,
     * making changes immediately visible to all threads
     */
    private volatile static Singleton uniqueInstance;
    private String data;

    private Singleton(String data){
        this.data = data;
    }

    // double check locking
    public static Singleton getInstance(String value) {
        if (uniqueInstance == null) {
            synchronized (Singleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Singleton(value);
                }
            }
        }
        return uniqueInstance;
    }

    public void display() {
        System.out.println("Instance Data: " + data);
    }
}
