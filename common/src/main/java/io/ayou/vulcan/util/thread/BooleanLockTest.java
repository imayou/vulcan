package io.ayou.vulcan.util.thread;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @ClassName BooleanLockTest
 */
public class BooleanLockTest {

    //定义BooleanLock
    private final Lock lock = new BooleanLock();

    //使用try...finally语句块确保lock每次都能被正确释放
    public void syncMethod() {
        try {
            //加锁
            lock.lock();

            try {
                int randomInt = ThreadLocalRandom.current().nextInt(10);
                System.out.println(Thread.currentThread() + " get the lock.");
                TimeUnit.SECONDS.sleep(randomInt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            BooleanLockTest booleanLockTest = new BooleanLockTest();
            IntStream.range(0, 10).mapToObj(i -> new Thread(booleanLockTest::syncMethod)).forEach(Thread::start);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
