package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.Queue;

public class PublishResultEvent implements Event<Model> {
    //private Queue<MicroService> registerServices;
    Student student;
    Model model;
    Future future;
    public PublishResultEvent (Student s,Model m){//need to be pointers to the right student/model
        this.student=s;
        this.model=m;
        future=null;
    }
    public Student getStudent(){
        return student;
    }
    public Model getModel(){
        return model;
    }
    public Future getFu(){
        return future;
    }




}
