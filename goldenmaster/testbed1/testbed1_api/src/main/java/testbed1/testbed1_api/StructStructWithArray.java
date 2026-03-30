package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructStructWithArray {

    public StructStructWithArray(List<StructStringWithArray> fieldStruct)
    {
      this.fieldStruct = fieldStruct;
    }

    public StructStructWithArray()
    {
        this.fieldStruct = new ArrayList<>();
    }
    @JsonProperty("field_struct")
    public List<StructStringWithArray> fieldStruct;

    public StructStructWithArray(StructStructWithArray other)
    {
        this.fieldStruct = other.fieldStruct.stream()
            .map(StructStringWithArray::new)
            .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructStructWithArray)) return false;
        StructStructWithArray other = (StructStructWithArray) o;

        return
         Objects.equals(this.fieldStruct, other.fieldStruct);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + fieldStruct.hashCode();
        return result;
    }


}
