plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace '{{camel .Module.Name}}.{{camel .Module.Name}}_android_service'
    compileSdk 35

    defaultConfig {
        minSdk 34
		targetSdk 34
		versionCode 1
		versionName "1.0"

		testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
	}

}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation project(':{{camel .Module.Name}}_api')
    implementation project(':{{camel .Module.Name}}_impl')
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.robolectric:robolectric:4.10.3'
    testImplementation 'org.mockito:mockito-core:5.12.0'
    testImplementation 'org.mockito:mockito-inline:5.2.0'
}
