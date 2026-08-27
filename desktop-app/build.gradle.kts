plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":ui-compose"))
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
