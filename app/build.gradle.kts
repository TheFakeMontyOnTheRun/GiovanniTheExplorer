plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 35
    defaultConfig {
        applicationId = "br.DroidDemos"
        minSdk = 21
        targetSdk = 36
        version = 2.0
        versionCode = 11
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildTypes.invoke {
        "release" {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }
    }
    namespace = "br.odb.giovanni"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${rootProject.extra["kotlinVersion"]}")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.core:core-ktx:1.16.0")
}
