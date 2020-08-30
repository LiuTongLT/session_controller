import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionClientTest {
    @Before
    public void init(){
        BasicConfigurator.configure();
    }
    @Test
    public void singleThreadTest(){
        DeliverySessionCreation deliverySessionCreation = new DeliverySessionCreation(1,ActionType.Start,"TMGI",1,4,"a");
        DeliverySession deliverySession = new DeliverySession(deliverySessionCreation);
        deliverySession.setTimer();

    }
    @Test
    public void multipleThreadTest(){
        int concurrent = 50; // concurrent requests
        CountDownLatch countDownLatch = new CountDownLatch(concurrent);
        ExecutorService threadPool = Executors.newFixedThreadPool(concurrent);
        int concurrentPer = concurrent;
        //Thread pool submit requests
        for(int i = 0; i < concurrentPer; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        DeliverySessionCreation deliverySessionCreation = new DeliverySessionCreation(1,ActionType.Start,"TMGI",1,4,"a");
                        DeliverySession deliverySession = new DeliverySession(deliverySessionCreation);
                        deliverySession.setTimer();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });

        }
        try {
            countDownLatch.await();
            threadPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
