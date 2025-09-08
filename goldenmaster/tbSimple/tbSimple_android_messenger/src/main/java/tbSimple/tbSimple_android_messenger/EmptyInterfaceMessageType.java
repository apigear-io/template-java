package tbSimple.tbSimple_android_messenger;

public enum EmptyInterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    EmptyInterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    EmptyInterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static EmptyInterfaceMessageType fromInteger(int value)
    {
        for (EmptyInterfaceMessageType event : EmptyInterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return EmptyInterfaceMessageType_UNKNOWN;
    }
}
