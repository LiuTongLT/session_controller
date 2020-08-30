import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SessionClient {
    private static final String URL = "http://127.0.0.1:8081/nbi/deliverysession?id=1";
    public static void main(String[] args) {

        //To deal with the error "No appenders could be found for logger"
        BasicConfigurator.configure();

//        HttpClient client = HttpClient.newBuilder()
//                .version(HttpClient.Version.HTTP_1_1)
//                .build();
//        HttpRequest request = HttpRequest.newBuilder()
//                .POST(HttpRequest.BodyPublishers.noBody())
//                .header("accept","text/plain")
//                .uri(URI.create(URL))
//                .build();
//        try {
//            HttpResponse<String> response =  client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.headers());
//            System.out.println(response.statusCode());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        DeliverySessionCreation deliverySessionCreation = new DeliverySessionCreation(1,ActionType.Start,"TMGI",1,4,"a");
        DeliverySession deliverySession = new DeliverySession(deliverySessionCreation);
        deliverySession.setTimer();

    }
}

