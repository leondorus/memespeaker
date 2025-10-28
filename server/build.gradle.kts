plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

group = "me.leondorus.memespeaker"
version = "0.0.1"

application {
    mainClass = "$group.MainKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to application.mainClass
        )
    }
}

dependencies {
    implementation(project(":shared"))

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.sqlite.bundled)
}
