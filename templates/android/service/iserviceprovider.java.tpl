package {{camel .Module.Name}}.{{camel .Module.Name}}_android_service;
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};


public interface I{{Camel .Interface.Name}}ServiceProvider {
    public  I{{Camel .Interface.Name }} getServiceInstance();
    public void clear();
}
