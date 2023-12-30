plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    api("io.github.770grappenmaker:mappings-util:0.1.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
}

group = "net.weavemc"
version = "0.1"

kotlin {
    jvmToolchain(8)
}

java {
    withSourcesJar()
}

dependencies {
    api("org.ow2.asm:asm:9.6")
    api("org.ow2.asm:asm-commons:9.6")
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
            artifactId = "util"
            version = project.version as String
        }
    }
}