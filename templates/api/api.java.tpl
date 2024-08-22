package {{dot .Module.Name}}.api;

import com.fasterxml.jackson.annotation.JsonProperty;


public class {{Camel .Module.Name}} {

  // enumerations
  {{- range .Module.Enums }}
  public enum {{Camel .Name}} {
  {{- range .Members }}
      @JsonProperty("{{.Value}}")
      {{Camel .Name}},
  {{- end }}
  }
  {{- end }}

  // data structures
  {{- range .Module.Structs }}
  public static class {{Camel .Name}} {
    public {{Camel .Name}}({{javaParams "" .Fields}}) {
      {{- range .Fields }}
      this.{{camel .Name}} = {{camel .Name}};
      {{- end }}
    }  
  {{- range .Fields }}
    @JsonProperty("{{snake .Name}}")
    public {{javaType "" .}} {{camel .Name}};
  {{- end }}
  }
  {{- end }}

  // interfaces
  {{- range .Module.Interfaces }}
  public static interface I{{Camel .Name }}EventListener {
  {{- range .Properties }}
    void on{{Camel .Name}}Changed({{javaType "" .}} oldValue, {{javaType "" .}} newValue);
  {{- end }}
  {{- range .Signals }}
    void on{{Camel .Name}}({{javaParams "" .Params}});
  {{- end }}
  }

  public static interface I{{Camel .Name }} {
    // properties
  {{- range .Properties }}
    void set{{Camel .Name}}({{javaParam "" .}});
    {{javaReturn "" . }} get{{Camel .Name}}();
    void fire{{Camel .Name}}Changed({{javaType "" .}} oldValue, {{javaType "" .}} newValue);
  {{ end }}
    // methods
  {{- range .Operations }}
    {{javaReturn "" .Return}} {{camel .Name}}({{javaParams "" .Params}});
  {{- end }}

    // signal listeners
    void addEventListener(I{{Camel .Name }}EventListener listener);
    void removeEventListener(I{{Camel .Name }}EventListener listener);
  }

  public static class Abstract{{Camel .Name}} implements I{{Camel .Name }} {
    public Collection<IVoidInterfaceEventListener> events = new HashSet<>();

    public void addEventListener(IVoidInterfaceEventListener listener) {
      listeners.add(listener); 
    }
    public void removeEventListener(IVoidInterfaceEventListener listener) {
      listeners.remove(listener);
    }
  {{- range .Properties }}
    @Override
    public void fire{{Camel .Name}}Changed({{javaType "" .}} oldValue, {{javaType "" .}} newValue) {
      for (IVoidInterfaceEventListener listener : events) {
        listener.on{{Camel .Name}}Changed(oldValue, newValue);
      }
    }
  {{ end }}
  {{- range .Signals }}
    @Override
    public void fire{{Camel .Name}}({{javaParams "" .Params}}) {
      for (I{{Camel .Name }}EventListener listener : events) {
        listener.on{{Camel .Name}}({{ javaVars .Params}});
      }
    }
  {{ end }}
    
  }
  {{- end }}
}