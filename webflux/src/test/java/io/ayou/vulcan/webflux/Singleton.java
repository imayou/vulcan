package io.ayou.vulcan.webflux;

/**
 * @ClassName Singleton
 */
public class Singleton {
    private static Singleton instance = new Singleton();
    private static int x = 0;
    private static int y;

    public Singleton() {
        System.out.println("x-"+x);
        System.out.println("y-"+y);
        x++;
        y++;
    }

    public static Singleton getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        Singleton singleton = Singleton.getInstance();
        System.out.println(singleton.x);
        System.out.println(singleton.y);
    }
}
