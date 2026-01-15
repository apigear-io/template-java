package tbSimple.tbSimple_android_service;
import tbSimple.tbSimple_api.INoPropertiesInterface;


public interface INoPropertiesInterfaceServiceProvider {
    public  INoPropertiesInterface getServiceInstance();
    public void clear();
}
