package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class StructIntWithArray {

    public StructIntWithArray(int[] fieldInt)
    {
      this.fieldInt = fieldInt;
    }  

    public StructIntWithArray() 
    {
        this.fieldInt = new int[0];
    }
    @JsonProperty("field_int")
    public int[] fieldInt;

    public StructIntWithArray(StructIntWithArray other)
    {
        this.fieldInt = other.fieldInt != null
            ? java.util.Arrays.copyOf(other.fieldInt, other.fieldInt.length)
            : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructIntWithArray)) return false;
        StructIntWithArray other = (StructIntWithArray) o;

        return
         Arrays.equals(this.fieldInt, other.fieldInt);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Arrays.hashCode(fieldInt);
        return result;
    }


}
