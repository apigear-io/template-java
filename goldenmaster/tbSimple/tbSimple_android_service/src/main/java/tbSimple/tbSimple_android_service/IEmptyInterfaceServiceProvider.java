package tbSimple.tbSimple_android_service;
import tbSimple.tbSimple_api.IEmptyInterface;


public interface IEmptyInterfaceServiceProvider {
    public  IEmptyInterface getServiceInstance();
    public void clear();
}
