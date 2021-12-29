package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import javax.xml.transform.Result;
import java.util.Queue;

public class TestModelEvent implements Event<Model> {
   // private Queue<MicroService> registerServices;
    Student student;
   Model model;
   Future future;


    public TestModelEvent(Student s,Model mod){
        student=s;
        model=mod;
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
