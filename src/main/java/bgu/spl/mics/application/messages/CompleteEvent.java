package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;

public class CompleteEvent<Model> implements Event<Model> {
    Model model;
    public CompleteEvent(Model m){
        model=m;
    }
    public Future getFu() {
        return null;
    }
}
