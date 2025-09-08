package tbEnum.tbEnum_api;
import com.fasterxml.jackson.annotation.JsonProperty;


public enum Enum0
{
    @JsonProperty("0")
    Value0(0),
    @JsonProperty("1")
    Value1(1),
    @JsonProperty("2")
    Value2(2);

    private final int value;

    Enum0(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Enum0 fromValue(int value) {
        for (Enum0 e : values()) {
            if (e.value == value) return e;
        }
        throw new IllegalArgumentException("Unknown int value: " + value);
      }
}
