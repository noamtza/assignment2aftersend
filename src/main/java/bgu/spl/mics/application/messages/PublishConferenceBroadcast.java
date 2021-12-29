package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class PublishConferenceBroadcast implements Broadcast {
    //fields
   // private Queue<MicroService> registerServices;//need yo initialize as an empty queue at constructor
int serviceIndex;//will be used to folow the round robin routine;
    LinkedList<Student> students;
    public Vector <PublishResultEvent> toPublish;


    public PublishConferenceBroadcast(Vector<PublishResultEvent> publish){
        toPublish=publish;
    }
    public Vector getToPublish(){
        return toPublish;
    }


}
