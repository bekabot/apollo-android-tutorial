rootProject.name="apollo-android-tutorial"
include(":app")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        jcenter()
        maven {
            url = uri("https://dl.bintray.com/apollographql/android")
        }

    }
}