package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;

/**
 * CPU service is responsible for handling the {@link DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {
    CPU cpu;
    int countTicks;
    int timeUsed;
    public CPUService(String name,CPU cp) {
        super("CPU SERVICE");
        cpu=cp;
        countTicks=0;
        // TODO Implement this

    }

    @Override
    protected void initialize() {
        super.<TickBroadcast>subscribeBroadcast(TickBroadcast.class, t -> {
            cpu.updateTick();
            if(cpu.getToProcess()!=null){
                countTicks++;}

        });
        subscribeBroadcast(TerminateBroadcast.class, new Callback<TerminateBroadcast>() {
            @Override
            public void call(TerminateBroadcast c) throws InterruptedException {
                System.out.println("student is now terminate");
                terminate();

            }
        });

        // TODO Implement this

    }
}