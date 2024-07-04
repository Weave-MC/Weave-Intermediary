plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
}

group = "net.weavemc.internals"
version = "1.0.0-b.3"

kotlin {
    jvmToolchain(8)
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    api(libs.bundles.asm)
    implementation(libs.serialization.json)
    implementation(libs.mappings)
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
            version = project.version.toString()
        }
    }
}