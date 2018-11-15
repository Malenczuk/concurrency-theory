import monitors.Buffer;

public class ConsumerS extends Consumer {
    private final Buffer buffer;

    public ConsumerS(Buffer buffer, int nr) {
        this.buffer = buffer;
        this.nr = nr;
    }

    public ConsumerS(Buffer buffer, int nr, boolean verbose) {
        this.buffer = buffer;
        this.nr = nr;
        this.verbose = verbose;
    }

    public ConsumerS(Buffer buffer, int nr, int minComputeTime, int maxComputeTime) {
        this.buffer = buffer;
        this.nr = nr;
        this.minComputeTime = minComputeTime;
        this.maxComputeTime = maxComputeTime;
    }

    public ConsumerS(Buffer buffer, int nr, int minComputeTime, int maxComputeTime, boolean verbose) {
        this.buffer = buffer;
        this.nr = nr;
        this.minComputeTime = minComputeTime;
        this.maxComputeTime = maxComputeTime;
        this.verbose = verbose;
    }

    void consume() throws InterruptedException {
        int n = rand.nextInt(buffer.capacity() / 2) + 1;
        Object[] elements = buffer.take(n);
        if (verbose) read(elements);

        compute();
    }

}

