buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath("com.apollographql.apollo3:apollo-gradle-plugin:3.0.0-dev2")
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.30")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

