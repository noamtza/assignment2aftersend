package bgu.spl.mics.application.objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GPUTest {
GPU gpu;
    @BeforeEach
    void setUp() {
        gpu=new GPU(GPU.Type.RTX2080);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getModel() {
    }

    @Test
    void testSendDataBatch() {
//        int preSend=gpu.getCluster().getDataQueue().size();
//        gpu.sendDataBatch();
//        int afterSend=gpu.getCluster().getDataQueue().size();
//        assertTrue(preSend<afterSend);

    }

}