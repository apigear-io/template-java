package counter.counter_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CounterTestHelper
{

    static public ICounter makeTestCounter(ICounter testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        customTypes.customTypes_api.Vector3D localvector = customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D();
        testObjToFill.setVector(localvector);
        List<customTypes.customTypes_api.Vector3D> localvectorArray = new ArrayList<>();
	    localvectorArray.add(customTypes.customTypes_api.CustomTypesTestHelper.makeTestVector3D());
        testObjToFill.setVectorArray(localvectorArray);
        List<org.apache.commons.math3.geometry.euclidean.threed.Vector3D> localexternVectorArray = new ArrayList<>();
        testObjToFill.setExternVectorArray(localexternVectorArray);
        return testObjToFill;
    }
}
