package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.ConferenceService;

import java.util.ArrayList;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConfrenceInformation {

    private String name;
    private int date;
    public int tick;
    private ArrayList<Model> conpublictions;
    public  ConfrenceInformation(String n, int d){
        name=n;
        date=d;
        tick = 0;
        conpublictions=new ArrayList<>();
    }
    public int getDate(){
        return date;
    }

    public void updateTick(){
        tick = tick + 1;
    }
    public void setPublictions(ArrayList<Model> models){
        conpublictions=models;
    }
}
