package active_object;

public abstract class MethodRequest {

    RequestType type = RequestType.GENERAL;

    public abstract boolean guard();

    public abstract void execute();


}
