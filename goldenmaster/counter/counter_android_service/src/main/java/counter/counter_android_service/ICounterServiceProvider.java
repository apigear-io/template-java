package counter.counter_android_service;
import counter.counter_api.ICounter;


public interface ICounterServiceProvider {
    public  ICounter getServiceInstance();
    public void clear();
}
