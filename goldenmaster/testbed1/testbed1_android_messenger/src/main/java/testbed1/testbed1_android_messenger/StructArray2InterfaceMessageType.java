package testbed1.testbed1_android_messenger;

public enum StructArray2InterfaceMessageType {
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
    RPC_FuncBoolReq(17),
    RPC_FuncBoolResp(18),
    RPC_FuncIntReq(19),
    RPC_FuncIntResp(20),
    RPC_FuncFloatReq(21),
    RPC_FuncFloatResp(22),
    RPC_FuncStringReq(23),
    RPC_FuncStringResp(24),
    RPC_FuncEnumReq(25),
    RPC_FuncEnumResp(26),
    StructArray2InterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    StructArray2InterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static StructArray2InterfaceMessageType fromInteger(int value)
    {
        for (StructArray2InterfaceMessageType event : StructArray2InterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return StructArray2InterfaceMessageType_UNKNOWN;
    }
}
