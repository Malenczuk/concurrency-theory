package active_object;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ActivationQueue {
    private ReentrantLock lock = new ReentrantLock(true);
    private Condition wait = lock.newCondition();
    private MultiLinkedList<MethodRequest> multiLinkedList = new MultiLinkedList<>();

    ActivationQueue() {
    }

    void enqueue(MethodRequest methodRequest) throws InterruptedException {
        lock.lockInterruptibly();

        multiLinkedList.add(methodRequest, methodRequest.type.ordinal());
        wait.signal();
        lock.unlock();
    }

    MethodRequest dequeue() throws InterruptedException {
        return dequeue(RequestType.GENERAL);
    }

    MethodRequest dequeue(RequestType type) throws InterruptedException {
        lock.lockInterruptibly();

        while(multiLinkedList.size(type.ordinal()) == 0){
            wait.await();
        }
        MethodRequest methodRequest = multiLinkedList.poll(type.ordinal());
        lock.unlock();
        return methodRequest;
    }
}
