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
    public void multipleThreadTest(){
        SessionClient sessionClient = new SessionClient();
        sessionClient.multipleSessions();
    }
    @Test
    public void dynamicThreadPoll() throws InterruptedException {
        SessionClient sessionClient = new SessionClient();
        sessionClient.multipleSessionsWithDynamicPool(25);
    }
}
