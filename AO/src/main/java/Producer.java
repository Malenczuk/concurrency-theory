import java.util.Random;

public abstract class Producer extends Thread implements ITestObject{
    int minComputeTime = 100 * 1000;
    int maxComputeTime = 200 * 1000;
    Random rand = new Random(System.nanoTime());
    int nr;
    boolean verbose = false;
    int cycles = 0;

    public void run() {
        try {
            while (!isInterrupted()) {
                produce();
                cycles++;
            }
        } catch (InterruptedException e) {
            if (verbose)
                System.out.format("Producer[%d] Stopped after producing %d times\n", nr, cycles);
        }
    }

    abstract void produce() throws InterruptedException;

    void compute() {
        long computed = 0;
        long computeTime = rand.nextInt(maxComputeTime - minComputeTime + 1) + minComputeTime;
        long start = System.nanoTime();
        while ((System.nanoTime() - start) < computeTime)
            computed++;
    }

    public int getCycles() {
        return cycles;
    }
}
