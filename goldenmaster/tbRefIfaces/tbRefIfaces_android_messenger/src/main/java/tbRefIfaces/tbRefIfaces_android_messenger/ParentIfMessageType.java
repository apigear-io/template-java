package tbRefIfaces.tbRefIfaces_android_messenger;

public enum ParentIfMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_LocalIf(3),
    SET_LocalIf(4),
    PROP_LocalIfList(5),
    SET_LocalIfList(6),
    PROP_ImportedIf(7),
    SET_ImportedIf(8),
    PROP_ImportedIfList(9),
    SET_ImportedIfList(10),
    SIG_LocalIfSignal(11),
    SIG_LocalIfSignalList(12),
    SIG_ImportedIfSignal(13),
    SIG_ImportedIfSignalList(14),
    RPC_LocalIfMethodReq(15),
    RPC_LocalIfMethodResp(16),
    RPC_LocalIfMethodListReq(17),
    RPC_LocalIfMethodListResp(18),
    RPC_ImportedIfMethodReq(19),
    RPC_ImportedIfMethodResp(20),
    RPC_ImportedIfMethodListReq(21),
    RPC_ImportedIfMethodListResp(22),
    ParentIfMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    ParentIfMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static ParentIfMessageType fromInteger(int value)
    {
        for (ParentIfMessageType event : ParentIfMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return ParentIfMessageType_UNKNOWN;
    }
}
