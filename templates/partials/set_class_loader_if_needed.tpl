{{- define "setClassLoaderIfNeeded" }}
    {{- $numOfStructsSameModule := 0 }}
    {{- $numOfStructsOtherModule := 0 }}
    {{- $parcelableFromSameModule := "" }}
    {{- $parcelableFromOtherModule := "" }}
    {{- range .}}
	{{- if not .IsPrimitive }}
        {{- if (eq (.Schema.Import )  "" ) -}}
            {{- if eq   $numOfStructsSameModule  0   }}
                {{- $parcelableFromSameModule =   .  }}
            {{- end }}
            {{- $numOfStructsSameModule = len (printf "%*s " $numOfStructsSameModule "")  }}
        {{- else }}
            {{- if eq $numOfStructsOtherModule  0 }}
                {{- $parcelableFromOtherModule =  . }}
            {{- end }}
            {{- $numOfStructsOtherModule = len (printf "%*s " $numOfStructsOtherModule "") }}
	    {{- end }}
	{{- end }}
	{{- end }}

    {{- if $numOfStructsSameModule }}
        {{- if or (gt $numOfStructsSameModule 1) (ge $numOfStructsOtherModule 1) }}
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        {{- end }}
        data.setClassLoader({{template "getParcelable" $parcelableFromSameModule }}.class.getClassLoader());

    {{- else if $numOfStructsOtherModule}}
        {{- if gt $numOfStructsOtherModule 1 }}
    // all structs (even from other modules) are known at compile time (see gradle files) and share same PathClassLoader, any class loader provides access to it.
        {{- end }}
        data.setClassLoader({{template "getParcelable" $parcelableFromOtherModule }}.class.getClassLoader());
	{{- end }}
{{- end }}
