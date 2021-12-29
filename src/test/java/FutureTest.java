import bgu.spl.mics.Future;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

//import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;

class FutureTest {
    private static Future<String > future;

    @BeforeEach
    public void setUp()  {
        future=new Future<String>();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
     void testGet() throws InterruptedException{
        Thread waitResolve = new Thread() {
            public void run() {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException E) {
                    E.printStackTrace();
                }
                future.resolve("test resolve");
            }
        };
        Thread getThread = new Thread() {
            public void run() {
                assertTrue("test resolve" == future.get());
                try {
                    Thread.currentThread().join();
                } catch (InterruptedException E) {
                    E.printStackTrace();
                }
            }
        };
        waitResolve.start();
        getThread.start();
    }

    @Test
    void resolve() {
        future.resolve("testresult");//
        assertEquals("testresult", future.getResult());
        assertTrue(future.getHasResolved());
    }

    @Test
    public void isDone() {
        boolean resolved=future.getHasResolved();
        assertFalse(future.isDone());
        future.resolve("SomeResult");
        assertTrue(future.isDone());
    }

    @Test
    void getHasResolved() {
    }

    @Test
    void getResult() {
    }

    @Test
    void Get() {
        Thread waitResolve = new Thread() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException E) {
                    E.printStackTrace();
                }
                future.resolve("test resolve");
            }
        };
        Thread getThread = new Thread() {
            public void run() {
                assertNull(future.get(50,TimeUnit.MILLISECONDS));
                try {
                    Thread.currentThread().join();
                } catch (InterruptedException E) {
                    E.printStackTrace();
                }
            }
        };
        waitResolve.start();
        getThread.start();
    }

        }


