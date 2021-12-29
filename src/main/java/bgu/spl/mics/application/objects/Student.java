package bgu.spl.mics.application.objects;

import java.util.ArrayList;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
   public enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private transient ArrayList<Model> models;
    private ArrayList<Model> modelsTrained;
    private int publications;
    private int papersRead;
    public Student(String n,String dep,Degree stat){
        name=n;;
        department=dep;
        status=stat;
        models=new ArrayList<Model>();
        publications=0;
        papersRead=0;
        modelsTrained=new ArrayList<>();
    }
    public String getName(){
        return name;
    }
    public void setmodelTrained(Model m){
        modelsTrained.add(m);
    }

public void setPublications(){
        publications++;
}
    public void setPapersRead(int papers){
        papersRead=papers;
    }
    public Degree getStatus(){
        return status;
    }
//    public Model[] getModels(){
//        return models;
//    }
    public ArrayList<Model> getModels(){
        return models;
    }
    public void setModels(ArrayList<Model> listmodels){
        models=listmodels;

    }
}
