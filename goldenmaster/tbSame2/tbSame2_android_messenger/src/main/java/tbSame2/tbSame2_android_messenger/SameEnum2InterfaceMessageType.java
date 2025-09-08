package tbSame2.tbSame2_android_messenger;

public enum SameEnum2InterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_Prop1(3),
    SET_Prop1(4),
    PROP_Prop2(5),
    SET_Prop2(6),
    SIG_Sig1(7),
    SIG_Sig2(8),
    RPC_Func1Req(9),
    RPC_Func1Resp(10),
    RPC_Func2Req(11),
    RPC_Func2Resp(12),
    SameEnum2InterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    SameEnum2InterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static SameEnum2InterfaceMessageType fromInteger(int value)
    {
        for (SameEnum2InterfaceMessageType event : SameEnum2InterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return SameEnum2InterfaceMessageType_UNKNOWN;
    }
}
