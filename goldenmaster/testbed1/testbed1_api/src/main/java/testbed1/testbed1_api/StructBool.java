package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructBool {

    public StructBool(boolean fieldBool)
    {
      this.fieldBool = fieldBool;
    }

    public StructBool()
    {
    }
    @JsonProperty("field_bool")
    public boolean fieldBool;

    public StructBool(StructBool other)
    {
        this.fieldBool = other.fieldBool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructBool)) return false;
        StructBool other = (StructBool) o;

        return
         this.fieldBool == other.fieldBool;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Boolean.hashCode(fieldBool);
        return result;
    }


}
