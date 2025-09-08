package tbSimple.tbSimple_android_messenger;

public enum NoOperationsInterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_PropBool(3),
    SET_PropBool(4),
    PROP_PropInt(5),
    SET_PropInt(6),
    SIG_SigVoid(7),
    SIG_SigBool(8),
    NoOperationsInterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    NoOperationsInterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static NoOperationsInterfaceMessageType fromInteger(int value)
    {
        for (NoOperationsInterfaceMessageType event : NoOperationsInterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return NoOperationsInterfaceMessageType_UNKNOWN;
    }
}
