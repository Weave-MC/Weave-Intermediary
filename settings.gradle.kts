rootProject.name = "util"

pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.22"
        kotlin("plugin.serialization") version "1.9.22"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}