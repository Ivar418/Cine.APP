@file:OptIn(ExperimentalWasmDsl::class)

import com.android.build.api.dsl.ApplicationExtension
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.hyperetherLocalization)

}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    js {
        browser()
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
            implementation(libs.decompose)
//            implementation(libs.ktor.client.okhttp)

        }
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.datetime)
            implementation(libs.material.icons.extended)

            //Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            //Ktor
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
//            Decompose
            implementation(libs.decompose)
            implementation(compose.materialIconsExtended)
            implementation(libs.decompose.extensions.compose)
            implementation(libs.essentyLifecycleCoroutines)

            //Logger
            implementation("io.github.kdroidfilter:kmplog:0.6.2")
            //Coil
            implementation(libs.bundles.coil)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.cio)
            // Logger


        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.wasm)
        }
//        sourceSets["commonMain"].kotlin.srcDirs(
//            File(
//                layout.buildDirectory.get().asFile.path,
//                "generated/compose/resourceGenerator/kotlin/commonCustomResClass"
//            )
//        )
    }
}
buildkonfig {
    packageName = "com.ivarvisser.cineapp"

// Default values (usually for development)
    defaultConfigs {
        buildConfigField(STRING, "BASE_URL", "acc-cinenetapi.ivarvisser.nl")
//        val apiKey = project.findProperty("API_KEY")?.toString() ?: "fallback_key"
//        buildConfigField(STRING, "API_KEY", "\"$apiKey\"")
    }

// Release specific values
    targetConfigs {
        create("release") {
            buildConfigField(STRING, "BASE_URL", "prod-cinenetapi.ivarvisser.nl")
        }
    }
}
extensions.configure<ApplicationExtension> {
    sourceSets {
        getByName("main") {
            res.directories.add("src/commonMain/composeResources")
        }
    }
    namespace = "com.ivarvisser.cineapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ivarvisser.cineapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.ivarvisser.cineapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.ivarvisser.cineapp"
            packageVersion = "1.0.0"
        }
    }
}
