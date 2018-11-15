package active_object;


public class Proxy {
    private ActivationQueue activationQueue = new ActivationQueue();
    private Scheduler scheduler;
    private RingBuffer ringBuffer;
    private boolean verbose = false;

    public int capacity(){
        return ringBuffer.capacity();
    }

    public void stopScheduler() throws InterruptedException {
        scheduler.interrupt();
        scheduler.join();
    }

    public Proxy(int capacity){
        this.ringBuffer = new RingBuffer(capacity);
        this.scheduler = new Scheduler(activationQueue, false);
        scheduler.start();
    }

    public Proxy(int capacity, boolean verbose){
        this.ringBuffer = new RingBuffer(capacity);
        this.scheduler = new Scheduler(activationQueue, verbose);
        this.verbose = verbose;
        scheduler.start();
    }

    public Future<Object[]> take(int n) throws InterruptedException {
        TakeRequest request = new TakeRequest(ringBuffer, n, verbose);
        activationQueue.enqueue(request);
        return request.getFuture();
    }

    public Future<Boolean> put(Object[] elements) throws InterruptedException {
        PutRequest request = new PutRequest(ringBuffer, elements, verbose);
        activationQueue.enqueue(request);
        return request.getFuture();
    }
}
