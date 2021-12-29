package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {
    public Student student;
    public int modelIn;
    ConcurrentHashMap<Model,Future<Model>> modelFu;
    ArrayList<Student> students;
    boolean waitingtestFu;
    int numOfStudents;
    TrainModelEvent e;
   // int numOfStudents;

    public StudentService(String name, Student stud, ArrayList<Student> s) {
        super("StudentService");
        student=stud;
        waitingtestFu=true;
        students=s;
        // TODO Implement this\
        modelIn=0;
        modelFu=new ConcurrentHashMap<Model,Future<Model>>();
         e=null;
    }

    public Student getStudent() {
        return student;
    }


    @Override
    protected void initialize() {
//        System.out.println("student start initialize");
        super.<TickBroadcast>subscribeBroadcast(TickBroadcast.class, new Callback<TickBroadcast>() {
                 //   @Override
                    Future<Model> f = null;

                    public void call(TickBroadcast c) throws InterruptedException {
                        if (f == null) {
                            if (modelIn < getStudent().getModels().size()) {
                                f = sendEvent(new TrainModelEvent(getStudent(), getStudent().getModels().get(modelIn)));
                                modelIn++;
                            }
                        }
                        if (f != null) {
                            if (f.isDone()) {
                              //  System.out.println("future isDone");
                                Model m = f.get();
                                if (m.getStatus() == Model.Status.Trained) {
                                    getStudent().setmodelTrained(m);
                                    f = sendEvent(new TestModelEvent(getStudent(), m));
                                    waitingtestFu = false;
                                } else if (m.getStatus() == Model.Status.Tested) {
                                     //System.out.println("here");
                                    if (f.get().getResult().equals(Model.Result.Good)) {
                                        sendEvent(new PublishResultEvent(getStudent(), f.get()));
                                        f=null;
                                        waitingtestFu=true;
                                    }
                                }
                            }
                        }
                    }

            // If i have model to send
            // send model to train
            // if i aleready sent a model to train, check if future.isDone
            // if future.isdone -> send test
            // if we sent model to test. check isdone
            // if is done -> send publish

        });
        subscribeBroadcast(PublishConferenceBroadcast.class, new Callback<PublishConferenceBroadcast>() {
            public void call(PublishConferenceBroadcast p) {

                Vector<PublishResultEvent> vec = p.getToPublish();
                for (PublishResultEvent pub : vec) {
                    if (pub.getStudent().equals(getStudent())) {
                        getStudent().setPublications();
                        getStudent().setPapersRead(numOfStudents-1);
                        break;

                    }//need to save published models to outputFile?
                }
                getStudent().setPapersRead(numOfStudents);

            }
        });
        subscribeBroadcast(TerminateBroadcast.class, new Callback<TerminateBroadcast>() {
            @Override
            public void call(TerminateBroadcast c) throws InterruptedException {
                terminate();

            }
        });


    }
}

