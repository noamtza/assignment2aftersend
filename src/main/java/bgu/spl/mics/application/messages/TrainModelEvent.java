package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

import java.util.Queue;

public class TrainModelEvent implements Event<Model> {
    Student student;
    Model model;
    Future<Model> future;
   // private Queue<MicroService> registerServices;
   public TrainModelEvent(Student s ,Model m){
       student=s;
       model=m;
       future=new Future<Model>();
   }
   public Model getModel(){
       return model;
   }
   public void setFu(Future fu){
       future=fu;
   }

    public Student getStudent() {
        return student;
    }
    public Future getFu(){
       return future;
    }
}
