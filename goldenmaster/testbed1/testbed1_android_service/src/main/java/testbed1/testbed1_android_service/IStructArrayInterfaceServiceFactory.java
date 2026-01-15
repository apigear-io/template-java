package testbed1.testbed1_android_service;
import testbed1.testbed1_api.IStructArrayInterface;


public interface IStructArrayInterfaceServiceFactory {
    public  IStructArrayInterface getServiceInstance();
    public void clear();
}
