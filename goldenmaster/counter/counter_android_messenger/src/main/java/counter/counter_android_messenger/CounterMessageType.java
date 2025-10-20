package counter.counter_android_messenger;

public enum CounterMessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    PROP_Vector(3),
    SET_Vector(4),
    PROP_ExternVector(5),
    SET_ExternVector(6),
    PROP_VectorArray(7),
    SET_VectorArray(8),
    PROP_ExternVectorArray(9),
    SET_ExternVectorArray(10),
    SIG_ValueChanged(11),
    RPC_IncrementReq(12),
    RPC_IncrementResp(13),
    RPC_IncrementArrayReq(14),
    RPC_IncrementArrayResp(15),
    RPC_DecrementReq(16),
    RPC_DecrementResp(17),
    RPC_DecrementArrayReq(18),
    RPC_DecrementArrayResp(19),
    CounterMessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    CounterMessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static CounterMessageType fromInteger(int value)
    {
        for (CounterMessageType event : CounterMessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return CounterMessageType_UNKNOWN;
    }
}
