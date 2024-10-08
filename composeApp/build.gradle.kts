import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.js.backend.ast.JsImport

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                outputPath = file("${layout.buildDirectory}/dist")
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(projectDirPath)
                    }
                }
                cssSupport{
                    enabled = true
                }
            }
        }
        binaries.executable()
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting

        val commonMain by getting {
            resources.srcDir("src/commonMain/composeResources")
        }
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "calculate.mortgage.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "calculate.mortgage"
            packageVersion = "1.0.0"
        }
    }
}

tasks.register("bundleForWeb") {
    dependsOn("wasmJsBrowserProductionWebpack")
    doLast {
        copy {
            from("${layout.buildDirectory}/distributions") // The default output for the Webpack task
            into("$${layout.buildDirectory}/dist")          // Move the generated files to a 'dist' folder
        }
    }
}
