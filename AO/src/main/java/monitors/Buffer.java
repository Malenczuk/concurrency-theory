package monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private ReentrantLock lock = new ReentrantLock(true);
    private Condition firstProd = lock.newCondition();
    private Condition restProd = lock.newCondition();
    private Condition firstCons = lock.newCondition();
    private Condition restCons = lock.newCondition();

    private final RingBuffer buffer;
    private boolean hasWaitingFirstProd = false;
    private boolean hasWaitingFirstCons = false;
    private boolean sigRestProd = false;
    private boolean sigRestCons = false;

    public Buffer(int N) {
        this.buffer = new RingBuffer(N);
    }

    public int capacity(){
        return buffer.capacity();
    }

    public void put(Object[] portions) throws InterruptedException {
        lock.lock();
        try {
            while (hasWaitingFirstProd || sigRestProd) {
                restProd.await();
                sigRestProd = false;
            }
            while (hasWaitingFirstProd = (buffer.remainingCapacity() < portions.length))
                firstProd.await();

            buffer.put(portions);

            sigRestProd = lock.hasWaiters(restProd);
            restProd.signal();
            firstCons.signal();

        } finally {
            lock.unlock();
        }
    }

    public Object[] take(int n) throws InterruptedException {
        lock.lock();
        Object[] portions;
        try {
            while (hasWaitingFirstCons || sigRestCons) {
                restCons.await();
                sigRestCons = false;
            }
            while (hasWaitingFirstCons = (buffer.available() < n))
                firstCons.await();

            portions = buffer.take(n);

            sigRestCons = lock.hasWaiters(restCons);
            restCons.signal();
            firstProd.signal();
        } finally {
            lock.unlock();
        }
        return portions;
    }
}
