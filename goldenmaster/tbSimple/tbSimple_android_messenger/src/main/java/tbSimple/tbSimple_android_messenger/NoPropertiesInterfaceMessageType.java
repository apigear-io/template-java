package tbSimple.tbSimple_android_messenger;

public enum NoPropertiesInterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    SIG_SigVoid(3),
    SIG_SigBool(4),
    RPC_FuncVoidReq(5),
    RPC_FuncVoidResp(6),
    RPC_FuncBoolReq(7),
    RPC_FuncBoolResp(8),
    NoPropertiesInterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    NoPropertiesInterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static NoPropertiesInterfaceMessageType fromInteger(int value)
    {
        for (NoPropertiesInterfaceMessageType event : NoPropertiesInterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return NoPropertiesInterfaceMessageType_UNKNOWN;
    }
}
