import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
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
    public void setTimer(){
        CountDownLatch latch = new CountDownLatch(1);
        SessionRequest start = new SessionRequest(deliverySessionCreation);
        SessionRequest stop = new SessionRequest(deliverySessionCreation);
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
