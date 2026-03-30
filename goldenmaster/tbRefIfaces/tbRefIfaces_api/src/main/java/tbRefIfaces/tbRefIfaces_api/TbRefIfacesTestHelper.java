package tbRefIfaces.tbRefIfaces_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TbRefIfacesTestHelper
{

    static public ISimpleLocalIf makeTestSimpleLocalIf(ISimpleLocalIf testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        testObjToFill.setIntProperty(1);
        return testObjToFill;
    }

    static public IParentIf makeTestParentIf(IParentIf testObjToFill)
    {
        if (testObjToFill == null){return testObjToFill;}
        List<ISimpleLocalIf> locallocalIfList = new ArrayList<>();
        testObjToFill.setLocalIfList(locallocalIfList);
        List<tbIfaceimport.tbIfaceimport_api.IEmptyIf> localimportedIfList = new ArrayList<>();
        testObjToFill.setImportedIfList(localimportedIfList);
        return testObjToFill;
    }
}
