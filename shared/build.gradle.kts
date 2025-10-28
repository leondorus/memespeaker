plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm()
    jvmToolchain(21)

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines)

            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.io.mockk)
        }
        jvmMain {}
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
    add("kspJvmTest", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}