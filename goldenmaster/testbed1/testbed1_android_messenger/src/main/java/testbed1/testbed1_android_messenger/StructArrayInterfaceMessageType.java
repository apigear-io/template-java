package testbed1.testbed1_android_messenger;

public enum StructArrayInterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_PropBool(3),
    SET_PropBool(4),
    PROP_PropInt(5),
    SET_PropInt(6),
    PROP_PropFloat(7),
    SET_PropFloat(8),
    PROP_PropString(9),
    SET_PropString(10),
    PROP_PropEnum(11),
    SET_PropEnum(12),
    SIG_SigBool(13),
    SIG_SigInt(14),
    SIG_SigFloat(15),
    SIG_SigString(16),
    SIG_SigEnum(17),
    RPC_FuncBoolReq(18),
    RPC_FuncBoolResp(19),
    RPC_FuncIntReq(20),
    RPC_FuncIntResp(21),
    RPC_FuncFloatReq(22),
    RPC_FuncFloatResp(23),
    RPC_FuncStringReq(24),
    RPC_FuncStringResp(25),
    RPC_FuncEnumReq(26),
    RPC_FuncEnumResp(27),
    StructArrayInterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    StructArrayInterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static StructArrayInterfaceMessageType fromInteger(int value)
    {
        for (StructArrayInterfaceMessageType event : StructArrayInterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return StructArrayInterfaceMessageType_UNKNOWN;
    }
}
