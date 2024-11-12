import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-android-extensions")
}

android {
    namespace = "cr.ac.utn.movil"
    compileSdk = 34


    defaultConfig {
        applicationId = "cr.ac.utn.movil"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Habilitar View Binding
    viewBinding {

        enable = true
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

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.firebase.inappmessaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
<<<<<<< HEAD
<<<<<<< HEAD
    // Para RecyclerView
=======

>>>>>>> b74727a1950c15d48a109ac7f7811ffbb1d1aeeb
    implementation (libs.androidx.recyclerview)
    implementation (libs.androidx.appcompat)
    implementation (libs.androidx.constraintlayout)
<<<<<<< HEAD
=======
>>>>>>> 4186f466dd932a7107ad69a6fc5e130fccde3ad1
=======
>>>>>>> b74727a1950c15d48a109ac7f7811ffbb1d1aeeb

}