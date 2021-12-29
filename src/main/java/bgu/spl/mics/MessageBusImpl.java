package bgu.spl.mics;

import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.objects.Model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	//fields(we add)
	//messages queues for all microServices
	ConcurrentHashMap<MicroService, BlockingQueue<Message>> microServiceQueues;
	ConcurrentHashMap<Class<? extends Message>, BlockingQueue<MicroService>> MessagesSubscribes;
	ConcurrentHashMap<Event, Future> eventFu;
	private Object lockMes;

	private static class BusInstance {
		private static final MessageBusImpl instance = new MessageBusImpl();
	}

	public ConcurrentHashMap<Event, Future> getEventFu() {
		return eventFu;
	}

	private MessageBusImpl() {//check
		microServiceQueues = new ConcurrentHashMap<>();
		MessagesSubscribes = new ConcurrentHashMap<>();
		eventFu = new ConcurrentHashMap<>();
		lockMes = new Object();


	}


	public static MessageBusImpl getInstance() {
		return BusInstance.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if (MessagesSubscribes.containsKey(type)) {
			MessagesSubscribes.get(type).add(m);
		} else {
			MessagesSubscribes.put(type, new LinkedBlockingQueue<MicroService>());
			MessagesSubscribes.get(type).add(m);
		}

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (MessagesSubscribes.containsKey(type)) {
			MessagesSubscribes.get(type).add(m);
		} else {
			MessagesSubscribes.put(type, new LinkedBlockingQueue<MicroService>());
			MessagesSubscribes.get(type).add(m);
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		//synchronized (lockMes){
		// TODO Auto-generated method stub
		if (eventFu.containsKey(e)) {
			eventFu.get(e).resolve(result);
			//notifyAll();
		}//}
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		//BlockingQueue<MicroService> toSend = MessagesSubscribes.get(b.getClass());
			if (MessagesSubscribes.get(b.getClass()) != null && MessagesSubscribes.get(b.getClass()).size() > 0) {
				MicroService first = MessagesSubscribes.get(b.getClass()).poll();
				if (microServiceQueues.containsKey(first)) {
				microServiceQueues.get(first).add(b);}
				if (first != null && MessagesSubscribes.get(b.getClass()).size() > 0) {
					MessagesSubscribes.get(b.getClass()).add(first);
					while (MessagesSubscribes.get(b.getClass()).peek() != first) {
						MicroService m = MessagesSubscribes.get(b.getClass()).poll();
						if (microServiceQueues.containsKey(m)) {
							microServiceQueues.get(m).add(b);
						}
						MessagesSubscribes.get(b.getClass()).add(m);
					}
				}
			}
		}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {//roundrobin
		if (MessagesSubscribes.get(e.getClass()) != null && MessagesSubscribes.get(e.getClass()).size() > 0) {
			MicroService m = MessagesSubscribes.get(e.getClass()).poll();
			if (m != null) {
				microServiceQueues.get(m).add(e);
				MessagesSubscribes.get(e.getClass()).add(m);
				Future f = new Future<>();
				eventFu.put(e, f);
				return f;

			}
			else{
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public void register(MicroService m) {
		microServiceQueues.put(m, new LinkedBlockingQueue<Message>());
		m.setIsRegister(true);
	}
//}

	@Override
	public void unregister(MicroService m) {
		m.setIsRegister(false);
		if(microServiceQueues.contains(m)) {
			for (Message s : microServiceQueues.get(m)) {
				MessagesSubscribes.get(s.getClass()).remove(m);
			}
			microServiceQueues.remove(m);
		}
	}
	// TODO Auto-generated method stub


	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		if (microServiceQueues.containsKey(m)) {
			Message myMes = microServiceQueues.get(m).take();
			return myMes;
		}
		return null;
	}
}
