package testbed1.testbed1_android_service;
import testbed1.testbed1_api.IStructInterface;


public interface IStructInterfaceServiceFactory {
    public  IStructInterface getServiceInstance();
    public void clear();
}
