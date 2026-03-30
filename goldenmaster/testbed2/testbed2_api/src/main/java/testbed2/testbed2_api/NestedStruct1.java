package testbed2.testbed2_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class NestedStruct1 {

    public NestedStruct1(Struct1 field1)
    {
      this.field1 = field1;
    }

    public NestedStruct1()
    {
        this.field1 = new Struct1();
    }
    @JsonProperty("field1")
    public Struct1 field1;

    public NestedStruct1(NestedStruct1 other)
    {
        this.field1 = other.field1 != null
            ? new Struct1(other.field1)
            : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NestedStruct1)) return false;
        NestedStruct1 other = (NestedStruct1) o;

        return
         Objects.equals(this.field1, other.field1);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Objects.hashCode(field1);
        return result;
    }


}
