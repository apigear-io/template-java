package {{camel .Module.Name}}.{{camel .Module.Name}}_android_messenger;

public enum {{Camel .Interface.Name}}MessageType {
    REGISTER_CLIENT(0),
    UNREGISTER_CLIENT(1),
    INIT(2),
    {{- $msgNum :=  2}}
{{- range .Interface.Properties }}
    {{- $msgNum =  len (printf "%*s " $msgNum "" ) }}
    PROP_{{Camel .Name}}({{$msgNum}}),
    {{- $msgNum =  len (printf "%*s " $msgNum "" ) }}
    SET_{{Camel .Name}}({{$msgNum}}),
{{- end }}
{{- range .Interface.Signals }}
    {{- $msgNum =  len (printf "%*s " $msgNum "" ) }}
    SIG_{{Camel .Name}}({{$msgNum}}),
{{- end }}
{{- range .Interface.Operations }}
    {{- $msgNum =  len (printf "%*s " $msgNum "" ) }}
    RPC_{{Camel .Name}}Req({{$msgNum}}),
    {{- $msgNum =  len (printf "%*s " $msgNum "" ) }}
    RPC_{{Camel .Name}}Resp({{$msgNum}}),
{{- end }}
    {{Camel .Interface.Name}}MessageType_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    {{Camel .Interface.Name}}MessageType(int value) {this.value = value;}

    public int getValue()
    {
        return this.value;
    }

    public static {{Camel .Interface.Name}}MessageType fromInteger(int value)
    {
        for ({{Camel .Interface.Name}}MessageType event : {{Camel .Interface.Name}}MessageType.values())
        {
            if (event.getValue() == value)
            {
                return event;
            }
        }

        return {{Camel .Interface.Name}}MessageType_UNKNOWN;
    }
}
