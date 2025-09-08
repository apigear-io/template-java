package tbSimple.tbSimple_android_messenger;

public enum NoSignalsInterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_PropBool(3),
    SET_PropBool(4),
    PROP_PropInt(5),
    SET_PropInt(6),
    RPC_FuncVoidReq(7),
    RPC_FuncVoidResp(8),
    RPC_FuncBoolReq(9),
    RPC_FuncBoolResp(10),
    NoSignalsInterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    NoSignalsInterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static NoSignalsInterfaceMessageType fromInteger(int value)
    {
        for (NoSignalsInterfaceMessageType event : NoSignalsInterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return NoSignalsInterfaceMessageType_UNKNOWN;
    }
}
