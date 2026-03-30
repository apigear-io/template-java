package testbed2.testbed2_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class NestedStruct3 {

    public NestedStruct3(Struct1 field1, Struct2 field2, Struct3 field3)
    {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }

    public NestedStruct3()
    {
        this.field1 = new Struct1();
        this.field2 = new Struct2();
        this.field3 = new Struct3();
    }
    @JsonProperty("field1")
    public Struct1 field1;
    @JsonProperty("field2")
    public Struct2 field2;
    @JsonProperty("field3")
    public Struct3 field3;

    public NestedStruct3(NestedStruct3 other)
    {
        this.field1 = other.field1 != null
            ? new Struct1(other.field1)
            : null;
        this.field2 = other.field2 != null
            ? new Struct2(other.field2)
            : null;
        this.field3 = other.field3 != null
            ? new Struct3(other.field3)
            : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NestedStruct3)) return false;
        NestedStruct3 other = (NestedStruct3) o;

        return
         Objects.equals(this.field1, other.field1)
        && Objects.equals(this.field2, other.field2)
        && Objects.equals(this.field3, other.field3);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Objects.hashCode(field1);
        result = 31 * result + Objects.hashCode(field2);
        result = 31 * result + Objects.hashCode(field3);
        return result;
    }


}
