package testbed2.testbed2_android_messenger;

public enum ManyParamInterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_Prop1(3),
    SET_Prop1(4),
    PROP_Prop2(5),
    SET_Prop2(6),
    PROP_Prop3(7),
    SET_Prop3(8),
    PROP_Prop4(9),
    SET_Prop4(10),
    SIG_Sig1(11),
    SIG_Sig2(12),
    SIG_Sig3(13),
    SIG_Sig4(14),
    RPC_Func1Req(15),
    RPC_Func1Resp(16),
    RPC_Func2Req(17),
    RPC_Func2Resp(18),
    RPC_Func3Req(19),
    RPC_Func3Resp(20),
    RPC_Func4Req(21),
    RPC_Func4Resp(22),
    ManyParamInterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    ManyParamInterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static ManyParamInterfaceMessageType fromInteger(int value)
    {
        for (ManyParamInterfaceMessageType event : ManyParamInterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return ManyParamInterfaceMessageType_UNKNOWN;
    }
}
