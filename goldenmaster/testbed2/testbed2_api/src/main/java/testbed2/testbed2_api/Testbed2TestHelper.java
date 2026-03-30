package testbed2.testbed2_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        testStruct.field1 = Testbed2TestHelper.makeTestStruct1();
        return testStruct;
    }

    static public NestedStruct2 makeTestNestedStruct2()
    {
        NestedStruct2 testStruct = new NestedStruct2();
        testStruct.field1 = Testbed2TestHelper.makeTestStruct1();
        testStruct.field2 = Testbed2TestHelper.makeTestStruct2();
        return testStruct;
    }

    static public NestedStruct3 makeTestNestedStruct3()
    {
        NestedStruct3 testStruct = new NestedStruct3();
        testStruct.field1 = Testbed2TestHelper.makeTestStruct1();
        testStruct.field2 = Testbed2TestHelper.makeTestStruct2();
        testStruct.field3 = Testbed2TestHelper.makeTestStruct3();
        return testStruct;
    }

    static public IManyParamInterface makeTestManyParamInterface(IManyParamInterface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        testObjToFill.setProp1(1);
        testObjToFill.setProp2(1);
        testObjToFill.setProp3(1);
        testObjToFill.setProp4(1);
        return testObjToFill;
    }

    static public INestedStruct1Interface makeTestNestedStruct1Interface(INestedStruct1Interface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        NestedStruct1 localprop1 = Testbed2TestHelper.makeTestNestedStruct1();
        testObjToFill.setProp1(localprop1);
        return testObjToFill;
    }

    static public INestedStruct2Interface makeTestNestedStruct2Interface(INestedStruct2Interface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        NestedStruct1 localprop1 = Testbed2TestHelper.makeTestNestedStruct1();
        testObjToFill.setProp1(localprop1);
        NestedStruct2 localprop2 = Testbed2TestHelper.makeTestNestedStruct2();
        testObjToFill.setProp2(localprop2);
        return testObjToFill;
    }

    static public INestedStruct3Interface makeTestNestedStruct3Interface(INestedStruct3Interface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        NestedStruct1 localprop1 = Testbed2TestHelper.makeTestNestedStruct1();
        testObjToFill.setProp1(localprop1);
        NestedStruct2 localprop2 = Testbed2TestHelper.makeTestNestedStruct2();
        testObjToFill.setProp2(localprop2);
        NestedStruct3 localprop3 = Testbed2TestHelper.makeTestNestedStruct3();
        testObjToFill.setProp3(localprop3);
        return testObjToFill;
    }
}
