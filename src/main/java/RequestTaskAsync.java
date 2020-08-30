import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public class RequestTaskAsync extends TimerTask {
    private Logger logger = Logger.getLogger(RequestTaskAsync.class);
    private static SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static JAXBContext jaxbContext;

    static{
        try {
            jaxbContext = JAXBContext.newInstance(DeliverySessionCreation.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    private DeliverySessionCreation deliverySessionCreation;
    private StringBuilder URL = new StringBuilder("http://127.0.0.1:8081/nbi/deliverysession?id=");

    public RequestTaskAsync(DeliverySessionCreation deliverySessionCreation){
        this.deliverySessionCreation = deliverySessionCreation;
        URL.append(deliverySessionCreation.getDeliverySessionId());
    }
    @Override
    public void run() {
        requestAsyn();
    }

    public void requestAsyn(){
        logger.info("--------------send request---------------");
        logger.info("DeliverySession id: "+deliverySessionCreation.getDeliverySessionId()+"; Send time: "+df.format(new Date(System.currentTimeMillis()))+"; Action type: "+deliverySessionCreation.getAction());
        //logger.info("Send time: "+ df.format(new Date(System.currentTimeMillis())));

        HttpClient client =  null;
        HttpRequest request = null;
        Marshaller marshaller = null;
        try {
            marshaller =  jaxbContext.createMarshaller();
            File file = new File("src/main/resources/sessions.xsd");
            marshaller.marshal(deliverySessionCreation,file);
            //parse .xsd file to string
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder bodySB = new StringBuilder();
            while((line=bufferedReader.readLine())!=null){
                bodySB.append(line);
            }

            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(bodySB.toString()))
                    .uri(URI.create(URL.toString()))
                    .build();
            logger.info("Request body: "+bodySB.toString());

            client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            CompletableFuture<HttpResponse<String>> response =  client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            logger.info("--------------Receive response------------");
            HttpResponse<String> res =  response.get();
            logger.info("Response for delivery session: "+deliverySessionCreation.getDeliverySessionId() +"; Response code: "+res.statusCode()+"; Response header: "+ res.headers());

            if(deliverySessionCreation.getAction().equals(ActionType.Start)){
                deliverySessionCreation.setAction(ActionType.Stop);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestSync(){
        logger.info("--------------send request---------------");
        logger.info("DeliverySession id: "+deliverySessionCreation.getDeliverySessionId()+"; Send time: "+df.format(new Date(System.currentTimeMillis())));
        //logger.info("Send time: "+ df.format(new Date(System.currentTimeMillis())));

        HttpClient client = null;
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header("accept","text/plain")
                    .uri(URI.create(URL.toString()))
                    .build();
            client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("--------------Receive response------------");
            logger.info("Response for delivery session: "+deliverySessionCreation.getDeliverySessionId() +"; Response code: "+response.statusCode()+"; Response header: "+ response.headers());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
