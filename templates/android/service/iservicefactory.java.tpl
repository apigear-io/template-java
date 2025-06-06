package {{camel .Module.Name}}.android.service
import {{dot .Module.Name}}.api.I{{Camel .Interface.Name }};
//TODO should be{{dot .Module.Name}}.api.{{camel .Interface.Name}}

public interface I{{Camel .Interface.Name}}ServiceFactory {
    public  I{{Camel .Interface.Name }} getServiceInstance();
}
