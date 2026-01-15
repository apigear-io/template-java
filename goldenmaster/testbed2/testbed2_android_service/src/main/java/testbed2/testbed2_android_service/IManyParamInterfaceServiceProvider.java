package testbed2.testbed2_android_service;
import testbed2.testbed2_api.IManyParamInterface;


public interface IManyParamInterfaceServiceProvider {
    public  IManyParamInterface getServiceInstance();
    public void clear();
}
