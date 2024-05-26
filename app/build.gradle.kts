plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "io.github.potors.Imaginator"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.potors.Imaginator"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.create("release") {
            storeFile = file("keystore.jks")
                storePassword = System.getenv("APK_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("APK_KEY_ALIAS")
                keyPassword = System.getenv("APK_KEY_PASSWORD")

                val localKeystore = file(System.getProperty("user.home") + "/.android/imaginator.jks")
                if (localKeystore.exists() && localKeystore.isFile) {
                    storeFile = localKeystore
                }
            }
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
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.fragment.ktx)
}
