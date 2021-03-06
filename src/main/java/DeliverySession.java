import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class DeliverySession {
    private DeliverySessionCreation deliverySessionCreation;
    private Timer timer;
    RequestTaskSync startSync;
    RequestTaskSync stopSync;
    RequestTaskAsync startAsync;
    RequestTaskAsync stopAsync;

    public DeliverySession(){}

    public DeliverySession(DeliverySessionCreation deliverySessionCreation){
        this.deliverySessionCreation = deliverySessionCreation;
        timer = new Timer();
        startSync = new RequestTaskSync(deliverySessionCreation);
        stopSync = new RequestTaskSync(deliverySessionCreation);
        startAsync = new RequestTaskAsync(deliverySessionCreation);
        stopAsync = new RequestTaskAsync(deliverySessionCreation);

    }
    public void setTimerSync(){
        CountDownLatch latch = new CountDownLatch(1);
        timer.schedule(startSync, deliverySessionCreation.getStartTime()*1000);
        timer.schedule(stopSync, deliverySessionCreation.getStopTime()*1000);

        //In order to use test unit, the thread should wait for the time schedule events
       /* try {
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    public void setTimerAsync(){
        CountDownLatch latch = new CountDownLatch(1);

        timer.schedule(startAsync, deliverySessionCreation.getStartTime()*1000);
        timer.schedule(stopAsync, deliverySessionCreation.getStopTime()*1000);

        //In order to use test unit, the thread should wait for the time schedule events
        try {
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean setStopTime(int seconds){
        deliverySessionCreation.setStopTime(seconds);
        boolean re = stopSync.cancel();
        timer.purge();
        if(re){
            stopSync = new RequestTaskSync(deliverySessionCreation);
            timer.schedule(stopSync, seconds*1000);
        }
        return  re;
    }
}
