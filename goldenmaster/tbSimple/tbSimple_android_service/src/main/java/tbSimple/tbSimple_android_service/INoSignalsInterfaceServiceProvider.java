package tbSimple.tbSimple_android_service;
import tbSimple.tbSimple_api.INoSignalsInterface;


public interface INoSignalsInterfaceServiceProvider {
    public  INoSignalsInterface getServiceInstance();
    public void clear();
}
