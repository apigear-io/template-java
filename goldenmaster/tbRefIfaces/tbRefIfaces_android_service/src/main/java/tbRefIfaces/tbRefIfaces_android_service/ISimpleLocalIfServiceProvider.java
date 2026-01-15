package tbRefIfaces.tbRefIfaces_android_service;
import tbRefIfaces.tbRefIfaces_api.ISimpleLocalIf;


public interface ISimpleLocalIfServiceProvider {
    public  ISimpleLocalIf getServiceInstance();
    public void clear();
}
