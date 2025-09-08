buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

}

tasks.register('runAll') {

    description = "Builds app and all included builds (modulex, moduley) with all submodules"
    group = "build"
    {{- $withAndroid := .Features.android }}

    {{- range .System.Modules}}
    {{- if $withAndroid }}
    dependsOn gradle.includedBuild('{{camel .Name}}').task(':{{camel .Name}}_android_service:build')
    dependsOn gradle.includedBuild('{{camel .Name}}').task(':{{camel .Name}}_android_client:build')
    dependsOn gradle.includedBuild('{{camel .Name}}').task(':{{camel .Name}}_android_messenger:build')
    {{- end }}
    dependsOn gradle.includedBuild('{{camel .Name}}').task(':{{camel .Name}}_impl:build')
    dependsOn gradle.includedBuild('{{camel .Name}}').task(':{{camel .Name}}_api:build')
    {{- end }}

    dependsOn project(':{{camel .System.Name }}_example').tasks.named('build')

}

tasks.register("runJavaUnitTests") {
    description = "Runs all tests in all submodules"
    dependsOn subprojects.collect { it.tasks.withType(Test) }
}