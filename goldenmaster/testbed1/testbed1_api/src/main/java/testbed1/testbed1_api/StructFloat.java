package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructFloat {

    public StructFloat(float fieldFloat)
    {
      this.fieldFloat = fieldFloat;
    }

    public StructFloat()
    {
    }
    @JsonProperty("field_float")
    public float fieldFloat;

    public StructFloat(StructFloat other)
    {
        this.fieldFloat = other.fieldFloat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructFloat)) return false;
        StructFloat other = (StructFloat) o;

        return
         this.fieldFloat == other.fieldFloat;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Float.hashCode(fieldFloat);
        return result;
    }


}
