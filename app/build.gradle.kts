plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.hero_finder"
    compileSdk = 36

    buildFeatures{
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.hero_finder"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val marvelPublicKey: String = project.property("MARVEL_PUBLIC_KEY") as String
        val marvelPrivateKey: String = project.property("MARVEL_PRIVATE_KEY") as String

        buildConfigField("String", "MARVEL_PUBLIC_KEY", "\"${marvelPublicKey}\"")
        buildConfigField("String", "MARVEL_PRIVATE_KEY", "\"${marvelPrivateKey}\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.codepath.libraries:asynchttpclient:2.2.0")
}