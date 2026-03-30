package tbSimple.tbSimple_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TbSimpleTestHelper
{

    static public IVoidInterface makeTestVoidInterface(IVoidInterface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        return testObjToFill;
    }

    static public ISimpleInterface makeTestSimpleInterface(ISimpleInterface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        testObjToFill.setPropBool(true);
        testObjToFill.setPropInt(1);
        testObjToFill.setPropInt32(1);
        testObjToFill.setPropInt64(1L);
        testObjToFill.setPropFloat(1.0f);
        testObjToFill.setPropFloat32(1.0f);
        testObjToFill.setPropFloat64(1.0);
        testObjToFill.setPropString(new String("xyz"));
        return testObjToFill;
    }

    static public ISimpleArrayInterface makeTestSimpleArrayInterface(ISimpleArrayInterface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        List<Boolean> localpropBool = new ArrayList<>();
	    localpropBool.add(true);
        testObjToFill.setPropBool(localpropBool);
        List<Integer> localpropInt = new ArrayList<>();
	    localpropInt.add(1);
        testObjToFill.setPropInt(localpropInt);
        List<Integer> localpropInt32 = new ArrayList<>();
	    localpropInt32.add(1);
        testObjToFill.setPropInt32(localpropInt32);
        List<Long> localpropInt64 = new ArrayList<>();
	    localpropInt64.add(1L);
        testObjToFill.setPropInt64(localpropInt64);
        List<Float> localpropFloat = new ArrayList<>();
	    localpropFloat.add(1.0f);
        testObjToFill.setPropFloat(localpropFloat);
        List<Float> localpropFloat32 = new ArrayList<>();
	    localpropFloat32.add(1.0f);
        testObjToFill.setPropFloat32(localpropFloat32);
        List<Double> localpropFloat64 = new ArrayList<>();
	    localpropFloat64.add(1.0);
        testObjToFill.setPropFloat64(localpropFloat64);
        List<String> localpropString = new ArrayList<>();
	    localpropString.add(new String("xyz"));
        testObjToFill.setPropString(localpropString);
        testObjToFill.setPropReadOnlyString(new String("xyz"));
        return testObjToFill;
    }

    static public INoPropertiesInterface makeTestNoPropertiesInterface(INoPropertiesInterface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        return testObjToFill;
    }

    static public INoOperationsInterface makeTestNoOperationsInterface(INoOperationsInterface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        testObjToFill.setPropBool(true);
        testObjToFill.setPropInt(1);
        return testObjToFill;
    }

    static public INoSignalsInterface makeTestNoSignalsInterface(INoSignalsInterface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        testObjToFill.setPropBool(true);
        testObjToFill.setPropInt(1);
        return testObjToFill;
    }

    static public IEmptyInterface makeTestEmptyInterface(IEmptyInterface testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        return testObjToFill;
    }
}
