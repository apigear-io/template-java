package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class StructBoolWithArray {

    public StructBoolWithArray(boolean[] fieldBool)
    {
      this.fieldBool = fieldBool;
    }  

    public StructBoolWithArray() 
    {
        this.fieldBool = new boolean[0];
    }
    @JsonProperty("field_bool")
    public boolean[] fieldBool;

    public StructBoolWithArray(StructBoolWithArray other)
    {
        this.fieldBool = other.fieldBool != null
            ? java.util.Arrays.copyOf(other.fieldBool, other.fieldBool.length)
            : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructBoolWithArray)) return false;
        StructBoolWithArray other = (StructBoolWithArray) o;

        return
         Arrays.equals(this.fieldBool, other.fieldBool);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Arrays.hashCode(fieldBool);
        return result;
    }


}
