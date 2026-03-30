package tbNames.tbNames_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TbNamesTestHelper
{

    static public INamEs makeTestNamEs(INamEs testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        testObjToFill.setSwitch(true);
        testObjToFill.setSomeProperty(1);
        testObjToFill.setSomePoperty2(1);
        testObjToFill.setEnumProperty(EnumWithUnderScores.SecondValue);
        return testObjToFill;
    }
}
