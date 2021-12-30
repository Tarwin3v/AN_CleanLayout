import dependencies.AndroidTestDependencies
import dependencies.Dependencies
import dependencies.SupportDependencies

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdk = Versions.compilesdk
    defaultConfig {
        applicationId = Application.id
        minSdk = Versions.minsdk
        targetSdk = Versions.targetsdk
        versionCode = Application.version_code
        versionName = Application.version_name

        testInstrumentationRunner = AndroidTestDependencies.instrumentation_runner
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = Java.java_version
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    // Kotlin
    implementation(Dependencies.kotlin_standard_library)
    implementation(Dependencies.kotlin_reflect)
    implementation(Dependencies.ktx)

    // Support
    implementation(SupportDependencies.appcompat)
    implementation(SupportDependencies.constraintlayout)
    implementation(SupportDependencies.material_design)
    implementation(SupportDependencies.swipe_refresh_layout)

}



