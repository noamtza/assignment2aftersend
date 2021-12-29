package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;

import java.util.Queue;

public class TickBroadcast implements Broadcast {

   //. private Queue<MicroService> registerServices;
    int countTicks;//count ticks that recived from timeService;
    public TickBroadcast(int currTick){
        countTicks=currTick;
    };

    public int getCountTicks(){
        return countTicks;
    }


}
