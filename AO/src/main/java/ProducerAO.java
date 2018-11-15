import active_object.Future;
import active_object.Proxy;

import java.util.Random;

public class ProducerAO extends Producer {

    private final Proxy proxy;

    public ProducerAO(Proxy proxy, int nr) {
        this.proxy = proxy;
        this.nr = nr;
    }

    public ProducerAO(Proxy proxy, int nr, boolean verbose) {
        this.proxy = proxy;
        this.nr = nr;
        this.verbose = verbose;
    }

    public ProducerAO(Proxy proxy, int nr, int minComputeTime, int maxComputeTime) {
        this.proxy = proxy;
        this.nr = nr;
        this.minComputeTime = minComputeTime;
        this.maxComputeTime = maxComputeTime;
    }

    public ProducerAO(Proxy proxy, int nr, int minComputeTime, int maxComputeTime, boolean verbose) {
        this.proxy = proxy;
        this.nr = nr;
        this.minComputeTime = minComputeTime;
        this.maxComputeTime = maxComputeTime;
        this.verbose = verbose;
    }

    void produce() throws InterruptedException {
        Object[] elements = create();
        Future<Boolean> future = proxy.put(elements);

        compute();

        future.get();
    }

    private Object[] create() {
        int n = rand.nextInt(proxy.capacity() / 2) + 1;
        Object[] elements = new Object[n];
        for (int i = 0; i < n; i++) {
            elements[i] = String.format("[%d] (%d) %d", nr, cycles, i);
        }
        return elements;
    }

}


