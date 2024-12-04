import org.jetbrains.compose.reload.ComposeHotRun
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag


plugins {
    kotlin("jvm") version "2.1.0-firework.31"
    kotlin("plugin.allopen") version "2.1.0-firework.31"
    kotlin("plugin.compose") version "2.1.0-firework.31" // <- Use special builds of Kotlin/Compose Compiler

    id("org.jetbrains.kotlinx.benchmark") version "0.4.13"
    id("org.jetbrains.compose") version "1.7.1"
    id("org.jetbrains.compose-hot-reload") version "1.0.0-dev.31.1" // <- add this additionally

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven("https://packages.jetbrains.team/maven/p/firework/dev")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.13")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation(compose.desktop.currentOs)
    implementation(compose.foundation)
    implementation(compose.material3)
}

composeCompiler {
    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}

tasks.register<ComposeHotRun>("runHot") {
    mainClass.set("io.sebi.AppKt")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(20)
    compilerOptions {
        freeCompilerArgs.add("-Xwhen-guards")
    }
}

// build.gradle.kts
benchmark {
    targets {
        register("main")
    }
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}