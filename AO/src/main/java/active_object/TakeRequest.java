package active_object;

public class TakeRequest extends MethodRequest {

    private RingBuffer ringBuffer;
    private boolean verbose = false;
    private int n;
    private Future<Object[]> future = new Future<>();

    TakeRequest(RingBuffer ringBuffer, int n, boolean verbose) {
        this.type = RequestType.CONSUME;
        this.ringBuffer = ringBuffer;
        this.n = n;
        this.verbose = verbose;
    }

    Future<Object[]> getFuture() {
        return future;
    }

    @Override
    public boolean guard() {
        if(verbose && !(ringBuffer.available() >= n))
            System.out.println(String.format("[ BUFFER | %d/%d ] X ==[%d]==>", ringBuffer.remainingCapacity(), ringBuffer.capacity(), n));
        return ringBuffer.available() >= n;
    }

    @Override
    public void execute() {
        if(verbose)
            System.out.println(String.format("[ BUFFER | %d/%d ] ==[%d]==>", ringBuffer.remainingCapacity(), ringBuffer.capacity(), n));
        future.setResult(ringBuffer.take(n));
    }
}
