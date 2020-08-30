import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Timer;
import java.util.TimerTask;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class DeliverySessionCreation {
    private int DeliverySessionId;
    private ActionType Action;
    private String TMGI;
    private int StartTime;
    private int StopTime;
    private String Version;

    public DeliverySessionCreation(){}

    public DeliverySessionCreation(int id, ActionType action, String tmgi, int startT, int stopT, String version){
        this.DeliverySessionId = id;
        this.Action = action;
        this.TMGI = tmgi;
        this.StartTime = startT;
        this.StopTime = stopT;
        this.Version = version;
        //System.out.println("Create Session creator!");
    }

    public int getDeliverySessionId() {
        return DeliverySessionId;
    }

    public void setDeliverySessionId(int deliverySessionId) {
        DeliverySessionId = deliverySessionId;
    }

    public ActionType getAction() {
        return Action;
    }

    public void setAction(ActionType action) {
        Action = action;
    }

    public String getTMGI() {
        return TMGI;
    }

    public void setTMGI(String TMGI) {
        this.TMGI = TMGI;
    }

    public int getStartTime() {
        return StartTime;
    }

    public void setStartTime(int startTime) {
        StartTime = startTime;
    }

    public int getStopTime() {
        return StopTime;
    }

    public void setStopTime(int stopTime) {
        StopTime = stopTime;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }
}
