android {
    namespace '{{camel .Module.Name}}.{{camel .Module.Name}}_api'
}

dependencies {
    implementation 'com.fasterxml.jackson.core:jackson-annotations:2.17.0'
    {{- range .Module.Externs}}
    {{- $externInfo := javaExtern . }}
    {{- if $externInfo.DownloadPackage}}
    api '{{$externInfo.DownloadPackage}}{{ if $externInfo.Version}}:{{$externInfo.Version}}{{end}}'
    {{- end }}
    {{- end }}
    {{- range .Module.Imports}}
    api project(':{{camel .Name}}_api')
    {{- end }}
}
