@file:OptIn(ExperimentalWasmDsl::class)

import com.android.build.api.dsl.ApplicationExtension
import com.codingfeline.buildkonfig.compiler.FieldSpec
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


    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
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
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.work.runtime.ktx)
        }
        commonMain.dependencies {
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
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            //Decompose
            implementation(libs.decompose)
            implementation(compose.materialIconsExtended)
            implementation(libs.decompose.extensions.compose)
            implementation(libs.essentyLifecycleCoroutines)

            //Logger
            implementation(libs.klf)
            //Coil
            implementation(libs.bundles.coil)
            //Webvieuw MultiPlatofrm
            implementation(libs.multiplatform.settings.no.arg)

            //Notifications
            api("io.github.mirzemehdi:kmpnotifier-local:2.0.0")

        }
        commonTest.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.kotlin.test)
            implementation(libs.koin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.turbine)
        }

        val androidInstrumentedTest by sourceSets.getting {
            dependsOn(commonTest.get())
        }
        sourceSets["desktopMain"].dependencies {
            implementation(libs.slf4j.simple)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.cio)
            implementation("io.github.kevinnzou:compose-webview-multiplatform-desktop:2.0.3")
        }


        wasmJsMain.dependencies {
            implementation(libs.slf4j.simple)
            implementation(libs.ktor.client.wasm)
        }

    }
}
buildkonfig {
    packageName = "com.ivarvisser.cineapp"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.BOOLEAN, "IS_DEBUG", "true")
        buildConfigField(STRING, "BASE_URL", "acc-cinenetapi.ivarvisser.nl")
        buildConfigField(STRING, "PROTOCOL", "HTTPS")
    }

    targetConfigs {
        create("staging") {
            buildConfigField(FieldSpec.Type.BOOLEAN, "IS_DEBUG", "true")
            buildConfigField(STRING, "BASE_URL", "acc-cinenetapi.ivarvisser.nl")
            buildConfigField(STRING, "PROTOCOL", "HTTPS")
        }
        create("release") {
            buildConfigField(FieldSpec.Type.BOOLEAN, "IS_DEBUG", "false")
            buildConfigField(STRING, "BASE_URL", "prod-cinenetapi.ivarvisser.nl")
            buildConfigField(STRING, "PROTOCOL", "HTTPS")
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
    flavorDimensions.add("environment")
    productFlavors {
        create("development") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
        }
        create("production") {
            dimension = "environment"
        }
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
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
    androidTestImplementation(libs.compose.uiTest.junit4)
    debugImplementation(libs.compose.uiTest.manifest)
}

compose.desktop {
    application {
        mainClass = "com.ivarvisser.cineapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.ivarvisser.cineapp"
            packageVersion = "1.0.0"
        }
        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs(
            "--add-opens",
            "java.desktop/java.awt.peer=ALL-UNNAMED"
        ) // recommended but not necessary

        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }
    }
}
