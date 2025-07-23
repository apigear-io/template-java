package {{dot .Module.Name}}.{{dot .Module.Name}}_api;

import com.fasterxml.jackson.annotation.JsonProperty;

//TODO imports - may need some struct from this or imported module

  public  class {{Camel .Struct.Name}} {

    public {{Camel .Struct.Name}}({{javaParams "" .Struct.Fields}}) {
      {{- range .Struct.Fields }}
      this.{{camel .Name}} = {{camel .Name}};
      {{- end }}
    }  

     public {{Camel .Struct.Name}}() {}
  {{- range .Struct.Fields }}
    @JsonProperty("{{snake .Name}}")
    public {{javaType "" .}} {{camel .Name}};
  {{- end }}

    public {{Camel .Struct.Name}}({{Camel .Struct.Name}} other) {
{{- range .Struct.Fields }}
    this.{{camel .Name}} = other.{{camel .Name}};
  {{- end }}
    //TODO deepcopy of structs and arrays this.x = new sth(other.x);
}

  }
