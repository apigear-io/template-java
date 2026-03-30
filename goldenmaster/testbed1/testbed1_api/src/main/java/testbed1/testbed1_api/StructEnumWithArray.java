package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructEnumWithArray {

    public StructEnumWithArray(List<Enum0> fieldEnum)
    {
      this.fieldEnum = fieldEnum;
    }

    public StructEnumWithArray()
    {
        this.fieldEnum = new ArrayList<>();
    }
    @JsonProperty("field_enum")
    public List<Enum0> fieldEnum;

    public StructEnumWithArray(StructEnumWithArray other)
    {
        this.fieldEnum = new ArrayList<>(other.fieldEnum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructEnumWithArray)) return false;
        StructEnumWithArray other = (StructEnumWithArray) o;

        return
         Objects.equals(this.fieldEnum, other.fieldEnum);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + fieldEnum.hashCode();
        return result;
    }


}
