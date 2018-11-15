import monitors.Buffer;

public class ProducerS extends Producer {

    private final Buffer buffer;

    public ProducerS(Buffer buffer, int nr) {
        this.buffer = buffer;
        this.nr = nr;
    }

    public ProducerS(Buffer buffer, int nr, boolean verbose) {
        this.buffer = buffer;
        this.nr = nr;
        this.verbose = verbose;
    }

    public ProducerS(Buffer buffer, int nr, int minComputeTime, int maxComputeTime) {
        this.buffer = buffer;
        this.nr = nr;
        this.minComputeTime = minComputeTime;
        this.maxComputeTime = maxComputeTime;
    }

    public ProducerS(Buffer buffer, int nr, int minComputeTime, int maxComputeTime, boolean verbose) {
        this.buffer = buffer;
        this.nr = nr;
        this.minComputeTime = minComputeTime;
        this.maxComputeTime = maxComputeTime;
        this.verbose = verbose;
    }

    void produce() throws InterruptedException {
        Object[] elements = create();
        buffer.put(elements);

        compute();
    }

    private Object[] create() {
        int n = rand.nextInt(buffer.capacity() / 2) + 1;
        Object[] elements = new Object[n];
        for (int i = 0; i < n; i++) {
            elements[i] = String.format("[%d] (%d) %d", nr, cycles, i);
        }
        return elements;
    }

}
