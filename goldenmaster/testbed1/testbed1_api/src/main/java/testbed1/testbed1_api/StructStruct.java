package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructStruct {

    public StructStruct(StructString fieldString)
    {
      this.fieldString = fieldString;
    }

    public StructStruct()
    {
        this.fieldString = new StructString();
    }
    @JsonProperty("field_string")
    public StructString fieldString;

    public StructStruct(StructStruct other)
    {
        this.fieldString = other.fieldString != null
            ? new StructString(other.fieldString)
            : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructStruct)) return false;
        StructStruct other = (StructStruct) o;

        return
         Objects.equals(this.fieldString, other.fieldString);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Objects.hashCode(fieldString);
        return result;
    }


}
