plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace '{{camel .Module.Name}}.{{camel .Module.Name}}serviceexample'
    compileSdk 35

    defaultConfig {
        applicationId "{{camel .Module.Name}}.{{camel .Module.Name}}serviceexample"
        minSdk 33
        targetSdk 35
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
}

dependencies {

    implementation libs.appcompat
    implementation libs.material
    implementation project(':{{camel .Module.Name}}_api')
    implementation project(':{{camel .Module.Name}}_android_messenger')
    implementation project(':{{camel .Module.Name}}_android_service')
    implementation project(':{{camel .Module.Name}}_impl')
    testImplementation libs.junit
    androidTestImplementation libs.ext.junit
    androidTestImplementation libs.espresso.core
}