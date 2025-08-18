plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

group = "me.leondorus"
version = "0.0.1"

kotlin {
    jvm()
    jvmToolchain(21)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.io.mockk)
            }
        }
        val jvmMain by getting {
        }
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

//tasks.verification.allTests {
//    jvmArgs = listOf("-XX:+EnableDynamicAgentLoading")
//}