package tbNames.tbNames_api;
import com.fasterxml.jackson.annotation.JsonProperty;


public enum EnumWithUnderScores
{
    @JsonProperty("0")
    FirstValue(0),
    @JsonProperty("1")
    SecondValue(1),
    @JsonProperty("2")
    ThirdValue(2);

    private final int value;

    EnumWithUnderScores(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EnumWithUnderScores fromValue(int value) {
        for (EnumWithUnderScores e : values()) {
            if (e.value == value) return e;
        }
        throw new IllegalArgumentException("Unknown int value: " + value);
      }
}
