import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;


public class RequestTaskSync extends TimerTask {

    private Logger logger = Logger.getLogger(RequestTaskSync.class);
    private static SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private JAXBContext jaxbContext;

    private DeliverySessionCreation deliverySessionCreation;
    private StringBuilder URL = new StringBuilder("http://127.0.0.1:8081/nbi/deliverysession?id=");

    public RequestTaskSync(DeliverySessionCreation deliverySessionCreation){
        this.deliverySessionCreation = deliverySessionCreation;
        URL.append(deliverySessionCreation.getDeliverySessionId());
        try {
            jaxbContext = JAXBContext.newInstance(DeliverySessionCreation.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        logger.info("--------------send request---------------");
        logger.info("DeliverySession id: "+deliverySessionCreation.getDeliverySessionId()+"; Send time: "+df.format(new Date(System.currentTimeMillis()))+"; Action type: "+deliverySessionCreation.getAction());

        Marshaller marshaller = null;
        HttpUriRequest httpUriRequest = null;
        CloseableHttpClient closeableHttpClient = null;
        PoolingHttpClientConnectionManager manager = null;
        try {
            manager = new PoolingHttpClientConnectionManager();
            //The max connections
            manager.setMaxTotal(200);
            //The max num of connections per route
            manager.setDefaultMaxPerRoute(100);
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
                    .setHeader("Connection","keep-alive")
                    .setEntity(new StringEntity(bodySB.toString(), ContentType.APPLICATION_XML))
                    .build();
            closeableHttpClient = HttpClients.custom().setConnectionManager(manager).build();

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
