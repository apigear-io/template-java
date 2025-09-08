package testbed2.testbed2_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class NestedStruct3 {

    public NestedStruct3(Enum1 field1, Struct2 field2, Struct3 field3, Enum1[] fieldX, Struct2[] fieldY, Struct3[] fieldZ)
    {
      this.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
      this.fieldX = fieldX;
      this.fieldY = fieldY;
      this.fieldZ = fieldZ;
    }  

    public NestedStruct3() 
    {
        this.field1 = Enum1.values()[0];
        this.field2 = new Struct2();
        this.field3 = new Struct3();
        this.fieldX = new Enum1[0];
        this.fieldY = new Struct2[0];
        this.fieldZ = new Struct3[0];
    }
    @JsonProperty("field1")
    public Enum1 field1;
    @JsonProperty("field2")
    public Struct2 field2;
    @JsonProperty("field3")
    public Struct3 field3;
    @JsonProperty("field_x")
    public Enum1[] fieldX;
    @JsonProperty("field_y")
    public Struct2[] fieldY;
    @JsonProperty("field_z")
    public Struct3[] fieldZ;

    public NestedStruct3(NestedStruct3 other)
    {
        this.field1 = other.field1;
        this.field2 = new Struct2(other.field2);
        this.field3 = new Struct3(other.field3);
        this.fieldX = java.util.Arrays.copyOf(other.fieldX, other.fieldX.length);
        this.fieldY = new Struct2[other.fieldY.length];
        for (int i = 0; i < other.fieldY.length; i++)
        {
            this.fieldY[i] = new Struct2(other.fieldY[i]);
        }
        this.fieldZ = new Struct3[other.fieldZ.length];
        for (int i = 0; i < other.fieldZ.length; i++)
        {
            this.fieldZ[i] = new Struct3(other.fieldZ[i]);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NestedStruct3)) return false;
        NestedStruct3 other = (NestedStruct3) o;

        return
         this.field1 == other.field1
        && Objects.equals(this.field2, other.field2)
        && Objects.equals(this.field3, other.field3)
        && Arrays.equals(this.fieldX, other.fieldX)
        && Arrays.equals(this.fieldY, other.fieldY)
        && Arrays.equals(this.fieldZ, other.fieldZ);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Objects.hashCode(field1);
        result = 31 * result + Objects.hashCode(field2);
        result = 31 * result + Objects.hashCode(field3);
        result = 31 * result + Arrays.hashCode(fieldX);
        result = 31 * result + Arrays.hashCode(fieldY);
        result = 31 * result + Arrays.hashCode(fieldZ);
        return result;
    }


}
