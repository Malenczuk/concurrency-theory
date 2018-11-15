package active_object;

public class Scheduler extends Thread{

    private final ActivationQueue activationQueue;
    private boolean verbose = false;

    Scheduler(ActivationQueue activationQueue, boolean verbose){
        this.activationQueue = activationQueue;
        this.verbose = verbose;
    }

    public void run(){
        try {
            while (!isInterrupted()) {
                MethodRequest current = activationQueue.dequeue();
                while (!current.guard()) {
                    MethodRequest opposite = activationQueue.dequeue(current.type.oposite());
                    opposite.execute();
                }
                current.execute();
            }
        } catch (InterruptedException e) {
            if(verbose)
                System.out.println("Scheduler stopped");
        }
    }
}
