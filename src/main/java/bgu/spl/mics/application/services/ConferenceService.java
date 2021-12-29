package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.CRMSRunner;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.PublishResultEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.ConfrenceInformation;
import bgu.spl.mics.application.objects.Model;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConferenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {
    //fields
    Vector<PublishResultEvent> toPublish;
    ArrayList<Model> publishModeles;
    // Callback<PublishResultEvent> C;
    ConfrenceInformation conInfo;
    int countTime;

    public ConferenceService(String name, ConfrenceInformation conIn) {
        super("ConferenceService");
        toPublish = new Vector<PublishResultEvent>();
        conInfo = conIn;
        publishModeles = new ArrayList<>();
        countTime = 0;
        // TODO Implement this
    }

//   // public void addElement(PublishResultEvent publish) {//will be called from studentService
//        toPublish.add(publish);
//    }

    //callback-test is good?,
    @Override
    protected void initialize() {//subscribe event(publish result ..),suscribeBroadcast
        super.<TickBroadcast>subscribeBroadcast(TickBroadcast.class, t -> {
            countTime++;
            if (t.getCountTicks() >= conInfo.getDate()) {
                PublishConferenceBroadcast p = new PublishConferenceBroadcast(toPublish);
                conInfo.setPublictions(publishModeles);
                sendBroadcast(p);
                terminate();
            }
        });

        super.<Model, PublishResultEvent>subscribeEvent(PublishResultEvent.class, res -> {
            toPublish.add(res);
            publishModeles.add(res.getModel());

        });
//        subscribeBroadcast(TerminateBroadcast.class, new Callback<TerminateBroadcast>() {
//            @Override
//            public void call(TerminateBroadcast c) throws InterruptedException {
//                System.out.println("student is now terminate");
//                conInfo.setPublictions(publishModeles);
//                terminate();
//
//            }
//        });
        CRMSRunner.countInit.countDown();

    }
}






