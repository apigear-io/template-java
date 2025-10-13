plugins {
    alias(libs.plugins.android.library)
}

group = "{{camel .Module.Name}}"
version = "{{.Module.Version}}"

android {
    namespace '{{camel .Module.Name}}.{{camel .Module.Name}}_android_service'
    compileSdk 35

    defaultConfig {
        minSdk 33
		targetSdk 34
		versionCode 1
		versionName "1.0"

		testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
	}

    testOptions {
        unitTests.includeAndroidResources = true
    }

}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation project(':{{camel .Module.Name}}_api')
    {{- if len (.Module.Interfaces)}}
    implementation project(':{{camel .Module.Name}}_impl')
    {{- end }}
    implementation project(':{{camel .Module.Name}}_android_messenger')
    {{- range .Module.Imports}}
    api '{{camel .Name}}:{{camel .Name}}_android_messenger:{{ ($.System.LookupModule .Name).Version }}'
    {{- $importModule := ($.System.LookupModule .Name) }}
    {{- if len $importModule.Interfaces}}
    implementation '{{camel .Name}}:{{camel .Name}}_impl:{{ $importModule.Version }}'
    {{- end }}
    {{- end }}
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.robolectric:robolectric:4.10.3'
    testImplementation 'org.mockito:mockito-core:5.12.0'
    testImplementation 'org.mockito:mockito-inline:5.2.0'
}
