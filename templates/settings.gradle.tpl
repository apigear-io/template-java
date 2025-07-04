pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "{{camel .Module.Name}}"
{{- if .Features.android }}
include ':{{camel .Module.Name}}_android_service'
include ':{{camel .Module.Name}}_android_messenger'
{{- end -}}
{{- if .Features.stubs }}
include ':{{camel .Module.Name}}_impl'
{{- end -}}
{{- if .Features.api }}
include ':{{camel .Module.Name}}_api'
{{- end -}}
