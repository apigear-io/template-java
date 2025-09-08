package testbed2.testbed2_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class NestedStruct2 {

    public NestedStruct2(Struct1 field1, Struct2 field2)
    {
      this.field1 = field1;
      this.field2 = field2;
    }  

    public NestedStruct2() 
    {
        this.field1 = new Struct1();
        this.field2 = new Struct2();
    }
    @JsonProperty("field1")
    public Struct1 field1;
    @JsonProperty("field2")
    public Struct2 field2;

    public NestedStruct2(NestedStruct2 other)
    {
        this.field1 = new Struct1(other.field1);
        this.field2 = new Struct2(other.field2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NestedStruct2)) return false;
        NestedStruct2 other = (NestedStruct2) o;

        return
         Objects.equals(this.field1, other.field1)
        && Objects.equals(this.field2, other.field2);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Objects.hashCode(field1);
        result = 31 * result + Objects.hashCode(field2);
        return result;
    }


}
