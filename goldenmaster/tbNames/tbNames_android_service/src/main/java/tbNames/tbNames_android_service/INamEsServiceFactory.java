package tbNames.tbNames_android_service;
import tbNames.tbNames_api.INamEs;


public interface INamEsServiceFactory {
    public  INamEs getServiceInstance();
    public void clear();
}
