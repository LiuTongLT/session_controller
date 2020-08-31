import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SessionClientTest {
    @Before
    public void init(){
        BasicConfigurator.configure();
    }
    @Test
    public void singleThreadTest(){
        SessionClient sessionClient = new SessionClient();
        sessionClient.singleSession();
    }
    @Test
    public void multipleThreadSyncTest(){
        SessionClient sessionClient = new SessionClient();
        sessionClient.multipleSessionsSync();
    }
    @Test
    public void dynamicThreadPoll() throws InterruptedException {
        SessionClient sessionClient = new SessionClient();
        sessionClient.multipleSessionsWithDynamicPool(25);
    }
    @Test
    public void asyncRequest(){
        SessionClient sessionClient = new SessionClient();
        sessionClient.mutipleThreadsAsync();
    }
    @Test
    public void setStopTimeSync(){
        SessionClient sessionClient = new SessionClient();
        System.out.println(sessionClient.setStopTimeSingleT(15));
    }
}
