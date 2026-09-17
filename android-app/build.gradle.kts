import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    jvmToolchain(21)
}

// Release signing is optional: contributors without a release keystore (see
// docs/agents or the setup wizard) still get an unsigned, buildable release
// variant. Only the person publishing to Play Console needs keystore.properties.
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use { load(it) }
    }
}

// Firebase (Issue #35) needs google-services.json, downloaded from the Firebase console,
// placed at android-app/google-services.json. The plugin is applied conditionally so the
// project still builds (without working Firebase Auth/Functions at runtime) for anyone who
// hasn't set this up yet.
val googleServicesFile = file("google-services.json")
if (googleServicesFile.exists()) {
    apply(plugin = "com.google.gms.google-services")
}

// RevenueCat's Google Play public API key is not a secret in the same sense as the Gemini/
// signing keys, but is still project-specific local config kept out of source control (see
// docs/agents or the setup wizard). Falls back to an empty string so the project still
// compiles without it.
val revenueCatPropertiesFile = rootProject.file("revenuecat.properties")
val revenueCatProperties = Properties().apply {
    if (revenueCatPropertiesFile.exists()) {
        revenueCatPropertiesFile.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.smilestudio.android"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.smilestudio.android"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        versionCode = 2
        versionName = "0.2"

        buildConfigField(
            "String",
            "REVENUECAT_API_KEY",
            "\"${revenueCatProperties.getProperty("apiKey", "")}\"",
        )
    }

    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            create("release") {
                storeFile = file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":ui-compose"))
    implementation(project(":vision-recognition"))
    // MaterialExpressiveTheme/MotionScheme.expressive() aren't public yet in the JetBrains
    // Compose Multiplatform material3 artifact (still internal as of composeMultiplatform's
    // bundled 1.12.0-alpha03). android-app is Android-only, so it can depend on the real
    // AndroidX material3 alpha directly instead, where these APIs are public (behind
    // @OptIn(ExperimentalMaterial3ExpressiveApi::class)).
    implementation(libs.androidx.compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.foundation)
    implementation(compose.ui)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.functions)
    implementation(libs.revenuecat.purchases)
    implementation(libs.revenuecat.purchases.ui)
    implementation(libs.kotlinx.coroutines.play.services)
    testImplementation(kotlin("test-junit"))
}
