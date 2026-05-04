plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleServices) // Plugin de Firebase
    alias(libs.plugins.ksp) // Plugin para Room
}

android {
    namespace = "com.flufiai.flufiaigo"
    compileSdk = 36

    buildFeatures {
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.flufiai.flufiaigo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0-entrega2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // UI & Material
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)

    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
}