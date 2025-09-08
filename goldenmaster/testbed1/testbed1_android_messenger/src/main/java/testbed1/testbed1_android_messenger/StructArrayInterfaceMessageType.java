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
    SIG_SigBool(11),
    SIG_SigInt(12),
    SIG_SigFloat(13),
    SIG_SigString(14),
    RPC_FuncBoolReq(15),
    RPC_FuncBoolResp(16),
    RPC_FuncIntReq(17),
    RPC_FuncIntResp(18),
    RPC_FuncFloatReq(19),
    RPC_FuncFloatResp(20),
    RPC_FuncStringReq(21),
    RPC_FuncStringResp(22),
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
