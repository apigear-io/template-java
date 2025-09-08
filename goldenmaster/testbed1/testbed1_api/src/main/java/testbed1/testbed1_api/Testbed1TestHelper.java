package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;


public class Testbed1TestHelper
{

    static public StructBool makeTestStructBool() 
    {
        StructBool testStruct = new StructBool();
        testStruct.fieldBool = true;
        return testStruct;
    }

    static public StructInt makeTestStructInt() 
    {
        StructInt testStruct = new StructInt();
        testStruct.fieldInt = 1;
        return testStruct;
    }

    static public StructFloat makeTestStructFloat() 
    {
        StructFloat testStruct = new StructFloat();
        testStruct.fieldFloat = 1.0f;
        return testStruct;
    }

    static public StructString makeTestStructString() 
    {
        StructString testStruct = new StructString();
        testStruct.fieldString = new String("xyz");
        return testStruct;
    }

}