package active_object;

public enum RequestType {
    GENERAL, PRODUCE, CONSUME;

    public RequestType oposite() {
        switch (this) {
            case PRODUCE:
                return CONSUME;
            case CONSUME:
                return PRODUCE;
            default:
                return GENERAL;
        }
    }
}
