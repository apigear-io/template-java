package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructIntWithArray {

    public StructIntWithArray(List<Integer> fieldInt)
    {
      this.fieldInt = fieldInt;
    }

    public StructIntWithArray()
    {
        this.fieldInt = new ArrayList<>();
    }
    @JsonProperty("field_int")
    public List<Integer> fieldInt;

    public StructIntWithArray(StructIntWithArray other)
    {
        this.fieldInt = new ArrayList<>(other.fieldInt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructIntWithArray)) return false;
        StructIntWithArray other = (StructIntWithArray) o;

        return
         Objects.equals(this.fieldInt, other.fieldInt);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + fieldInt.hashCode();
        return result;
    }


}
