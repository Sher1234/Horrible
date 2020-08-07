buildscript {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://maven.google.com")
        maven("https://jitpack.io")
        mavenCentral()
        jcenter()
        google()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4-M3")
        classpath("com.android.tools.build:gradle:4.2.0-alpha05")
        classpath("com.google.gms:google-services:4.3.3")
    }
}

allprojects {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://maven.google.com")
        maven("https://jitpack.io")
        mavenCentral()
        jcenter()
        google()
    }
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}