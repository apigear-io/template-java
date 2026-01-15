package tbSimple.tbSimple_android_service;
import tbSimple.tbSimple_api.IVoidInterface;


public interface IVoidInterfaceServiceProvider {
    public  IVoidInterface getServiceInstance();
    public void clear();
}
