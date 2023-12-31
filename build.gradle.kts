plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    `maven-publish`
}

group = "net.weavemc.intermediary"
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
    implementation("com.grappenmaker:mappings-util:0.2.0")
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
            artifactId = "intermediary"
            version = project.version as String
        }
    }
}