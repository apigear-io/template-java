plugins {
    alias(libs.plugins.android.library)
}

group = "{{camel .Module.Name}}"
version = "{{.Module.Version}}"

android {
    namespace '{{camel .Module.Name}}.{{camel .Module.Name}}_android_client'
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
    implementation project(':{{camel .Module.Name}}_android_messenger')
    {{- range .Module.Imports}}
    api '{{camel .Name}}:{{camel .Name}}_android_messenger:{{ ($.System.LookupModule .Name).Version }}'
    {{- end }}
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.robolectric:robolectric:4.10.3'
    testImplementation 'org.mockito:mockito-core:5.12.0'
    testImplementation 'org.mockito:mockito-inline:5.2.0'
    {{- if len (.Module.Interfaces)}}
    testImplementation project(':{{camel .Module.Name}}_impl')
    {{- end }}
    {{- range .Module.Imports}}
    {{- $importModule := ($.System.LookupModule .Name) }}
    {{- if len $importModule.Interfaces}}
    testImplementation '{{camel .Name}}:{{camel .Name}}_impl:{{ $importModule.Version }}'
    {{- end }}
    {{- end }}
}
