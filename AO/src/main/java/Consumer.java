import java.util.Random;

public abstract class Consumer extends Thread implements ITestObject {
    int minComputeTime = 100 * 1000;
    int maxComputeTime = 200 * 1000;
    Random rand = new Random(System.nanoTime());
    int nr;
    boolean verbose = false;
    int cycles = 0;

    public void run() {
        try {
            while (!isInterrupted()) {
                consume();
                cycles++;
            }
        } catch (InterruptedException e) {
            if (verbose)
                System.out.format("Consumer[%d] Stopped after consuming %d times\n", nr, cycles);
        }
    }

    abstract void consume() throws InterruptedException;

    void read(Object[] elements) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object element : elements) {
            stringBuilder.append(element).append(", ");
        }
        System.out.println(String.format("Consumer[%d] consumed : {%s}", nr, stringBuilder.toString()));
    }

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
