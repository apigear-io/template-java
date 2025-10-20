package tbIfaceimport.tbIfaceimport_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

public class TbIfaceimportTestHelper
{

    static public IEmptyIf makeTestEmptyIf(IEmptyIf testObjToFill) 
    {
        if (testObjToFill == null){return testObjToFill;}
        return testObjToFill;
    }
}