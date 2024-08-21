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
  public static interface I{{Camel .Name }}EventListener extends EventListener {
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

    // property listeners
    void addPropertyChangeListener(PropertyChangeListener listener);
    void removePropertyChangeListener(PropertyChangeListener listener);

    // signal listeners
    void add{{Camel .Name}}EventListener(I{{Camel .Name }}EventListener listener);
    void remove{{Camel .Name}}EventListener(I{{Camel .Name }}EventListener listener);
  }
  {{- end }}

}