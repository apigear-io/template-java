package tbSimple.tbSimple_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

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
        boolean[] localpropBool = new boolean[1];
	    localpropBool[0] = true;
        testObjToFill.setPropBool(localpropBool);
        int[] localpropInt = new int[1];
	    localpropInt[0] = 1;
        testObjToFill.setPropInt(localpropInt);
        int[] localpropInt32 = new int[1];
	    localpropInt32[0] = 1;
        testObjToFill.setPropInt32(localpropInt32);
        long[] localpropInt64 = new long[1];
	    localpropInt64[0] = 1L;
        testObjToFill.setPropInt64(localpropInt64);
        float[] localpropFloat = new float[1];
	    localpropFloat[0] = 1.0f;
        testObjToFill.setPropFloat(localpropFloat);
        float[] localpropFloat32 = new float[1];
	    localpropFloat32[0] = 1.0f;
        testObjToFill.setPropFloat32(localpropFloat32);
        double[] localpropFloat64 = new double[1];
	    localpropFloat64[0] = 1.0;
        testObjToFill.setPropFloat64(localpropFloat64);
        String[] localpropString = new String[1];
	    localpropString[0] = new String("xyz");
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