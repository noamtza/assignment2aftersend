package bgu.spl.mics;

/**
 * The message-bus is a shared object used for communication between
 * micro-services.
 * It should be implemented as a thread-safe singleton.
 * The message-bus implementation must be thread-safe as
 * it is shared between all the micro-services in the system.
 * You must not alter any of the given methods of this interface.
 * You cannot add methods to this interface.
 */
public interface MessageBus {

    /**
     * Subscribes {@code m} to receive {@link Event}s of type {@code type}.
     * <p>
     * @param <T>  The type of the result expected by the completed event.
     * @param type The type to subscribe to,
     * @param m    The subscribing micro-service.
     *
     * @pre m!=null, type!=null
     * @post- type should be in the list of Messages thar m can accept.
     */
    <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m);

    /**
     * Subscribes {@code m} to receive {@link Broadcast}s of type {@code type}.
     * <p>
     * @param type 	The type to subscribe to.
     * @param m    	The subscribing micro-service.
     *
     * @pre m!=null, type!=null
     * @post- m should be in the list of microservices that accept broadcast b.
     *      */

    void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m);


    /**
     * Notifies the MessageBus that the event {@code e} is completed and its
     * result was {@code result}.
     * When this method is called, the message-bus will resolve the {@link Future}
     * object associated with {@link Event} {@code e}.
     * <p>
     * @param <T>    The type of the result expected by the completed event.
     * @param e      The completed event.
     * @param result The resolved result of the completed event.
     *
     * @pre Future<Event<T>>.get()-- wait.
     * @post Future<Event<T>>.get() -return result.
     */
    <T> void complete(Event<T> e, T result);
    //
    /**
     * Adds the {@link Broadcast} {@code b} to the message queues of all the
     * micro-services subscribed to {@code b.getClass()}.
     * <p>
     * @param b 	The message to added to the queues.
     *
     * pre-none
     * post-all micro services that subscribed broadcast b should have b in there waiting message list.
     */
    void sendBroadcast(Broadcast b);
    //new micro service m
    //subscribe broadcast(b,m)
    //after sendBroadcast awaitmessage m=check if  broadcast b

    /**
     * Adds the {@link Event} {@code e} to the message queue of one of the
     * micro-services subscribed to {@code e.getClass()} in a round-robin
     * fashion. This method should be non-blocking.
     * <p>
     * @param <T> The type of the result expected by the event and its corresponding future object.
     * @param e     	The event to add to the queue.
     * @return {@link Future<T>} object to be resolved once the processing is complete,
     * 	       null in case no micro-service has subscribed to {@code e.getClass()}
     *
     * @pre e!=null ,x=e.getMessageQueue().size()
     * @post=return null or-if !return null->e.getMessageQueue().size()=x+1
     */
    <T> Future<T> sendEvent(Event<T> e);


    /**
     * Allocates a message-queue for the {@link MicroService} {@code m}.
     * <p>
     * @param m the micro-service to create a queue for.
     *
     * @pre m.getMessageQueue==null
     * @post m.getMessageQueue.size()==0;
     */

    void register(MicroService m);
    //do:(0-initialize microService m) 1.awaitMessage(m) -should be null.
    //2.register(m) 3.subscribeEvent(E,m)  4.awitMessage(m)==should be type e;

    /**
     * Removes the message queue allocated to {@code m} via the call to
     * {@link #register(bgu.spl.mics.MicroService)} and cleans all references
     * related to {@code m} in this message-bus. If {@code m} was not
     * registered, nothing should happen.
     * <p>
     * @param m the micro-service to unregister.
     * @pre m.getMessageQueue.size()>=0
     * @post m.getMessageQueue==null //should check if all refereces to m have been deleted.
     */
    void unregister(MicroService m);

    /**
     * Using this method, a <b>registered</b> micro-service can take message
     * from its allocated queue.
     * This method is blocking meaning that if no messages
     * are available in the micro-service queue it
     * should wait until a message becomes available.
     * The method should throw the {@link IllegalStateException} in the case
     * where {@code m} was never registered.
     * <p>
     * @param m The micro-service requesting to take a message from its message
     *          queue.
     * @return The next message in the {@code m}'s queue (blocking).
     * @throws InterruptedException if interrupted while waiting for a message
     *                              to became available.
     * @pre none
     * @post m.getMessageQueue!=null
     */
    Message awaitMessage(MicroService m) throws InterruptedException;

}
