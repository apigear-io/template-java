package testbed2.testbed2_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class Struct2 {

    public Struct2(int field1, int field2)
    {
      this.field1 = field1;
      this.field2 = field2;
    }  

    public Struct2() 
    {
    }
    @JsonProperty("field1")
    public int field1;
    @JsonProperty("field2")
    public int field2;

    public Struct2(Struct2 other)
    {
        this.field1 = other.field1;
        this.field2 = other.field2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Struct2)) return false;
        Struct2 other = (Struct2) o;

        return
         this.field1 == other.field1
        && this.field2 == other.field2;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Integer.hashCode(field1);
        result = 31 * result + Integer.hashCode(field2);
        return result;
    }


}
