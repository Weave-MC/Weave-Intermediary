package net.weavemc.internals

import kotlin.properties.Delegates
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

enum class MinecraftVersion(
    val protocol: Int,
    val versionName: String,
    val mappingName: String,
    vararg val aliases: String,
) {
    V1_7_10(5, "1.7.10", "1.7", "1.7"),
    V1_8_9(47, "1.8.9", "1.8", "1.8"),
    V1_12_2(340, "1.12.2", "1.12", "1.12"),
    V1_16_5(754, "1.16.5", "1.16", "1.16"),
    V1_20_1(763, "1.20.1", "1.20","1.20");

    companion object {
        fun fromProtocol(protocol: Int): MinecraftVersion? = entries.find { it.protocol == protocol }
        fun fromVersionName(versionName: String): MinecraftVersion? =
            entries.find { versionName.contains(it.versionName) }
                ?: entries.find { it.aliases.any { alias -> versionName.contains(alias) } }

        fun fromAlias(alias: String): MinecraftVersion? = entries.find { it.aliases.contains(alias) }
    }
}

enum class MinecraftClient(
    val clientName: String,
    vararg val aliases: String,
) {
    VANILLA("Vanilla"),
    FORGE("Forge", "MinecraftForge", "Minecraft Forge"),
    LABYMOD("LabyMod", "Laby"),
    LUNAR("Lunar Client", "Lunar", "LunarClient"),
    BADLION("Badlion Client", "BLC", "Badlion", "BadlionClient");

    companion object {
        fun fromClientName(clientName: String): MinecraftClient? =
            entries.find { it.clientName.equals(clientName, ignoreCase = true) }
                ?: entries.find { it.aliases.any { alias -> alias.equals(clientName, ignoreCase = true) } }
    }
}

object GameInfo {
    enum class MinecraftLauncher {
        OTHER,
        MULTIMC,
        PRISM,
    }

    val args: Map<String, String>
        get() = System.getProperties()["weave.main.args"] as? Map<String, String>
            ?: error("Failed to retrieve Minecraft arguments")

    val sunArgs = System.getProperty("sun.java.command")
        ?: error("Failed to retrieve command line arguments, this should never happen.")

    private val versionString: String by lazy {
        args["version"]?.lowercase() ?: error("Could not parse version from arguments")
    }

    val gameVersion: MinecraftVersion by lazy {
        versionString.let(MinecraftVersion::fromVersionName) ?: error("Could not find game version")
    }

    val gameClient: MinecraftClient by lazy {
        when {
            classExists("com.moonsworth.lunar.genesis.Genesis") -> MinecraftClient.LUNAR
            classExists("net.minecraftforge.fml.common.Loader") -> MinecraftClient.FORGE
//            versionString.contains("forge") -> MinecraftClient.FORGE
            versionString.contains("labymod") -> MinecraftClient.LABYMOD
            else -> MinecraftClient.VANILLA
        }
    }

    val gameLauncher: MinecraftLauncher by lazy {
        when {
            classExists("org.multimc.EntryPoint") -> MinecraftLauncher.MULTIMC
            classExists("org.prismlauncher.EntryPoint") -> MinecraftLauncher.PRISM
            else -> MinecraftLauncher.OTHER
        }
    }

    private fun classExists(name: String): Boolean =
        GameInfo::class.java.classLoader.getResourceAsStream("${name.replace('.', '/')}.class") != null
}

enum class MappingsType(val id: String) {
    MOJANG("mojmap"),
    MCP("mcp"),
    YARN("yarn"),
    MERGED("merged");

    companion object {
        /**
         * Converts the mapping String into a [MappingsType] enum.
         *
         * @param mappings The mappings String.
         * @return The [MappingsType] corresponding [id].
         * @throws IllegalArgumentException If there is no mappings which corresponds with the [id].
         */
        @JvmStatic
        fun fromString(mappings: String) =
            enumValues<MappingsType>().find { it.id == mappings } ?: error("No such mappings: $mappings")
    }

    private fun resolvingProperty() = ReadOnlyProperty<MappingsType, _> { _, prop -> resolve(prop.name) }
    val named by resolvingProperty()
    val srg by resolvingProperty()
    val obf by resolvingProperty()
    val intermediary by resolvingProperty()

    fun resolve(type: String) = "$id-$type"
}