package tbEnum.tbEnum_android_service;
import tbEnum.tbEnum_api.IEnumInterface;


public interface IEnumInterfaceServiceProvider {
    public  IEnumInterface getServiceInstance();
    public void clear();
}
