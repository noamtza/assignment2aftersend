//package bgu.spl.mics.application.objects;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class CPUTest {
//    CPU cpu;
//
//    @BeforeEach
//    void setUp() {
//        cpu=new CPU(5);
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void sendProcesedData() {
//        int preSend=cpu.getCluster().getProcessedDataQueue().size();
//        cpu.sendProcesedData();
//        int afterSend=cpu.getCluster().getProcessedDataQueue().size();
//        assertTrue(preSend<afterSend);
//    }
//
//
//
//}