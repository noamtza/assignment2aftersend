import bgu.spl.mics.*;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
//    MessageBusImpl meesageBusImp;
//    @BeforeEach
//    void setUp() {
//       meesageBusImp=new MessageBusImpl();
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void testSubscribeEvent() {
//        MicroService m=new StudentService("Test");
//        ExampleEvent e=new ExampleEvent("TestEvent");
//        meesageBusImp.register(m);
//        meesageBusImp.subscribeEvent(e.getClass(),m);
//        Message M=null;
//        meesageBusImp.sendEvent(e);
//        try {
//             M=meesageBusImp.awaitMessage(m);
//        }catch (Exception ignore){
//
//        }
//        assertNotNull(M);
//    }
//
//    @Test
//    void testSubscribeBroadcast() {
//        MicroService m=new StudentService("Test");
//        Broadcast b=new ExampleBroadcast("testBroadcast");
//        meesageBusImp.register(m);
//        meesageBusImp.subscribeBroadcast(b.getClass(),m);
//        Message M=null;
//        meesageBusImp.sendBroadcast(b);
//        try {
//            M=meesageBusImp.awaitMessage(m);
//        }catch (Exception ignore){
//
//        }
//        assertNotNull(M);
//    }
//
//    @Test
//    void testComplete() {
//        MicroService m=new StudentService("Test");
//        meesageBusImp.register(m);
//        ExampleEvent e=new ExampleEvent("TestEvent");
//        meesageBusImp.subscribeEvent(e.getClass(),m);
//        meesageBusImp.complete(e,"testResult");
//        assertEquals(meesageBusImp.sendEvent(e).getResult(),"testResult");
//    }
//
//    @Test
//    void testSendBroadcast() {
//        //same as subscribeBroadcast()
//    }
//
//    @Test
//    void testSendEvent() {
//        MicroService m=new StudentService("Test");
//        meesageBusImp.register(m);
//        ExampleEvent e=new ExampleEvent("TestEvent");
//        assertNull(meesageBusImp.sendEvent(e));//test 1
//
//        meesageBusImp.subscribeEvent(e.getClass(),m);
//        Message M=null;
//        meesageBusImp.sendEvent(e);
//        try {
//            M=meesageBusImp.awaitMessage(m);
//        }catch (Exception ignore){
//
//        }
//        assertNotNull(M);
//    }
//    @Test
//    void testRegister() {
//        //same as subscribeBroadcast()
//    }
//
//    @Test
//    void testUnregister() {
//        MicroService m=new StudentService("Test");
//        meesageBusImp.register(m);
//        ExampleEvent e=new ExampleEvent("TestEvent");
//        meesageBusImp.subscribeEvent(e.getClass(),m);
//        meesageBusImp.unregister(m);
//        assertNull(meesageBusImp.sendEvent(e));
//    }
//
//    @Test
//    void testAwaitMessage() throws InterruptedException{
//        MicroService m=new StudentService("Test");
//        meesageBusImp.register(m);
//        ExampleEvent e=new ExampleEvent("TestEvent");
//        meesageBusImp.subscribeEvent(e.getClass(),m);
//            Thread SendMessage = new Thread() {
//                public void run() {
//                    try {
//                        Thread.sleep(150);
//                    } catch (InterruptedException E) {
//                        E.printStackTrace();
//                    }
//                    meesageBusImp.sendEvent(e);
//                }
//            };
//            Thread awaitThread = new Thread() {
//                public void run() {
//                    try {
//                        Thread.currentThread().join();
//                        assertEquals("testEvent",meesageBusImp.awaitMessage(m));
//                    } catch (InterruptedException E) {
//                        E.printStackTrace();
//                    }
//                }
//            };
//        SendMessage.start();
//        awaitThread.start();
//        }
    }
