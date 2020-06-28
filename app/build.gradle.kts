import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

val buildTime get() = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    .format(LocalDateTime.now(ZoneOffset.UTC))?:""

val properties by lazy {
    val value = Properties()
    value.load(FileInputStream(rootProject.file("local.properties")))
    value
}

android {
    signingConfigs {
        register("default") {
            storeFile = file(properties.getProperty("storeFile"))
            storePassword = properties.getProperty("password")
            keyPassword = properties.getProperty("password")
            keyAlias = properties.getProperty("keyAlias")
        }
    }

    compileSdkVersion(30)

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        applicationId = "info.horriblesubs.sher"
        versionName = "0.9.0-beta4"
        targetSdkVersion(30)
        minSdkVersion(21)
        versionCode = 90

        buildConfigField("String", "BUILD_TIME", "\"$buildTime\"")
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("default")
            isMinifyEnabled = false
        }

        getByName("debug") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("default")
            isMinifyEnabled = false
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

configurations {
    implementation.get().exclude(mapOf(
        "group" to "androidx.browser",
        "module" to "browser"
    ))
}

androidExtensions {
    isExperimental = true
}

dependencies {
    //kotlin-coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")

    //kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4-M2")

    //androidx.lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0-alpha05")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.3.0-alpha05")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.0-alpha05")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //androidx.*
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0-rc01")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta7")
    implementation("androidx.recyclerview:recyclerview:1.2.0-alpha04")
    implementation("androidx.annotation:annotation:1.2.0-alpha01")
    implementation("androidx.viewpager2:viewpager2:1.1.0-alpha01")
    implementation("androidx.appcompat:appcompat:1.3.0-alpha01")
    implementation("androidx.core:core-ktx:1.5.0-alpha01")

    //androidx.room
    implementation("androidx.room:room-runtime:2.3.0-alpha01")
    implementation("androidx.room:room-guava:2.3.0-alpha01")
    implementation("androidx.room:room-ktx:2.3.0-alpha01")
    kapt("androidx.room:room-compiler:2.3.0-alpha01")

    //com.google.android.material
    implementation("com.google.android.material:material:1.3.0-alpha01")

    //com.google.firebase
    implementation("com.google.firebase:firebase-analytics:17.4.3")
    implementation("com.google.firebase:firebase-messaging:20.2.1")
    implementation("com.google.firebase:firebase-core:17.4.3")

    //com.google.android
    implementation("com.google.android.gms:play-services-ads:19.2.0")
    implementation("com.google.android.play:core:1.7.3")

    //bumptech.glide
    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")

//    Coroutine Image Loader
//    implementation("io.coil-kt:coil:0.11.0")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //androidx.paging
    implementation("androidx.paging:paging-guava:3.0.0-alpha02")
    implementation("androidx.paging:paging-runtime-ktx:3.0.0-alpha02")
    testImplementation("androidx.paging:paging-common-ktx:3.0.0-alpha02")

//    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.20")

//    org.jsoup
//    implementation ("org.jsoup:jsoup:1.13.1")

    //preference-ktx
    implementation("androidx.preference:preference-ktx:1.1.1")

    //google-flexbox
    implementation("com.google.android:flexbox:2.0.1")

    /*
    * Fix for Duplicate class com.google.common.util.concurrent.ListenableFuture found in modules
    * jetified-guava-26.0-android.jar (com.google.guava:guava:26.0-android) and
    * jetified-listenablefuture-1.0.jar (com.google.guava:listenablefuture:1.0)
    */
//    implementation ("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.2-rc01")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.0.9")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0-rc01")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}