package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructInt {

    public StructInt(int fieldInt)
    {
      this.fieldInt = fieldInt;
    }

    public StructInt()
    {
    }
    @JsonProperty("field_int")
    public int fieldInt;

    public StructInt(StructInt other)
    {
        this.fieldInt = other.fieldInt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructInt)) return false;
        StructInt other = (StructInt) o;

        return
         this.fieldInt == other.fieldInt;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Integer.hashCode(fieldInt);
        return result;
    }


}
