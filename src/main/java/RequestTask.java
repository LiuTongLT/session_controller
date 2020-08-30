import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class RequestTask extends TimerTask {
    private Logger logger = Logger.getLogger(RequestTask.class);
    private static SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private DeliverySessionCreation deliverySessionCreation;
    private StringBuilder URL = new StringBuilder("http://127.0.0.1:8081/nbi/deliverysession?id=");

    public RequestTask(DeliverySessionCreation deliverySessionCreation){
        this.deliverySessionCreation = deliverySessionCreation;
        URL.append(deliverySessionCreation.getDeliverySessionId());
    }
    @Override
    public void run() {
        logger.info("--------------send request---------------");
        logger.info("DeliverySession id: "+deliverySessionCreation.getDeliverySessionId()+"; Send time: "+df.format(new Date(System.currentTimeMillis())));
        //logger.info("Send time: "+ df.format(new Date(System.currentTimeMillis())));

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header("accept","text/plain")
                    .uri(URI.create(URL.toString()))
                    .build();


            logger.info("Request body: "+request.bodyPublisher().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("--------------Receive response------------");
            logger.info("Response for delivery session: "+deliverySessionCreation.getDeliverySessionId() +"; Response code: "+response.statusCode()+"; Response header: "+ response.headers());
            //logger.info("Response code: "+response.statusCode());
            //System.out.println(response.headers());
            //System.out.println(response.statusCode());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
