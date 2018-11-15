import active_object.Future;
import active_object.Proxy;

public class ConsumerAO extends Consumer {

    private final Proxy proxy;

    public ConsumerAO(Proxy proxy, int nr) {
        this.proxy = proxy;
        this.nr = nr;
    }

    public ConsumerAO(Proxy proxy, int nr, boolean verbose) {
        this.proxy = proxy;
        this.nr = nr;
        this.verbose = verbose;
    }

    public ConsumerAO(Proxy proxy, int nr, int minComputeTime, int maxComputeTime) {
        this.proxy = proxy;
        this.nr = nr;
        this.minComputeTime = minComputeTime;
        this.maxComputeTime = maxComputeTime;
    }

    public ConsumerAO(Proxy proxy, int nr, int minComputeTime, int maxComputeTime, boolean verbose) {
        this.proxy = proxy;
        this.nr = nr;
        this.minComputeTime = minComputeTime;
        this.maxComputeTime = maxComputeTime;
        this.verbose = verbose;
    }

    void consume() throws InterruptedException {
        int n = rand.nextInt(proxy.capacity() / 2) + 1;
        Future<Object[]> future = proxy.take(n);

        compute();

        Object[] elements = future.get();
        if (verbose) read(elements);
    }
}
