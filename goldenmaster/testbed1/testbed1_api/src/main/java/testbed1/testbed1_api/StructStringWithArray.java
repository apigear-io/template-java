package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructStringWithArray {

    public StructStringWithArray(List<String> fieldString)
    {
      this.fieldString = fieldString;
    }

    public StructStringWithArray()
    {
        this.fieldString = new ArrayList<>();
    }
    @JsonProperty("field_string")
    public List<String> fieldString;

    public StructStringWithArray(StructStringWithArray other)
    {
        this.fieldString = new ArrayList<>(other.fieldString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructStringWithArray)) return false;
        StructStringWithArray other = (StructStringWithArray) o;

        return
         Objects.equals(this.fieldString, other.fieldString);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + fieldString.hashCode();
        return result;
    }


}
