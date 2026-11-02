plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.zohrah.androidassignments"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.zohrah.androidassignments"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Enable coverage for local unit tests
            enableUnitTestCoverage = true

            // Enable coverage for instrumented tests
            enableAndroidTestCoverage = true
        }

        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Unit testing
    testImplementation(libs.junit)

    // Instrumented testing
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation("androidx.test:core:1.7.0")
}
