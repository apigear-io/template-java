package testbed2.testbed2_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;


public class Testbed2TestHelper
{

    static public Struct1 makeTestStruct1() 
    {
        Struct1 testStruct = new Struct1();
        testStruct.field1 = 1;
        return testStruct;
    }

    static public Struct2 makeTestStruct2() 
    {
        Struct2 testStruct = new Struct2();
        testStruct.field1 = 1;
        testStruct.field2 = 1;
        return testStruct;
    }

    static public Struct3 makeTestStruct3() 
    {
        Struct3 testStruct = new Struct3();
        testStruct.field1 = 1;
        testStruct.field2 = 1;
        testStruct.field3 = 1;
        return testStruct;
    }

    static public Struct4 makeTestStruct4() 
    {
        Struct4 testStruct = new Struct4();
        testStruct.field1 = 1;
        testStruct.field2 = 1;
        testStruct.field3 = 1;
        testStruct.field4 = 1;
        return testStruct;
    }

    static public NestedStruct1 makeTestNestedStruct1() 
    {
        NestedStruct1 testStruct = new NestedStruct1();
        testStruct.field1 = makeTestStruct1();
        return testStruct;
    }

    static public NestedStruct2 makeTestNestedStruct2() 
    {
        NestedStruct2 testStruct = new NestedStruct2();
        testStruct.field1 = makeTestStruct1();
        testStruct.field2 = makeTestStruct2();
        return testStruct;
    }

    static public NestedStruct3 makeTestNestedStruct3() 
    {
        NestedStruct3 testStruct = new NestedStruct3();
        testStruct.field1 = Enum1.Value2;
        testStruct.field2 = makeTestStruct2();
        testStruct.field3 = makeTestStruct3();
        testStruct.fieldX = new Enum1[1];
	    testStruct.fieldX[0] = Enum1.Value2;
        testStruct.fieldY = new Struct2[1];
	    testStruct.fieldY[0] = makeTestStruct2();
        testStruct.fieldZ = new Struct3[1];
	    testStruct.fieldZ[0] = makeTestStruct3();
        return testStruct;
    }

}