import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Timer;

public class SessionXmlTest {
    private JAXBContext context;
    @Before
    public void init() throws JAXBException {
        this.context = JAXBContext.newInstance(DeliverySession.class);
    }
    @Test
    public void createXmlFile() throws JAXBException {
        Marshaller marshaller = this.context.createMarshaller();
        DeliverySessionCreation session = new DeliverySessionCreation(1,ActionType.Start,"TMGI",123,456,"a");
        DeliverySession deliverySession = new DeliverySession(session);
        marshaller.marshal(deliverySession,new File("src/main/resources/sessions.xsd"));
    }
}
