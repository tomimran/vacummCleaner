package bgu.spl.mics;
//this is a git test
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private static MessageBusImpl instance; //this is the only instance of MessageBus
	private final ConcurrentHashMap<MicroService, Queue<Message>> serviceQueueMap; //contains the queues of all registered MicroService
	private final ConcurrentHashMap<Class<? extends Event<?>>, Queue<MicroService>> eventQueueMap; //contains queue of the registered MicroServices for each type of event
	private final ConcurrentHashMap<Class<? extends Broadcast>, List<MicroService>> broadcastListMap; //contains list of the registered MicroService for each type of broadcast
	private final ConcurrentHashMap<Event<?>, Future<?>> ongoingEvents; //contains the events that already assigned to specific MicroService but haven't completed yet


	private MessageBusImpl() { //constructor
		serviceQueueMap = new ConcurrentHashMap<>();
		eventQueueMap = new ConcurrentHashMap<>();
		broadcastListMap = new ConcurrentHashMap<>();
		ongoingEvents = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance() { //returns the only instance of MessageBus
		if (instance != null) {
			return instance;
		}
		synchronized (MessageBusImpl.class) {
			if (instance != null) {
				instance = new MessageBusImpl();
			}
		}
		return instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		eventQueueMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
		eventQueueMap.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		broadcastListMap.putIfAbsent(type, new ArrayList<>());
		broadcastListMap.get(type).add(m);
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		Future<T> future = (Future<T>) ongoingEvents.remove(e);
		if (future != null) {
			future.resolve(result);
		}
	}

	@Override
	public void sendBroadcast(Broadcast b) { //maybe we should use synchronize here
		List<MicroService> l = broadcastListMap.get(b.getClass());
		if (l != null) {
			for (MicroService m : l) {
				Queue<Message> queue = serviceQueueMap.get(m);
				if (queue != null) {
					queue.add(b);
				}
			}
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) { //we should use synchronize here
		//potential issue: assigning event to unregistered MicroService
		boolean completed = false;
		Queue<MicroService> q = eventQueueMap.get(e.getClass());
		if (q != null) {
			synchronized (q) {
				MicroService m = q.poll();
				q.add(m);
				Queue<Message> serviceQ = serviceQueueMap.get(m);
				if (serviceQ != null) {
					serviceQ.add(e);

					Future<T> f = new Future<>();
					ongoingEvents.put(e, f);
					notifyAll();
					return f;
				}
			}
		}
		return null;
	}

	@Override
	public void register(MicroService m) {
		if (m != null) {
			serviceQueueMap.putIfAbsent(m, new ConcurrentLinkedQueue<>());
		}
	}

	@Override
	public void unregister(MicroService m) { //didnt complete yet
		synchronized (m){}
		for (Queue<MicroService> q : eventQueueMap.values()) {
			synchronized (q) {
				q.remove(m);
			}
		}
		for (List<MicroService> q : broadcastListMap.values()) {
			q.remove(m);
		}
		Queue<Message> uncompletedEvents = serviceQueueMap.remove(m);
		for (Message message : uncompletedEvents) {
			if (message instanceof Event<?>) {
				sendEvent((Event<?>)message);
			}
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		Queue<Message> q = serviceQueueMap.get(m);
		synchronized (q) {
			while (q.isEmpty()) {
				try {
					q.wait();
				}
				catch (InterruptedException e) {
					return null;
				}
			}
			notifyAll();
			return q.poll();
		}
	}
}
