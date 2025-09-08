package testbed2.testbed2_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class Struct1 {

    public Struct1(int field1)
    {
      this.field1 = field1;
    }  

    public Struct1() 
    {
    }
    @JsonProperty("field1")
    public int field1;

    public Struct1(Struct1 other)
    {
        this.field1 = other.field1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Struct1)) return false;
        Struct1 other = (Struct1) o;

        return
         this.field1 == other.field1;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Integer.hashCode(field1);
        return result;
    }


}
