package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 *
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
	//(fields)
	private boolean hasResolved;//indicate whether an evet has been resolved
	private T result;
	private Object lockRes;

	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
		//TODO: implement this
		hasResolved=false;
		result=null;
		lockRes=new Object();
	}

	/**
	 * retrieves the result the Future object holds if it has been resolved.
	 * This is a blocking method! It waits for the computation in case it has
	 * not been completed.
	 * <p>
	 * @return return the result of type T if it is available, if not wait until it is available.
	 * 
	 * @pre none
	 * post result!=null (//complete have been done)
	 */
	public  synchronized T get() {
	//	synchronized (lockRes) {
			if (!hasResolved) {
				//synchronized (lockRes) {
					try {
						this.wait();
					} catch (InterruptedException E) {
					}
				}
			return result;
		}



	/**
	 * @param =result
	 * 
	 * @pre none
	 * @post this.get()==result;&&hasResolved=true;
	 */
	public synchronized void  resolve (T result) {
//		synchronized (lockRes) {
			this.result = result;
		hasResolved = true;
			notifyAll();
			//hasResolved = true;
	//	}
	}

	/**
	 * @pre none
	 * @post =hasResolved
	 * @return true if this object has been resolved, false otherwise
	 */
	public boolean isDone() {
		//TODO: implement this.
		return hasResolved;
	}

	public boolean getHasResolved(){//we add
		return hasResolved;
	}

	public T getResult(){//we add
		return result;
	}

	/**
	 * retrieves the result the Future object holds if it has been resolved,
	 * This method is non-blocking, it has a limited amount of time determined
	 * by {@code timeout}
	 * <p>
	 * @param timout 	the maximal amount of time units to wait for the result.
	 * @param unit		the {@link TimeUnit} time units to wait.
	 * @return return the result of type T if it is available, if not,
	 * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
	 *         elapsed, return null.
	 *
	 * @pre none
	 * @post null(after timeout passed and futere has not been resolved)/result.
	 */
	public T get(long timeout, TimeUnit unit) {
		//TODO: implement this.
		return result;
	}

}
