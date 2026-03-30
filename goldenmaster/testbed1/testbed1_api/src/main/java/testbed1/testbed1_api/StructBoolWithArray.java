package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructBoolWithArray {

    public StructBoolWithArray(List<Boolean> fieldBool)
    {
      this.fieldBool = fieldBool;
    }

    public StructBoolWithArray()
    {
        this.fieldBool = new ArrayList<>();
    }
    @JsonProperty("field_bool")
    public List<Boolean> fieldBool;

    public StructBoolWithArray(StructBoolWithArray other)
    {
        this.fieldBool = new ArrayList<>(other.fieldBool);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructBoolWithArray)) return false;
        StructBoolWithArray other = (StructBoolWithArray) o;

        return
         Objects.equals(this.fieldBool, other.fieldBool);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + fieldBool.hashCode();
        return result;
    }


}
