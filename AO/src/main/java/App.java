import active_object.*;
import monitors.Buffer;
import util.Pair;
import util.Statistics;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class App {

    public static void main(String[] args) throws InterruptedException {
        int capacity = 1000;
        int pCount = 25;
        int cCount = 25;
        int testTime = 30 * 1000;
        int minComputeTime = 10 * 1000 * 1000;
        int maxComputeTime = 100 * 1000 * 1000;
        boolean verbose = false;

        Pair<double[], double[]> result;

        result = testActiveObject(capacity, pCount, cCount, testTime, minComputeTime, maxComputeTime, verbose);
        System.out.println(formatResult(result, capacity, pCount, cCount, testTime, minComputeTime, maxComputeTime, true));
        result = testSynchronized(capacity, pCount, cCount, testTime, minComputeTime, maxComputeTime, verbose);
        System.out.println(formatResult(result, capacity, pCount, cCount, testTime, minComputeTime, maxComputeTime, false));
    }

    private static Pair<double[], double[]> testActiveObject(int capacity, int pCount, int cCount, int testTime, int minComputeTime, int maxComputeTime, boolean verbose) throws InterruptedException {
        Proxy proxy = new Proxy(capacity, verbose);
        ProducerAO[] producers = new ProducerAO[pCount];
        ConsumerAO[] consumers = new ConsumerAO[cCount];

        for (int i = 0; i < pCount; i++)
            producers[i] = new ProducerAO(proxy, i, minComputeTime, maxComputeTime, verbose);

        for (int i = 0; i < cCount; i++)
            consumers[i] = new ConsumerAO(proxy, i, minComputeTime, maxComputeTime, verbose);

        Pair<double[], double[]> results = run(producers, consumers, testTime);
        proxy.stopScheduler();

        return results;

    }

    private static Pair<double[], double[]> testSynchronized(int capacity, int pCount, int cCount, int testTime, int minComputeTime, int maxComputeTime, boolean verbose) throws InterruptedException {
        Buffer buffer = new Buffer(capacity);
        ProducerS[] producers = new ProducerS[pCount];
        ConsumerS[] consumers = new ConsumerS[cCount];

        for (int i = 0; i < pCount; i++)
            producers[i] = new ProducerS(buffer, i, minComputeTime, maxComputeTime, verbose);

        for (int i = 0; i < cCount; i++)
            consumers[i] = new ConsumerS(buffer, i, minComputeTime, maxComputeTime, verbose);

        return run(producers, consumers, testTime);
    }

    private static Pair<double[], double[]> run(Producer[] producers, Consumer[] consumers, int testTime) throws InterruptedException {
        for (Producer producer : producers) producer.start();

        for (Consumer consumer : consumers) consumer.start();

        Thread.sleep(testTime);

        for (Producer producer : producers) producer.interrupt();

        for (Consumer consumer : consumers) consumer.interrupt();

        return Pair.of(join(producers), join(consumers));
    }

    private static double[] join(ITestObject[] testObjects) throws InterruptedException {
        double[] cycles = new double[testObjects.length];
        for (int i = 0; i < testObjects.length; i++) {
            testObjects[i].join();
            cycles[i] = testObjects[i].getCycles();
        }
        return cycles;
    }

    private static String formatResult(Pair<double[], double[]> result, int capacity, int pCount, int cCount, int testTime, int minComputeTime, int maxComputeTime, boolean active) {
        StringBuilder stringBuilder = new StringBuilder();
        Statistics pStatistics = new Statistics(result.first);
        Statistics cStatistics = new Statistics(result.second);
        Statistics statistics = new Statistics(DoubleStream.concat(Arrays.stream(result.first), Arrays.stream(result.second)).toArray());

        if (active)
            stringBuilder.append("Active Object Test\n");
        else
            stringBuilder.append("Synchronous Test\n");

        stringBuilder.append(String.format("BufferSize    = %12d   ProducersCount  = %12d   ConsumersCount = %12d\n", capacity, pCount, cCount));
        stringBuilder.append(String.format("TestTime      = %12ds  MinComputeTime  = %12dns MaxComputeTime = %12dns\n", testTime/1000, minComputeTime, maxComputeTime));
        stringBuilder.append(String.format("ProducersMean = %12.2f   ProducersStdDev = %12.2f\n", pStatistics.getMean(), pStatistics.getStdDev()));
        stringBuilder.append(String.format("ConsumersMean = %12.2f   ConsumersStdDev = %12.2f\n", cStatistics.getMean(), cStatistics.getStdDev()));
        stringBuilder.append(String.format("TotalMean     = %12.2f   TotalStdDev     = %12.2f\n", statistics.getMean(), statistics.getStdDev()));
        return stringBuilder.toString();
    }

}
