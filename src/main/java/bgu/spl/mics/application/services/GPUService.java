package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 //* in addition to sending the {@link }.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {
    GPU currGpu;
    Event currEvent;
    int countTicks;
    Object lock;
    boolean isTickedcalled;
    int numOfbatches;
//
    public GPUService(String name, GPU gpu) {
        super("gpu service");
        // TODO Implement this
        currGpu = gpu;
        currEvent=null;
        countTicks=0;
        lock=new Object();
        isTickedcalled=false;
        numOfbatches=0;

        //future=null;

    }

    @Override
    protected void initialize() {
        // TODO Implement this
        subscribeBroadcast(TickBroadcast.class, t -> {
         //System.out.println("tick broadcast gpuservice");
          currGpu.updateTick();
          //  if (currEvent != null) {
                    if (currGpu.getProcessedData().size() > 0 || currGpu.getUnprocessedData().size() > 0) {
                        countTicks++;
                    }
                   if (currEvent != null) {
                        currGpu.sendClusterUnprocessed();
                        if (currGpu.getProcInd() == numOfbatches&&currGpu.getNumOfBatches()!=0) {
                            mesB.complete(currEvent, currGpu.getModel());
                            currGpu.getModel().setStatus(Model.Status.Trained);
                            // currEvent.getFu().resolve(currGpu.getModel());
                            currGpu.getCluster().addModel(currGpu.getModel());
                            currGpu.getCluster().setGpuTimeUnitUsed(countTicks);
                            //currGpu.setProcInd(0);
                        }
                        isTickedcalled = true;
                    }
                //}
        });
        subscribeEvent(TrainModelEvent.class, c -> {
            currEvent=c;
            numOfbatches=(c.getModel().getData().getSize())/1000;
            currGpu.setModel(c.getModel());
            currGpu.divideData();
            });
      //  super.<TickBroadcast>subscribeBroadcast(TickBroadcast.class, t -> {
//            subscribeBroadcast(TickBroadcast.class, t -> {
//                    System.out.println("gpu start tick callback");
//                    if (currGpu.getProcessedData().size() > 0 || currGpu.getUnprocessedData().size() > 0) {
//                        countTicks++;
//                    }
//                    currGpu.sendClusterUnprocessed();
//                    if (currGpu.getProcInd() == currGpu.getNumOfBatches()) {
//                        mesB.complete(currEvent, currGpu.getModel());
//                        currGpu.getCluster().getStatistics().addModel(currGpu.getModel());
//                        currGpu.getCluster().getStatistics().setGpuTimeUnitUsed(countTicks);
//                    }
//                    System.out.println("gpu subscribed");
//                    notifyAll();
//        });
        subscribeEvent(TestModelEvent.class, c -> {
            if(currEvent!=null) {
                Model ReModel = c.getModel();
                if ((c.getStudent().getStatus()).equals(Student.Degree.MSc)) {//Msc student-0.6 to be good
                    if (Math.random() < 0.6)
                        ReModel.setResult(Model.Result.Good);
                    else
                        ReModel.setResult(Model.Result.Bad);

                } else if ((c.getStudent().getStatus()).equals(Student.Degree.PhD)) {
                    if (Math.random() < 0.8)
                        ReModel.setResult(Model.Result.Good);
                    else
                        ReModel.setResult(Model.Result.Bad);
                }
                mesB.complete(c, ReModel);
                ReModel.setStatus(Model.Status.Tested);
            }
        });
        subscribeBroadcast(TerminateBroadcast.class, new Callback<TerminateBroadcast>() {
            @Override
            public void call(TerminateBroadcast c) throws InterruptedException {
                terminate();

            }
        });

        CRMSRunner.countInit.countDown();
    }
}
