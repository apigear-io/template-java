pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
                includeGroupByRegex("org\\.jetbrains.*") // allow Kotlin
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}


rootProject.name = "{{camel .System.Name}}"
include ':{{camel .System.Name }}_example'
{{- $withAndroid := .Features.android }}
{{- range .System.Modules}}
includeBuild '{{camel .Name}}'
{{- end }}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            // your libs.versions.toml will be picked up automatically
        }
    }

}
