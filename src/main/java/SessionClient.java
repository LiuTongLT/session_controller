import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionClient {
    private static final String URL = "http://127.0.0.1:8081/nbi/deliverysession?id=1";

    public static void main(String[] args) {

        //To deal with the error "No appenders could be found for logger"
        BasicConfigurator.configure();
        SessionClient sessionClient = new SessionClient();
        sessionClient.singleSession();

    }
    public void singleSession(){
        DeliverySessionCreation deliverySessionCreation = new DeliverySessionCreation(1,ActionType.Start,"TMGI",1,4,"a");
        DeliverySession deliverySession = new DeliverySession(deliverySessionCreation);
        deliverySession.setTimerSync();
    }

    public void multipleSessionsSync(){
        // concurrent requests (Threads number)
        int concurrent = 50;
        // using CountDownLatch to make sure that the main thread does not exit until all threads finish
        CountDownLatch countDownLatch = new CountDownLatch(concurrent);
        ExecutorService threadPool = Executors.newFixedThreadPool(concurrent);
        int concurrentPer = concurrent;

        //Thread pool submit requests
        for(int i = 0; i < concurrentPer; i++) {
            DeliverySessionCreation deliverySessionCreation = new DeliverySessionCreation(i,ActionType.Start,"TMGI",i,i+5,"a");
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        DeliverySession deliverySession = new DeliverySession(deliverySessionCreation);
                        deliverySession.setTimerSync();
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

    public void multipleSessionsWithDynamicPool(int numberOfSessions) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                10,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100));
        //AtomicInteger sessionId = new AtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(numberOfSessions);
        for(int i=0; i<numberOfSessions; i++){
            DeliverySessionCreation deliverySessionCreation = new DeliverySessionCreation(i,ActionType.Start,"TMGI",i,i+5,"a");
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        DeliverySession deliverySession = new DeliverySession(deliverySessionCreation);
                        deliverySession.setTimerSync();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }
        if(numberOfSessions>20){
            //if the new core pool size is larger than old max pool size, we should firstly set the max pool size
            executor.setMaximumPoolSize(20);
            executor.setCorePoolSize(20);
            executor.prestartAllCoreThreads();
            System.out.println("Max thread number: "+executor.getMaximumPoolSize());
        }

        try {
            countDownLatch.await();
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void mutipleThreadsAsync(){
        // concurrent requests (Threads number)
        int concurrent = 10;
        // using CountDownLatch to make sure that the main thread does not exit until all threads finish
        CountDownLatch countDownLatch = new CountDownLatch(concurrent);
        ExecutorService threadPool = Executors.newFixedThreadPool(concurrent);
        int concurrentPer = concurrent;

        AtomicInteger sessionId = new AtomicInteger(0);
        //Thread pool submit requests
        for(int i = 0; i < concurrentPer; i++) {
            DeliverySessionCreation deliverySessionCreation = new DeliverySessionCreation(sessionId.getAndIncrement(),ActionType.Start,"TMGI",i,i+5,"a");
            //sessionId.getAndIncrement();
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        DeliverySession deliverySession = new DeliverySession(deliverySessionCreation);
                        deliverySession.setTimerAsync();
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

