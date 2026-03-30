package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructString {

    public StructString(String fieldString)
    {
      this.fieldString = fieldString;
    }

    public StructString()
    {
    }
    @JsonProperty("field_string")
    public String fieldString;

    public StructString(StructString other)
    {
        this.fieldString = other.fieldString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructString)) return false;
        StructString other = (StructString) o;

        return
         this.fieldString == other.fieldString;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + (fieldString != null ? fieldString.hashCode() : 0);
        return result;
    }


}
