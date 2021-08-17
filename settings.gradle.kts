enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement.repositories {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    mavenCentral()
    google()
}

rootProject.name = "JustTvLauncher"
include(":app")
