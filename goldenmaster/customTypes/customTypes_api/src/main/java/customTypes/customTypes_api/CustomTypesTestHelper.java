package customTypes.customTypes_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomTypesTestHelper
{

    static public Vector3D makeTestVector3D()
    {
        Vector3D testStruct = new Vector3D();
        testStruct.x = 1.0f;
        testStruct.y = 1.0f;
        testStruct.z = 1.0f;
        return testStruct;
    }
}
