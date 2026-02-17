package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class StructFloatWithArray {

    public StructFloatWithArray(float[] fieldFloat)
    {
      this.fieldFloat = fieldFloat;
    }  

    public StructFloatWithArray() 
    {
        this.fieldFloat = new float[0];
    }
    @JsonProperty("field_float")
    public float[] fieldFloat;

    public StructFloatWithArray(StructFloatWithArray other)
    {
        this.fieldFloat = other.fieldFloat != null
            ? java.util.Arrays.copyOf(other.fieldFloat, other.fieldFloat.length)
            : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructFloatWithArray)) return false;
        StructFloatWithArray other = (StructFloatWithArray) o;

        return
         Arrays.equals(this.fieldFloat, other.fieldFloat);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Arrays.hashCode(fieldFloat);
        return result;
    }


}
