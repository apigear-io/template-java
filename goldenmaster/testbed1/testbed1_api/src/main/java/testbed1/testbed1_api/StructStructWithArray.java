package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class StructStructWithArray {

    public StructStructWithArray(StructStringWithArray[] fieldStruct)
    {
      this.fieldStruct = fieldStruct;
    }  

    public StructStructWithArray() 
    {
        this.fieldStruct = new StructStringWithArray[0];
    }
    @JsonProperty("field_struct")
    public StructStringWithArray[] fieldStruct;

    public StructStructWithArray(StructStructWithArray other)
    {
        this.fieldStruct = new StructStringWithArray[other.fieldStruct.length];
        for (int i = 0; i < other.fieldStruct.length; i++)
        {
            this.fieldStruct[i] = new StructStringWithArray(other.fieldStruct[i]);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructStructWithArray)) return false;
        StructStructWithArray other = (StructStructWithArray) o;

        return
         Arrays.equals(this.fieldStruct, other.fieldStruct);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Arrays.hashCode(fieldStruct);
        return result;
    }


}
