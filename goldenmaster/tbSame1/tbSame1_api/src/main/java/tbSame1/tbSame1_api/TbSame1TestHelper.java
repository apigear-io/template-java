package tbSame1.tbSame1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;


public class TbSame1TestHelper
{

    static public Struct1 makeTestStruct1() 
    {
        Struct1 testStruct = new Struct1();
        testStruct.field1 = 1;
        testStruct.field2 = 1;
        testStruct.field3 = 1;
        return testStruct;
    }

    static public Struct2 makeTestStruct2() 
    {
        Struct2 testStruct = new Struct2();
        testStruct.field1 = 1;
        testStruct.field2 = 1;
        testStruct.field3 = 1;
        return testStruct;
    }

}