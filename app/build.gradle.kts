import dependencies.AndroidTestDependencies
import dependencies.Dependencies
import dependencies.SupportDependencies
import dependencies.TestDependencies

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

    sourceSets {
        getByName("test").resources.srcDir("src/test/res")
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    // Kotlin
    implementation(Dependencies.kotlin_standard_library)
    implementation(Dependencies.kotlin_reflect)
    implementation(Dependencies.ktx)
    implementation(Dependencies.kotlin_coroutines)
    implementation(Dependencies.kotlin_coroutines_android)
    implementation(Dependencies.kotlin_coroutines_play_services)

    // Dagger
    implementation(Dependencies.dagger)

    // Annotation Processing
    kapt(AnnotationProcessing.dagger_compiler)

    // Firebase
    implementation(Dependencies.firebase_firestore)
    implementation(Dependencies.firebase_auth)
    implementation(Dependencies.firebase_analytics)
    implementation(Dependencies.firebase_crashlytics)

    // Network
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofit_gson)

    // Support
    implementation(SupportDependencies.appcompat)
    implementation(SupportDependencies.constraintlayout)
    implementation(SupportDependencies.material_design)
    implementation(SupportDependencies.swipe_refresh_layout)

    // Unit test
    testImplementation(TestDependencies.junit4)
    testImplementation(TestDependencies.jupiter_params)
    testImplementation(TestDependencies.jupiter_api)
    testImplementation(TestDependencies.mockk)
    testImplementation(Dependencies.retrofit_gson)
    testRuntimeOnly(TestDependencies.jupiter_engine)

}



