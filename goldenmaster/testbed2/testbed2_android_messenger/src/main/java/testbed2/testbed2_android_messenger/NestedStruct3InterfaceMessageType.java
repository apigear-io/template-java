package testbed2.testbed2_android_messenger;

public enum NestedStruct3InterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_Prop1(3),
    SET_Prop1(4),
    PROP_Prop2(5),
    SET_Prop2(6),
    PROP_Prop3(7),
    SET_Prop3(8),
    SIG_Sig1(9),
    SIG_Sig2(10),
    SIG_Sig3(11),
    RPC_Func1Req(12),
    RPC_Func1Resp(13),
    RPC_Func2Req(14),
    RPC_Func2Resp(15),
    RPC_Func3Req(16),
    RPC_Func3Resp(17),
    NestedStruct3InterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    NestedStruct3InterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static NestedStruct3InterfaceMessageType fromInteger(int value)
    {
        for (NestedStruct3InterfaceMessageType event : NestedStruct3InterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return NestedStruct3InterfaceMessageType_UNKNOWN;
    }
}
