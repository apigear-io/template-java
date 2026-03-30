package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class StructEnum {

    public StructEnum(Enum0 fieldEnum)
    {
      this.fieldEnum = fieldEnum;
    }

    public StructEnum()
    {
        this.fieldEnum = Enum0.values()[0];
    }
    @JsonProperty("field_enum")
    public Enum0 fieldEnum;

    public StructEnum(StructEnum other)
    {
        this.fieldEnum = other.fieldEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructEnum)) return false;
        StructEnum other = (StructEnum) o;

        return
         this.fieldEnum == other.fieldEnum;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Objects.hashCode(fieldEnum);
        return result;
    }


}
