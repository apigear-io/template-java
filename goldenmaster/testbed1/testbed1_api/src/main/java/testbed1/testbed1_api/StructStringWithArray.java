package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class StructStringWithArray {

    public StructStringWithArray(String[] fieldString)
    {
      this.fieldString = fieldString;
    }  

    public StructStringWithArray() 
    {
        this.fieldString = new String[0];
    }
    @JsonProperty("field_string")
    public String[] fieldString;

    public StructStringWithArray(StructStringWithArray other)
    {
        this.fieldString = other.fieldString != null
            ? java.util.Arrays.copyOf(other.fieldString, other.fieldString.length)
            : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructStringWithArray)) return false;
        StructStringWithArray other = (StructStringWithArray) o;

        return
         Arrays.equals(this.fieldString, other.fieldString);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Arrays.hashCode(fieldString);
        return result;
    }


}
