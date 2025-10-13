plugins {
    id 'java-library'
}

group = "{{camel .Module.Name}}"
version = "{{.Module.Version}}"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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
    api '{{camel .Name}}:{{camel .Name}}_api:{{ ($.System.LookupModule .Name).Version }}'
    {{- end }}
}