package customTypes.customTypes_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public  class Vector3D {

    public Vector3D(float x, float y, float z)
    {
      this.x = x;
      this.y = y;
      this.z = z;
    }  

    public Vector3D() 
    {
    }
    @JsonProperty("x")
    public float x;
    @JsonProperty("y")
    public float y;
    @JsonProperty("z")
    public float z;

    public Vector3D(Vector3D other)
    {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3D)) return false;
        Vector3D other = (Vector3D) o;

        return
         this.x == other.x
        && this.y == other.y
        && this.z == other.z;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + Float.hashCode(x);
        result = 31 * result + Float.hashCode(y);
        result = 31 * result + Float.hashCode(z);
        return result;
    }


}
