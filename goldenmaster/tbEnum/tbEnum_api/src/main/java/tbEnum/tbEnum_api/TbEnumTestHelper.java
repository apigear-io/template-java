package tbEnum.tbEnum_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public class TbEnumTestHelper
{

    static public IEnumInterface makeTestEnumInterface(IEnumInterface testObjToFill) 
    {
        if (testObjToFill == null){return testObjToFill;}
        testObjToFill.setProp0(Enum0.Value1);
        testObjToFill.setProp1(Enum1.Value2);
        testObjToFill.setProp2(Enum2.Value1);
        testObjToFill.setProp3(Enum3.Value2);
        return testObjToFill;
    }
}