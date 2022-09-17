plugins {
	id("com.android.application")
	id("kotlin-android")
}

android {
	compileSdkVersion(32)
	defaultConfig {
		applicationId = "br.DroidDemos"
		minSdkVersion(19)
		targetSdkVersion(32)
		version = 2.0
		versionCode = 10
	}
	buildTypes.invoke {
		"release" {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
		}
	}
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${rootProject.extra["kotlin_version"]}")
	implementation("androidx.appcompat:appcompat:1.4.1")
	implementation("androidx.core:core-ktx:1.7.0")
}
