plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    `maven-publish`
}

group = "net.weavemc.internals"
version = "1.0.0"

kotlin {
    jvmToolchain(8)
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api("org.ow2.asm:asm:9.4")
    api("org.ow2.asm:asm-commons:9.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("io.github.770grappenmaker:mappings-util:0.1.4")
}

publishing {
    repositories {
        maven {
            name = "WeaveMC"
            url = uri("https://repo.weavemc.dev/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "net.weavemc"
            artifactId = "internals"
            version = project.version as String + "-PRE2"
        }
    }
}