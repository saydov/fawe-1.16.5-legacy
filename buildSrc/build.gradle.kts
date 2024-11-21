import java.util.Properties

plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
}

repositories {
    gradlePluginPortal()
    maven {
        name = "EngineHub"
        url = uri("https://maven.enginehub.org/repo/")
    }
}

val properties = Properties().also { props ->
    project.projectDir.resolveSibling("gradle.properties").bufferedReader().use {
        props.load(it)
    }
}

dependencies {
    implementation(gradleApi())
    implementation("com.github.johnrengelman:shadow:8.1.1")

    constraints {
        val asmVersion = "[9.7,)"
        implementation("org.ow2.asm:asm:$asmVersion") {
            because("Need Java 17 support in shadow")
        }
        implementation("org.ow2.asm:asm-commons:") {
            because("Need Java 17 support in shadow")
        }
        implementation("org.vafer:jdependency:[2.10,)") {
            because("Need Java 17 support in shadow")
        }
    }
}
