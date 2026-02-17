package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class StructEnumWithArray {

    public StructEnumWithArray(Enum0[] fieldEnum)
    {
      this.fieldEnum = fieldEnum;
    }  

    public StructEnumWithArray() 
    {
        this.fieldEnum = new Enum0[0];
    }
    @JsonProperty("field_enum")
    public Enum0[] fieldEnum;

    public StructEnumWithArray(StructEnumWithArray other)
    {
        this.fieldEnum = other.fieldEnum != null
            ? java.util.Arrays.copyOf(other.fieldEnum, other.fieldEnum.length)
            : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructEnumWithArray)) return false;
        StructEnumWithArray other = (StructEnumWithArray) o;

        return
         Arrays.equals(this.fieldEnum, other.fieldEnum);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Arrays.hashCode(fieldEnum);
        return result;
    }


}
