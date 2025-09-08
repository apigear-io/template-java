package tbSimple.tbSimple_android_messenger;

public enum SimpleArrayInterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_PropBool(3),
    SET_PropBool(4),
    PROP_PropInt(5),
    SET_PropInt(6),
    PROP_PropInt32(7),
    SET_PropInt32(8),
    PROP_PropInt64(9),
    SET_PropInt64(10),
    PROP_PropFloat(11),
    SET_PropFloat(12),
    PROP_PropFloat32(13),
    SET_PropFloat32(14),
    PROP_PropFloat64(15),
    SET_PropFloat64(16),
    PROP_PropString(17),
    SET_PropString(18),
    PROP_PropReadOnlyString(19),
    SET_PropReadOnlyString(20),
    SIG_SigBool(21),
    SIG_SigInt(22),
    SIG_SigInt32(23),
    SIG_SigInt64(24),
    SIG_SigFloat(25),
    SIG_SigFloat32(26),
    SIG_SigFloat64(27),
    SIG_SigString(28),
    RPC_FuncBoolReq(29),
    RPC_FuncBoolResp(30),
    RPC_FuncIntReq(31),
    RPC_FuncIntResp(32),
    RPC_FuncInt32Req(33),
    RPC_FuncInt32Resp(34),
    RPC_FuncInt64Req(35),
    RPC_FuncInt64Resp(36),
    RPC_FuncFloatReq(37),
    RPC_FuncFloatResp(38),
    RPC_FuncFloat32Req(39),
    RPC_FuncFloat32Resp(40),
    RPC_FuncFloat64Req(41),
    RPC_FuncFloat64Resp(42),
    RPC_FuncStringReq(43),
    RPC_FuncStringResp(44),
    SimpleArrayInterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    SimpleArrayInterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static SimpleArrayInterfaceMessageType fromInteger(int value)
    {
        for (SimpleArrayInterfaceMessageType event : SimpleArrayInterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return SimpleArrayInterfaceMessageType_UNKNOWN;
    }
}
