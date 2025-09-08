package tbNames.tbNames_android_messenger;

public enum NamEsMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_Switch(3),
    SET_Switch(4),
    PROP_SomeProperty(5),
    SET_SomeProperty(6),
    PROP_SomePoperty2(7),
    SET_SomePoperty2(8),
    SIG_SomeSignal(9),
    SIG_SomeSignal2(10),
    RPC_SomeFunctionReq(11),
    RPC_SomeFunctionResp(12),
    RPC_SomeFunction2Req(13),
    RPC_SomeFunction2Resp(14),
    NamEsMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    NamEsMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static NamEsMessageType fromInteger(int value)
    {
        for (NamEsMessageType event : NamEsMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return NamEsMessageType_UNKNOWN;
    }
}
