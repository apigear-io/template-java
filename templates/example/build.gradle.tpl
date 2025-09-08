plugins {
    alias(libs.plugins.android.library)
}
android {
    namespace '{{camel .System.Name }}_example'
    compileSdk 35

    defaultConfig {
        minSdk 33
		targetSdk 34
		versionCode 1
		versionName "1.0"

		testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
	}

}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.2.0'
    {{- $withAndroid := .Features.android }}

    {{- range .System.Modules}}
    {{- if $withAndroid }}
    implementation("{{camel .Name}}:{{camel .Name}}_android_service:{{.Version}}")
    implementation("{{camel .Name}}:{{camel .Name}}_android_client:{{.Version}}")
    implementation("{{camel .Name}}:{{camel .Name}}_android_messenger:{{.Version}}")
    {{- end }}
    implementation("{{camel .Name}}:{{camel .Name}}_impl:{{.Version}}")
    implementation("{{camel .Name}}:{{camel .Name}}_api:{{.Version}}")
    {{- end }}

}