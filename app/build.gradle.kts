plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.blipblipcode.distribuidoraayl"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.blipblipcode.distribuidoraayl"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packagingOptions.resources.excludes.addAll(listOf("/META-INF/{AL2.0,LGPL2.1}", "META-INF/gradle/incremental.annotation.processors") )

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.androidx.livedata)
    implementation(libs.kotlin.reflect)

    //Preferences
    implementation(libs.androidx.preference.ktx)
    implementation(libs.datastore)

    //camera
    implementation(libs.bundles.cameraX)

    //compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)

    //pdf
    implementation(libs.itext7.core)

    //navigation
    implementation(libs.bundles.compose.navigation)

    //ui
    implementation(libs.bundles.androidx.uiMaterial)
    implementation(libs.lottie.compose)
    implementation(libs.form.builder)

    //dateTime
    implementation(libs.bundles.dateTime)

    //barcode
    implementation(libs.barcode.scanner)

    //network
    implementation(libs.bundles.network)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.interceptor)

    //firebase
    implementation(platform(libs.firebase.boom))
    implementation(libs.bundles.firebase)

    //playService
    implementation(libs.bundles.play.service)

    //room
    implementation(libs.bundles.room)
    annotationProcessor(libs.android.room.compiler)
    ksp(libs.android.room.compiler)


    //hilt
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    //workManager
    implementation(libs.bundles.work.manager)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(kotlin("reflect"))
}