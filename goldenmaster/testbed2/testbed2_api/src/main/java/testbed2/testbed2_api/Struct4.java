package testbed2.testbed2_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public  class Struct4 {

    public Struct4(int field1, int field2, int field3, int field4)
    {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
      this.field4 = field4;
    }

    public Struct4()
    {
    }
    @JsonProperty("field1")
    public int field1;
    @JsonProperty("field2")
    public int field2;
    @JsonProperty("field3")
    public int field3;
    @JsonProperty("field4")
    public int field4;

    public Struct4(Struct4 other)
    {
        this.field1 = other.field1;
        this.field2 = other.field2;
        this.field3 = other.field3;
        this.field4 = other.field4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Struct4)) return false;
        Struct4 other = (Struct4) o;

        return
         this.field1 == other.field1
        && this.field2 == other.field2
        && this.field3 == other.field3
        && this.field4 == other.field4;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Integer.hashCode(field1);
        result = 31 * result + Integer.hashCode(field2);
        result = 31 * result + Integer.hashCode(field3);
        result = 31 * result + Integer.hashCode(field4);
        return result;
    }


}
