import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;


public class SessionRequest extends TimerTask {

    private Logger logger = Logger.getLogger(RequestTask.class);
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

    public SessionRequest(DeliverySessionCreation deliverySessionCreation){
        this.deliverySessionCreation = deliverySessionCreation;
        URL.append(deliverySessionCreation.getDeliverySessionId());
    }

    @Override
    public void run() {
        logger.info("--------------send request---------------");
        logger.info("DeliverySession id: "+deliverySessionCreation.getDeliverySessionId()+"; Send time: "+df.format(new Date(System.currentTimeMillis()))+"; Action type: "+deliverySessionCreation.getAction());

        Marshaller marshaller = null;
        HttpUriRequest httpUriRequest = null;
        CloseableHttpClient closeableHttpClient = null;
        try {
            //create the .xsd file using marshaller
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
            httpUriRequest = RequestBuilder.post()
                    .setUri(URI.create(URL.toString()))
                    .setEntity(new StringEntity(bodySB.toString(), ContentType.APPLICATION_XML))
                    .build();
            closeableHttpClient = HttpClients.createDefault();

            logger.info("Request body: "+bodySB.toString());

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    logger.info("--------------Receive response------------");
                    int responseCode = httpResponse.getStatusLine().getStatusCode();
                    logger.info("Response code for delivery session: "+deliverySessionCreation.getDeliverySessionId()+": "+responseCode);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    return httpEntity.toString();
                }
            };
            String responseBody = closeableHttpClient.execute(httpUriRequest, responseHandler);

            logger.info("Response for delivery session: "+deliverySessionCreation.getDeliverySessionId() +"; Response body: "+responseBody);
            if(deliverySessionCreation.getAction().equals(ActionType.Start)){
                deliverySessionCreation.setAction(ActionType.Stop);
            }
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
