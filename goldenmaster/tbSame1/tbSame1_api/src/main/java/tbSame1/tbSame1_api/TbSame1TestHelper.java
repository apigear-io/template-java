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

    static public ISameStruct1Interface makeTestSameStruct1Interface(ISameStruct1Interface testObjToFill) 
    {
        if (testObjToFill == null){return testObjToFill;}
        Struct1 localprop1 = TbSame1TestHelper.makeTestStruct1();
        testObjToFill.setProp1(localprop1);
        return testObjToFill;
    }

    static public ISameStruct2Interface makeTestSameStruct2Interface(ISameStruct2Interface testObjToFill) 
    {
        if (testObjToFill == null){return testObjToFill;}
        Struct2 localprop1 = TbSame1TestHelper.makeTestStruct2();
        testObjToFill.setProp1(localprop1);
        Struct2 localprop2 = TbSame1TestHelper.makeTestStruct2();
        testObjToFill.setProp2(localprop2);
        return testObjToFill;
    }

    static public ISameEnum1Interface makeTestSameEnum1Interface(ISameEnum1Interface testObjToFill) 
    {
        if (testObjToFill == null){return testObjToFill;}
        testObjToFill.setProp1(Enum1.Value2);
        return testObjToFill;
    }

    static public ISameEnum2Interface makeTestSameEnum2Interface(ISameEnum2Interface testObjToFill) 
    {
        if (testObjToFill == null){return testObjToFill;}
        testObjToFill.setProp1(Enum1.Value2);
        testObjToFill.setProp2(Enum2.Value2);
        return testObjToFill;
    }
}