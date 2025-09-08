package testbed2.testbed2_api;
import com.fasterxml.jackson.annotation.JsonProperty;


public enum Enum1
{
    @JsonProperty("1")
    Value1(1),
    @JsonProperty("2")
    Value2(2),
    @JsonProperty("3")
    Value3(3),
    @JsonProperty("4")
    Value4(4);

    private final int value;

    Enum1(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Enum1 fromValue(int value) {
        for (Enum1 e : values()) {
            if (e.value == value) return e;
        }
        throw new IllegalArgumentException("Unknown int value: " + value);
      }
}
