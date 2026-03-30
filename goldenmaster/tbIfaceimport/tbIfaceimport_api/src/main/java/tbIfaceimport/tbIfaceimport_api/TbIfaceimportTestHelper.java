package tbIfaceimport.tbIfaceimport_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TbIfaceimportTestHelper
{

    static public IEmptyIf makeTestEmptyIf(IEmptyIf testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        return testObjToFill;
    }
}
