package active_object;

public class Future<E>{

    private E result;
    private boolean isDone = false;
//    private Condition condition = makeCondition(
//            new Assertion() {
//                @Override
//                public boolean isTrue() {
//                    return isDone;
//                }
//            }
//    );

    synchronized void setResult(E result) {
        this.result = result;
        this.isDone = true;
        notify();
    }

    synchronized public boolean isDone(){
        return isDone;
    }

    synchronized public E get() throws InterruptedException {
        while (!isDone)
            wait();
        return result;
    }

}
