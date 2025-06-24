package {{dot .Module.Name}}.{{dot .Module.Name}}_api;

import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }}EventListener;
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.I{{Camel .Interface.Name }};
//TODO imported/extern modules
{{- range .Module.Structs }}
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Name}};
{{- end }}
{{- range .Module.Enums }}
import {{dot .Module.Name}}.{{dot .Module.Name}}_api.{{Camel .Name}};
{{- end }}

import java.util.Collection;
import java.util.HashSet;

{{- $interfaceName := printf "I%s" (Camel .Interface.Name) }}
  public abstract class Abstract{{Camel .Interface.Name}} implements {{$interfaceName}} {
    public Collection<{{$interfaceName}}EventListener> listeners = new HashSet<>();

    public void addEventListener({{$interfaceName}}EventListener listener) {
      listeners.add(listener); 
    }
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
    
  }
