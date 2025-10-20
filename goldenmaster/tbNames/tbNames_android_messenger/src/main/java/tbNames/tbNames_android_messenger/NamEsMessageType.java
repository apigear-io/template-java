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
    PROP_EnumProperty(9),
    SET_EnumProperty(10),
    SIG_SomeSignal(11),
    SIG_SomeSignal2(12),
    RPC_SomeFunctionReq(13),
    RPC_SomeFunctionResp(14),
    RPC_SomeFunction2Req(15),
    RPC_SomeFunction2Resp(16),
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
