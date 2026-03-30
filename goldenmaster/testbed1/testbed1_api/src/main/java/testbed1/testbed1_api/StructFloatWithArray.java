package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructFloatWithArray {

    public StructFloatWithArray(List<Float> fieldFloat)
    {
      this.fieldFloat = fieldFloat;
    }

    public StructFloatWithArray()
    {
        this.fieldFloat = new ArrayList<>();
    }
    @JsonProperty("field_float")
    public List<Float> fieldFloat;

    public StructFloatWithArray(StructFloatWithArray other)
    {
        this.fieldFloat = new ArrayList<>(other.fieldFloat);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructFloatWithArray)) return false;
        StructFloatWithArray other = (StructFloatWithArray) o;

        return
         Objects.equals(this.fieldFloat, other.fieldFloat);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + fieldFloat.hashCode();
        return result;
    }


}
