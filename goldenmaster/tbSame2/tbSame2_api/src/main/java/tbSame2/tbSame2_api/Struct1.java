package tbSame2.tbSame2_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class Struct1 {

    public Struct1(int field1, int field2, int field3)
    {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }  

    public Struct1() 
    {
    }
    @JsonProperty("field1")
    public int field1;
    @JsonProperty("field2")
    public int field2;
    @JsonProperty("field3")
    public int field3;

    public Struct1(Struct1 other)
    {
        this.field1 = other.field1;
        this.field2 = other.field2;
        this.field3 = other.field3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Struct1)) return false;
        Struct1 other = (Struct1) o;

        return
         this.field1 == other.field1
        && this.field2 == other.field2
        && this.field3 == other.field3;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Integer.hashCode(field1);
        result = 31 * result + Integer.hashCode(field2);
        result = 31 * result + Integer.hashCode(field3);
        return result;
    }


}
