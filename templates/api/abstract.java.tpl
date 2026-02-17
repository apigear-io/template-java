package {{camel .Module.Name}}.{{camel .Module.Name}}_api;

import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.I{{Camel .Interface.Name }};

{{- range .Module.Structs }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}
{{- range .Module.Enums }}
import {{camel .Module.Name}}.{{camel .Module.Name}}_api.{{Camel .Name}};
{{- end }}

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

{{- $interfaceName := printf "I%s" (Camel .Interface.Name) }}
  public abstract class Abstract{{Camel .Interface.Name}} implements {{$interfaceName}} {
    private Collection<{{$interfaceName}}EventListener> listeners = ConcurrentHashMap.newKeySet();

    @Override
    public void addEventListener({{$interfaceName}}EventListener listener) {
      listeners.add(listener); 
    }
    @Override
    public void removeEventListener({{$interfaceName}}EventListener listener) {
      listeners.remove(listener);
    }
  {{- range .Interface.Properties }}
    @Override
    public void fire{{Camel .Name}}Changed({{javaType "" .}} newValue) {
      for ({{$interfaceName}}EventListener listener : listeners) {
        listener.on{{Camel .Name}}Changed(newValue);
      }
    }
  {{ end }}
  {{- range .Interface.Signals }}
    @Override
    public void fire{{Camel .Name}}({{javaParams "" .Params}}) {
      for ({{$interfaceName}}EventListener listener : listeners) {
        listener.on{{Camel .Name}}({{ javaVars .Params}});
      }
    }
  {{ end }}
    @Override
    public void fire_readyStatusChanged(boolean isReady)
    {
        for ({{$interfaceName}}EventListener listener : listeners) {
        listener.on_readyStatusChanged(isReady);
      }
    }
  }
