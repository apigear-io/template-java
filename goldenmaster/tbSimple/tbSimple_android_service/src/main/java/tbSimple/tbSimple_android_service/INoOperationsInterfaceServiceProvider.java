package tbSimple.tbSimple_android_service;
import tbSimple.tbSimple_api.INoOperationsInterface;


public interface INoOperationsInterfaceServiceProvider {
    public  INoOperationsInterface getServiceInstance();
    public void clear();
}
