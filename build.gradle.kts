// Top-level build file

buildscript {
    repositories {
        google()        // 🔥 VERY IMPORTANT
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
}