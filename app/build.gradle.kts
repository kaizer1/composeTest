
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.com.google.service)
    alias(libs.plugins.com.crash)
}

android {
    namespace = "cather.lfree.workdscather"
    compileSdk = 34

    defaultConfig {
        applicationId = "cather.lfree.workdscather"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        resourceConfigurations.plus(listOf("ru", "en"))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }

    androidResources {
        generateLocaleConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)

     val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
  implementation(composeBom)

  implementation (libs.play.services.code.scanner)
  debugImplementation("androidx.compose.ui:ui-tooling")
  implementation("androidx.compose.ui:ui-tooling-preview")

    implementation ("androidx.compose.material3:material3")
    implementation ("androidx.navigation:navigation-compose:2.7.5")

    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)


    implementation(libs.squareup.okhttp3 )
    implementation(platform(libs.squareup.okhttp3.bom))
    implementation (libs.viewmodel)
    implementation (libs.androidx.lifecycle.extensions)
    implementation(platform(libs.fire.bom))
    implementation(libs.fire.analy)
    implementation(libs.fire.firebase)
    implementation(libs.fire.crash)

    implementation ("androidx.camera:camera-core:1.3.1")
    implementation ("androidx.camera:camera-camera2:1.3.1")
    implementation ("androidx.camera:camera-lifecycle:1.3.1")
    implementation ("androidx.camera:camera-view:1.3.1")
    implementation ("com.google.zxing:core:3.3.3")

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}