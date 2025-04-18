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

rootProject.name = "AE.Health"
include(":app")
include(":data")
include(":domain")
include(":core")
include(":feature")
include(":domain:search")
include(":data:search")
include(":feature:home")
include(":core:util")
include(":network")
include(":network:search")
include(":core:network")
include(":core:config")
include(":core:ui")
