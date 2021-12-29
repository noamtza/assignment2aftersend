package bgu.spl.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {
    public enum Status {PreTrained,Training,Trained,Tested}
    public enum Result {None,Good,Bad}

    private String name;
    private Data data;
    private transient Student student;
    private Status status;
    private Result result;

    public Model(String n,Data dat,Student s){
        name=n;
        data=dat;
        student=s;
        status=Status.PreTrained;
        result=Result.None;
    }
    public Data getData(){
        return data;
    }

    public String getName(){
        return name;
    }
    public Result getResult(){
        return result;
    }
    public Status getStatus(){
        return status;
    }
    public void setStatus(Status s){
        status=s;
    }
    public void setResult(Result res){
        result=res;
    }
}