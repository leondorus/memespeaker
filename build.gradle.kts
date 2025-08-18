plugins {
    kotlin("jvm") version "2.1.20"
    alias(libs.plugins.ksp)
}

group = "me.leondorus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.coroutines)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.io.mockk)
}

tasks.test {
    jvmArgs = listOf("-XX:+EnableDynamicAgentLoading")
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}