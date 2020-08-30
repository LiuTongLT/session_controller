import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class DeliverySession {
    private DeliverySessionCreation deliverySessionCreation;
    private Timer timer;

    public DeliverySession(){}

    public DeliverySession(DeliverySessionCreation deliverySessionCreation){
        this.deliverySessionCreation = deliverySessionCreation;
        timer = new Timer();

    }
    public void setTimerSync(){
        CountDownLatch latch = new CountDownLatch(1);
        RequestTaskSync start = new RequestTaskSync(deliverySessionCreation);
        RequestTaskSync stop = new RequestTaskSync(deliverySessionCreation);
        timer.schedule(start, deliverySessionCreation.getStartTime()*1000);
        timer.schedule(stop, deliverySessionCreation.getStopTime()*1000);

        //In order to use test unit, the thread should wait for the time schedule events
        try {
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setTimerAsync(){
        CountDownLatch latch = new CountDownLatch(1);
        RequestTaskAsync start = new RequestTaskAsync(deliverySessionCreation);
        RequestTaskAsync stop = new RequestTaskAsync(deliverySessionCreation);
        timer.schedule(start, deliverySessionCreation.getStartTime()*1000);
        timer.schedule(stop, deliverySessionCreation.getStopTime()*1000);

        //In order to use test unit, the thread should wait for the time schedule events
        try {
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
