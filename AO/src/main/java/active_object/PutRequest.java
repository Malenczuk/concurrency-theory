package active_object;

public class PutRequest extends MethodRequest {
    private RingBuffer ringBuffer;
    private boolean verbose = false;
    private Object[] elements;
    private Future<Boolean> future = new Future<>();

    PutRequest(RingBuffer ringBuffer, Object[] elements, boolean verbose) {
        this.type = RequestType.PRODUCE;
        this.ringBuffer = ringBuffer;
        this.elements = elements;
        this.verbose = verbose;
    }

    Future<Boolean> getFuture() {
        return future;
    }

    @Override
    public boolean guard() {
        if (verbose && !(ringBuffer.remainingCapacity() >= elements.length))
            System.out.println(String.format("[ BUFFER | %d/%d ] X <==[%d]==", ringBuffer.remainingCapacity(), ringBuffer.capacity(), elements.length));
        return ringBuffer.remainingCapacity() >= elements.length;
    }

    @Override
    public void execute() {
        if (verbose)
            System.out.println(String.format("[ BUFFER | %d/%d ] <==[%d]==", ringBuffer.remainingCapacity(), ringBuffer.capacity(), elements.length));
        future.setResult(ringBuffer.put(elements));
    }
}
