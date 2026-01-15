package testbed1.testbed1_android_service;
import testbed1.testbed1_api.IStructInterface;


public interface IStructInterfaceServiceProvider {
    public  IStructInterface getServiceInstance();
    public void clear();
}
