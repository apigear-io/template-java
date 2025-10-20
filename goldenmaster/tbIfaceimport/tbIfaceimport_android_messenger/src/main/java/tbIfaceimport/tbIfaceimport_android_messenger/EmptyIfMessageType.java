package tbIfaceimport.tbIfaceimport_android_messenger;

public enum EmptyIfMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    EmptyIfMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    EmptyIfMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static EmptyIfMessageType fromInteger(int value)
    {
        for (EmptyIfMessageType event : EmptyIfMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return EmptyIfMessageType_UNKNOWN;
    }
}
