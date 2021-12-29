package bgu.spl.mics.application.objects;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.services.GPUService;


import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU<setProcInd> {

    /**
     * Enum representing the type of the GPU.
     */
    public enum Type {RTX3090, RTX2080, GTX1080}

   // MicroService gpuService;
    private Type type;
    Model model;
    int vector_index;
    BlockingQueue<DataBatch> processedData;//need to blocking-depends of gpu type -fix it!!!!!!!!!
    BlockingQueue<DataBatch> unprocessedData;
    int procInd;
    int numOfBatches;
    protected Cluster cluster=Cluster.getInstance();
    int tick;

    //GPUService gpuService;

    public GPU(Type type) {
        this.type = type;
        vector_index=0;
        procInd=0;
        numOfBatches=0;
        unprocessedData=new LinkedBlockingQueue<>();
        processedData=new LinkedBlockingQueue<>();
        tick = 0;
    }
    public Cluster getCluster() {
        return cluster;
    }
    public BlockingQueue<DataBatch> getProcessedData(){
        return processedData;
    }
    public BlockingQueue<DataBatch> getUnprocessedData(){
        return unprocessedData;
    }



    public Model getModel() {
        return model;
    }
    public void setModel(Model m){
        model=m;
    }
    public int getNumOfBatches(){
        return numOfBatches;
    }
    public int maxProsessed(){
        int maxProcessed=0;
        if(type.equals(Type.RTX3090)){
            maxProcessed=32;
        }
        if(type.equals(Type.RTX2080)){
            maxProcessed=16;
        }
        else{
            maxProcessed=8;
        }
        return maxProcessed;
    }
    public void updateTick(){
        tick = tick + 1;
    }
    public void divideData() {
        int batches = (model.getData().getSize()) / 1000;
        int index = 0;
        for (int i = 0; i < batches; i++) {//last iteration may be lass than 1000
            unprocessedData.add(new DataBatch());
        }
        int lastSize = model.getData().getSize() - ((batches) * 1000);
        if (lastSize != 0) {
            unprocessedData.add(new DataBatch());
        }
        numOfBatches=unprocessedData.size();
    }

    /**
     * @pre x=dataBatchvector.size()
     * @after dataBatchvector.size()==x-1
     */

//

    public boolean process(){
        sendClusterUnprocessed();
        return true;
    }
    public void sendClusterUnprocessed() {
      //  if (cluster.getCpus().size() > 0) {
      //  System.out.println("dina");
        if(unprocessedData!=null) {
            Pair<DataBatch, GPU> pair = new Pair(unprocessedData.poll(), this);
          //  cluster.addUnprocessedDataBatch(pair);
            cluster.sendUnprocessedData(pair);
        }
    }

    public void setProcInd(int index){
        procInd=index;
    }
    public void getProcessedData(DataBatch processedDat) {
        procInd++;
       // System.out.println(procInd);
        if (processedData.size() < maxProsessed()) {
            if (processedDat != null) {
                processedData.add(processedDat);
                // procInd++;
                //  System.out.println("arrivee to get processdaata");}
            }
        }
        else {//reached limit want to send to msb.
            processedData = new LinkedBlockingDeque<>();
        }

    }
    public int getProcInd() {
        return procInd;

    }

}
