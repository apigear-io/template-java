package counter.counter_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public class CounterTestHelper
{

    static public ICounter makeTestCounter(ICounter testObjToFill) 
    {
        if (testObjToFill == null){return testObjToFill;}
        customTypes.customTypes_api.Vector3D localvector = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
        testObjToFill.setVector(localvector);
        customTypes.customTypes_api.Vector3D[] localvectorArray = new customTypes.customTypes_api.Vector3D[1];
	    localvectorArray[0] = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
        testObjToFill.setVectorArray(localvectorArray);
        org.apache.commons.math3.geometry.euclidean.threed.Vector3D[] localexternVectorArray = new org.apache.commons.math3.geometry.euclidean.threed.Vector3D[1];
        testObjToFill.setExternVectorArray(localexternVectorArray);
        return testObjToFill;
    }
}