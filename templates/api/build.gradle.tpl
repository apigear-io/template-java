plugins {
    id 'java-library'
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation 'com.fasterxml.jackson.core:jackson-annotations:2.17.0'
}