plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    jvmToolchain(21)
    jvm()

    android {
        namespace = "com.smilestudio.vision"
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koog.agents)
            implementation(libs.koog.prompt.executor.google.client)
            implementation(libs.koog.http.client.ktor)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.koog.agents.test)
        }
    }
}
