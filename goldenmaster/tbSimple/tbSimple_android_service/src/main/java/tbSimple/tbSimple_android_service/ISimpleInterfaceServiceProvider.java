package tbSimple.tbSimple_android_service;
import tbSimple.tbSimple_api.ISimpleInterface;


public interface ISimpleInterfaceServiceProvider {
    public  ISimpleInterface getServiceInstance();
    public void clear();
}
