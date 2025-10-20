package tbRefIfaces.tbRefIfaces_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Arrays;

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
        ISimpleLocalIf[] locallocalIfList = new ISimpleLocalIf[1];
        testObjToFill.setLocalIfList(locallocalIfList);
        tbIfaceimport.tbIfaceimport_api.IEmptyIf[] localimportedIfList = new tbIfaceimport.tbIfaceimport_api.IEmptyIf[1];
        testObjToFill.setImportedIfList(localimportedIfList);
        return testObjToFill;
    }
}