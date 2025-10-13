android {
    namespace '{{camel .Module.Name}}.{{camel .Module.Name}}_android_service'

	tasks.register('packageSources', Jar) {
		archiveClassifier.set('sources')
		from android.sourceSets.main.java.srcDirs
		include '**/*.java'
	}
}

dependencies {
	implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation project(':{{camel .Module.Name}}_api')
    {{- if len (.Module.Interfaces)}}
    api project(':{{camel .Module.Name}}_impl')
    {{- end }}
    api project(':{{camel .Module.Name}}_android_messenger')
    {{- range .Module.Imports}}
    api project(':{{camel .Name}}_android_messenger')
    {{- $importModule := ($.System.LookupModule .Name)}}
    {{- if len $importModule.Interfaces}}
    api project(':{{camel .Name}}_impl')
    {{- end }}
    {{- end }}
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.robolectric:robolectric:4.10.3'
    testImplementation 'org.mockito:mockito-core:5.12.0'
    testImplementation 'org.mockito:mockito-inline:5.2.0'
}
