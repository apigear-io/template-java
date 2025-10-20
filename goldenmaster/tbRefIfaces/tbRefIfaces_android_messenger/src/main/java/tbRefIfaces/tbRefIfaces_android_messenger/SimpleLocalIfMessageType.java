package tbRefIfaces.tbRefIfaces_android_messenger;

public enum SimpleLocalIfMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_IntProperty(3),
    SET_IntProperty(4),
    SIG_IntSignal(5),
    RPC_IntMethodReq(6),
    RPC_IntMethodResp(7),
    SimpleLocalIfMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    SimpleLocalIfMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static SimpleLocalIfMessageType fromInteger(int value)
    {
        for (SimpleLocalIfMessageType event : SimpleLocalIfMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return SimpleLocalIfMessageType_UNKNOWN;
    }
}
