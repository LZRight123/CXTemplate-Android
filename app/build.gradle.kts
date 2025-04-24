import com.android.build.api.dsl.ApplicationBuildType
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
    kotlin("plugin.serialization") version "2.1.0"
//    alias(libs.plugins.google.services)
}

android {
    namespace = "com.fantasy.cctemplate"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fantasy.cctemplate"
        minSdk = 29
        targetSdk = 35
        versionCode = getVersionCode()
        versionName = "1.0.0"

        manifestPlaceholders += mapOf(
            "app_logo" to "@mipmap/ic_launcher",
            "app_name" to "@string/app_name",
            "network_security_xml" to "@xml/network_security_config"
        )
    }

    signingConfigs {
        create("universal") {
            storeFile = file("template.keystore")
            keyAlias = "tmp"
            keyPassword = "123456"
            storePassword = "123456"
            enableV1Signing = true
            enableV2Signing = true
        }
    }


    buildTypes {
        val commonConfig: ApplicationBuildType.() -> Unit = {
            signingConfig = signingConfigs["universal"]
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            commonConfig()
        }
        release {
            commonConfig()
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
        buildConfig = true
    }

    // 多渠道包
//    flavorDimensions += "channel"
//    productFlavors {
//        // 提取公共配置函数，用于创建不同渠道的 flavor
//        fun createChannelFlavor(flavorName: String) {
//            create(flavorName) {
//                dimension = "channel"
//                manifestPlaceholders["CHANNEL_VALUE"] = flavorName
//            }
//        }
//
//        createChannelFlavor("universal")
//        createChannelFlavor("google")
//        createChannelFlavor("china")
//    }

    androidComponents {
        applicationVariants.all {
            val variant = this
            outputs.forEach { output ->
                val output = output as com.android.build.gradle.internal.api.BaseVariantOutputImpl
                // 方式1：版本号_构建类型
                output.outputFileName =
                    "v${defaultConfig.versionName}_${defaultConfig.versionCode}.apk"
            }
        }
    }

    // 添加 lint 配置，禁用可能导致问题的检查器
    lint {
        disable += listOf(
            "AccidentalOctal", "AndroidGradlePluginVersion", "AnnotationProcessorOnCompilePath",
            "BomWithoutPlatform", "UseOfBundledGooglePlayServices", "ChromeOsAbiSupport",
            "GradleCompatible", "DataBindingWithoutKapt", "GradleDependency", "GradleDeprecated",
            "GradleDeprecatedConfiguration", "OutdatedLibrary", "DevModeObsolete",
            "DuplicatePlatformClasses", "EditedTargetSdkVersion", "ExpiredTargetSdkVersion",
            "ExpiringTargetSdkVersion", "GradleGetter", "GradlePluginVersion", "HighAppVersionCode",
            "GradleIdeError", "JavaPluginLanguageLevel", "JcenterRepositoryObsolete",
            "KaptUsageInsteadOfKsp", "KtxExtensionAvailable", "LifecycleAnnotationProcessorWithJava8",
            "MinSdkTooLow", "SimilarGradleDependency", "NotInterpolated", "GradlePath",
            "PlaySdkIndexDeprecated", "PlaySdkIndexGenericIssues", "PlaySdkIndexNonCompliant",
            "PlaySdkIndexVulnerability", "GradleDynamicVersion", "NewerVersionAvailable",
            "RiskyLibrary", "StringShouldBeInt", "UseTomlInstead", "OldTargetApi"
        )
    }
}

dependencies {
    /**
     * https://developer.android.com/jetpack/compose/bom/bom-mapping
     * 借助 Compose 物料清单 (BoM)，您只需指定 BoM 的版本，即可管理所有 Compose 库版本。
     * BoM 本身包含指向不同 Compose 库的稳定版的链接，以便它们能够很好地协同工作。
     * 在应用中使用 BoM 时，您无需向 Compose 库依赖项本身添加任何版本。
     * 更新 BoM 版本时，您使用的所有库都会自动更新到新版本。
     */
    implementation(platform(libs.androidx.compose.bom))
    /**
     * Compose 与 Kotlin 的兼容性对应关系 https://developer.android.com/jetpack/androidx/releases/compose-kotlin?hl=zh-cn
     */
    // Compose https://developer.android.com/jetpack/compose/interop
    implementation(libs.androidx.ui)
    implementation(libs.androidx.animation.android)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.google.id)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.billing.client)
    /**
     * 三方
     */
    implementation(platform(libs.supabase.bom))
    implementation(libs.bundles.cx.recommend)
    implementation(libs.bundles.cx.kmp)
}


tasks.register<Exec>("uploadDebug") {
    commandLine("sh", "uploadApk", "debug")
}

tasks.register<Exec>("uploadRelease") {
    commandLine("sh", "uploadApk", "release")
}

tasks.register("buildMyDebug") {
    dependsOn("assembleDebug")
    // 上传蒲公英和华为云
    finalizedBy("uploadDebug")
}

tasks.register("buildMyRelease") {
    dependsOn("assembleRelease")
    // 上传蒲公英和华为云
    finalizedBy("uploadRelease")
}

// 设置 versionCode 自增长
fun getVersionCode(): Int {
    val versionFile = file("version.properties")
    if (versionFile.canRead()) {
        val properties = Properties()
        properties.load(FileInputStream(versionFile))
        val versionCode =
            properties["VERSION_CODE"].toString().toInt() // 读取 version.properties 文件存放的版本号
        val runTasks = gradle.startParameter.taskNames

        if (runTasks.contains("buildMyRelease")) {
            // 仅在每次打 debug 包或 release 包的时候增加版本号，普通运行下无法自增长
            properties["VERSION_CODE"] = (versionCode + 1).toString()
            properties.store(versionFile.writer(), null)
            return versionCode + 1
        }
        return versionCode
    } else {
        throw GradleException("Could not find version.properties!")
    }
}