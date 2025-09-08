package tbEnum.tbEnum_android_messenger;

public enum EnumInterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_Prop0(3),
    SET_Prop0(4),
    PROP_Prop1(5),
    SET_Prop1(6),
    PROP_Prop2(7),
    SET_Prop2(8),
    PROP_Prop3(9),
    SET_Prop3(10),
    SIG_Sig0(11),
    SIG_Sig1(12),
    SIG_Sig2(13),
    SIG_Sig3(14),
    RPC_Func0Req(15),
    RPC_Func0Resp(16),
    RPC_Func1Req(17),
    RPC_Func1Resp(18),
    RPC_Func2Req(19),
    RPC_Func2Resp(20),
    RPC_Func3Req(21),
    RPC_Func3Resp(22),
    EnumInterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    EnumInterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static EnumInterfaceMessageType fromInteger(int value)
    {
        for (EnumInterfaceMessageType event : EnumInterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return EnumInterfaceMessageType_UNKNOWN;
    }
}
