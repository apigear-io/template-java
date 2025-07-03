package {{camel .Module.Name}}.{{camel .Module.Name}}_android_service;
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }};


public interface I{{Camel .Interface.Name}}ServiceFactory {
    public  I{{Camel .Interface.Name }} getServiceInstance();
}
