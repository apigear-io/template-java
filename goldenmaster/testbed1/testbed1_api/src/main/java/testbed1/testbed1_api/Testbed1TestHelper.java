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
        testStruct.fieldBool = new boolean[1];
	    testStruct.fieldBool[0] = true;
        return testStruct;
    }

    static public StructIntWithArray makeTestStructIntWithArray() 
    {
        StructIntWithArray testStruct = new StructIntWithArray();
        testStruct.fieldInt = new int[1];
	    testStruct.fieldInt[0] = 1;
        return testStruct;
    }

    static public StructFloatWithArray makeTestStructFloatWithArray() 
    {
        StructFloatWithArray testStruct = new StructFloatWithArray();
        testStruct.fieldFloat = new float[1];
	    testStruct.fieldFloat[0] = 1.0f;
        return testStruct;
    }

    static public StructStringWithArray makeTestStructStringWithArray() 
    {
        StructStringWithArray testStruct = new StructStringWithArray();
        testStruct.fieldString = new String[1];
	    testStruct.fieldString[0] = new String("xyz");
        return testStruct;
    }

    static public StructStructWithArray makeTestStructStructWithArray() 
    {
        StructStructWithArray testStruct = new StructStructWithArray();
        testStruct.fieldStruct = new StructStringWithArray[1];
	    testStruct.fieldStruct[0] = Testbed1TestHelper.makeTestStructStringWithArray();
        return testStruct;
    }

    static public StructEnumWithArray makeTestStructEnumWithArray() 
    {
        StructEnumWithArray testStruct = new StructEnumWithArray();
        testStruct.fieldEnum = new Enum0[1];
	    testStruct.fieldEnum[0] = Enum0.Value1;
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
        StructBool[] localpropBool = new StructBool[1];
	    localpropBool[0] = Testbed1TestHelper.makeTestStructBool();
        testObjToFill.setPropBool(localpropBool);
        StructInt[] localpropInt = new StructInt[1];
	    localpropInt[0] = Testbed1TestHelper.makeTestStructInt();
        testObjToFill.setPropInt(localpropInt);
        StructFloat[] localpropFloat = new StructFloat[1];
	    localpropFloat[0] = Testbed1TestHelper.makeTestStructFloat();
        testObjToFill.setPropFloat(localpropFloat);
        StructString[] localpropString = new StructString[1];
	    localpropString[0] = Testbed1TestHelper.makeTestStructString();
        testObjToFill.setPropString(localpropString);
        Enum0[] localpropEnum = new Enum0[1];
	    localpropEnum[0] = Enum0.Value1;
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