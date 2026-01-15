package tbRefIfaces.tbRefIfaces_android_service;
import tbRefIfaces.tbRefIfaces_api.IParentIf;


public interface IParentIfServiceFactory {
    public  IParentIf getServiceInstance();
    public void clear();
}
