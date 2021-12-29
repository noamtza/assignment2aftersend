package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.GPUService;
//import javafx.util.Pair;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class CPU {
    int cores;
    Vector<DataBatch> data;
    int index;
    CPUService cpuService;
    Pair<DataBatch, GPU> toProcess;

    int countTicks;
    protected Cluster cluster=Cluster.getInstance();
    int waitingTime;
    int tick = 0;


    public CPU(int cores) {
        this.cores = cores;
        cluster=cluster.getInstance();
        countTicks=0;
        toProcess=null;
        waitingTime=0;
    }

    public int getNumberOfCore() {
        return cores;
    }
        public void updateTick(){
        countTicks++ ;
    }

    public Pair<DataBatch, GPU> getToProcess(){
        return toProcess;
    }

    public void getUnprossesedDataBatch(Pair<DataBatch, GPU> pair) {
        toProcess = pair;
        //int wait = waitingTime;
        // System.out.println("arived!!!!");
        //if(this.countTicks==wait){
        int wait=this.getWaitingTime();
        //System.out.println("waitingTimeIs");
        //System.out.println(getWaitingTime());
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
                this.sendProcessData();
                getCluster().setTotalDataBatches();
                countTicks = 0;
                getCluster().setCpuTimeUnitUsed(wait);
                cluster.setAvilableCpus();
                cluster.addCpu(this);
            }


    public int getWaitingTime() {
            int waitingTime = -1;
            if (toProcess != null) {
                if (toProcess.getFirst() != null) {
                    if (toProcess.getSecond() .getModel() != null) {
                        if (toProcess.getSecond().getModel().getData().getType().equals(Data.Type.Images)) {
                            waitingTime = (32 / cores) * 4;
                        } else if (toProcess.getSecond().getModel().getData().getType().equals(Data.Type.Text)) {
                            waitingTime = (32 / cores) * 2;
                        } else {
                            waitingTime = (32 / cores) * 1;
                        }
                    }
                }
            }
        return waitingTime;
    }


    public void sendProcessData()  {
        if (toProcess != null) {
            cluster.addprocessedDataBatch(toProcess);
        }
    }

    //toDo
    public Cluster getCluster(){
        return cluster;
    }

    /**
     * @pre none
     * @after index=index+1
     */

}