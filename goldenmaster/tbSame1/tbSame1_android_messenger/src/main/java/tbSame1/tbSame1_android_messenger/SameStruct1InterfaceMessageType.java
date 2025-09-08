package tbSame1.tbSame1_android_messenger;

public enum SameStruct1InterfaceMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_Prop1(3),
    SET_Prop1(4),
    SIG_Sig1(5),
    RPC_Func1Req(6),
    RPC_Func1Resp(7),
    SameStruct1InterfaceMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    SameStruct1InterfaceMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static SameStruct1InterfaceMessageType fromInteger(int value)
    {
        for (SameStruct1InterfaceMessageType event : SameStruct1InterfaceMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return SameStruct1InterfaceMessageType_UNKNOWN;
    }
}
