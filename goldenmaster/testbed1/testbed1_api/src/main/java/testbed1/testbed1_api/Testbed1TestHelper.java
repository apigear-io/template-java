package testbed1.testbed1_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    static public StructStruct makeTestStructStruct()
    {
        StructStruct testStruct = new StructStruct();
        testStruct.fieldString = Testbed1TestHelper.makeTestStructString();
        return testStruct;
    }

    static public StructEnum makeTestStructEnum()
    {
        StructEnum testStruct = new StructEnum();
        testStruct.fieldEnum = Enum0.Value1;
        return testStruct;
    }

    static public StructBoolWithArray makeTestStructBoolWithArray()
    {
        StructBoolWithArray testStruct = new StructBoolWithArray();
        testStruct.fieldBool = new ArrayList<>();
	    testStruct.fieldBool.add(true);
        return testStruct;
    }

    static public StructIntWithArray makeTestStructIntWithArray()
    {
        StructIntWithArray testStruct = new StructIntWithArray();
        testStruct.fieldInt = new ArrayList<>();
	    testStruct.fieldInt.add(1);
        return testStruct;
    }

    static public StructFloatWithArray makeTestStructFloatWithArray()
    {
        StructFloatWithArray testStruct = new StructFloatWithArray();
        testStruct.fieldFloat = new ArrayList<>();
	    testStruct.fieldFloat.add(1.0f);
        return testStruct;
    }

    static public StructStringWithArray makeTestStructStringWithArray()
    {
        StructStringWithArray testStruct = new StructStringWithArray();
        testStruct.fieldString = new ArrayList<>();
	    testStruct.fieldString.add(new String("xyz"));
        return testStruct;
    }

    static public StructStructWithArray makeTestStructStructWithArray()
    {
        StructStructWithArray testStruct = new StructStructWithArray();
        testStruct.fieldStruct = new ArrayList<>();
	    testStruct.fieldStruct.add(Testbed1TestHelper.makeTestStructStringWithArray());
        return testStruct;
    }

    static public StructEnumWithArray makeTestStructEnumWithArray()
    {
        StructEnumWithArray testStruct = new StructEnumWithArray();
        testStruct.fieldEnum = new ArrayList<>();
	    testStruct.fieldEnum.add(Enum0.Value1);
        return testStruct;
    }

    static public IStructInterface makeTestStructInterface(IStructInterface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        StructBool localpropBool = Testbed1TestHelper.makeTestStructBool();
        testObjToFill.setPropBool(localpropBool);
        StructInt localpropInt = Testbed1TestHelper.makeTestStructInt();
        testObjToFill.setPropInt(localpropInt);
        StructFloat localpropFloat = Testbed1TestHelper.makeTestStructFloat();
        testObjToFill.setPropFloat(localpropFloat);
        StructString localpropString = Testbed1TestHelper.makeTestStructString();
        testObjToFill.setPropString(localpropString);
        return testObjToFill;
    }

    static public IStructArrayInterface makeTestStructArrayInterface(IStructArrayInterface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        List<StructBool> localpropBool = new ArrayList<>();
	    localpropBool.add(Testbed1TestHelper.makeTestStructBool());
        testObjToFill.setPropBool(localpropBool);
        List<StructInt> localpropInt = new ArrayList<>();
	    localpropInt.add(Testbed1TestHelper.makeTestStructInt());
        testObjToFill.setPropInt(localpropInt);
        List<StructFloat> localpropFloat = new ArrayList<>();
	    localpropFloat.add(Testbed1TestHelper.makeTestStructFloat());
        testObjToFill.setPropFloat(localpropFloat);
        List<StructString> localpropString = new ArrayList<>();
	    localpropString.add(Testbed1TestHelper.makeTestStructString());
        testObjToFill.setPropString(localpropString);
        List<Enum0> localpropEnum = new ArrayList<>();
	    localpropEnum.add(Enum0.Value1);
        testObjToFill.setPropEnum(localpropEnum);
        return testObjToFill;
    }

    static public IStructArray2Interface makeTestStructArray2Interface(IStructArray2Interface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        StructBoolWithArray localpropBool = Testbed1TestHelper.makeTestStructBoolWithArray();
        testObjToFill.setPropBool(localpropBool);
        StructIntWithArray localpropInt = Testbed1TestHelper.makeTestStructIntWithArray();
        testObjToFill.setPropInt(localpropInt);
        StructFloatWithArray localpropFloat = Testbed1TestHelper.makeTestStructFloatWithArray();
        testObjToFill.setPropFloat(localpropFloat);
        StructStringWithArray localpropString = Testbed1TestHelper.makeTestStructStringWithArray();
        testObjToFill.setPropString(localpropString);
        StructEnumWithArray localpropEnum = Testbed1TestHelper.makeTestStructEnumWithArray();
        testObjToFill.setPropEnum(localpropEnum);
        return testObjToFill;
    }
}
