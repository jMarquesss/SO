import java.util.concurrent.Semaphore;

public class Demo {
    public static void main(String[] args){
        Semaphore semaphore= new Semaphore(1);
        Thread t1 = new Thread (new Elevator(semaphore));
        t1.start();
    }
}
