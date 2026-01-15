package tbIfaceimport.tbIfaceimport_android_service;
import tbIfaceimport.tbIfaceimport_api.IEmptyIf;


public interface IEmptyIfServiceProvider {
    public  IEmptyIf getServiceInstance();
    public void clear();
}
