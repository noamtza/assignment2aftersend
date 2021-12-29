package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {
    int TickTime;
    int duration;
    int currTick;
    private static final TimeService instance = new TimeService();

    public TimeService() {
        super("TimeService");
        currTick = 0;
        // TODO Implement this
    }


    public int getCurrTick() {
        return currTick;
    }

    @Override
    protected void initialize() {
        // TODO Implement this
        while (currTick <= duration) {
            //System.out.println(currTick);
            currTick++;
            try {
                Thread.sleep(TickTime);
            } catch (InterruptedException E) {
            }
            sendBroadcast(new TickBroadcast(currTick));
        }
        sendBroadcast(new TerminateBroadcast());
    }

    public void setDuration(int dur) {
        duration = dur;
    }

    public void setTickTime(int tickTime) {
        TickTime = tickTime;
    }
}
