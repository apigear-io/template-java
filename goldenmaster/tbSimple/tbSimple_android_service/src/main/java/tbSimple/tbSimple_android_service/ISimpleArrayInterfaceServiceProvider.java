package tbSimple.tbSimple_android_service;
import tbSimple.tbSimple_api.ISimpleArrayInterface;


public interface ISimpleArrayInterfaceServiceProvider {
    public  ISimpleArrayInterface getServiceInstance();
    public void clear();
}
