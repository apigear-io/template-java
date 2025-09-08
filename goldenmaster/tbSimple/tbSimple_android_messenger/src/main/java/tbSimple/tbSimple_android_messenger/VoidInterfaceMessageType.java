package tbSimple.tbSimple_android_messenger;

public enum VoidInterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    SIG_SigVoid(3),
    RPC_FuncVoidReq(4),
    RPC_FuncVoidResp(5),
    VoidInterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    VoidInterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static VoidInterfaceMessageType fromInteger(int value)
    {
        for (VoidInterfaceMessageType event : VoidInterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return VoidInterfaceMessageType_UNKNOWN;
    }
}
