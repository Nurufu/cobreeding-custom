plugins {
    id("cobbreeding.platform-conventions")
}

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    forge {
        mixinConfig("cobbreeding.mixins.json")
        mixinConfig("cobbreeding-forge.mixins.json")
    }
}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
    "developmentForge"(project(":common", configuration = "namedElements")) { isTransitive = false }
    bundle(project(":common", configuration = "transformProductionForge")) { isTransitive = false }

    modApi(libs.architectury.forge)

    forge(libs.forge.loader)
    implementation(libs.forge.kotlin)

    modImplementation(libs.cobblemon.forge)
}

tasks {
    processResources {
        val forgeVersion = libs.forge.loader.get().version?.split('-')?.get(1)?.split('.')?.get(0)
        val forgeKotlinVersion = libs.forge.kotlin.get().version?.split('.')?.get(0)
        val architecturyVersion = libs.architectury.forge.get().version?.split('.')?.get(0)
        val cobblemonVersion = libs.cobblemon.forge.get().version?.split('+')?.get(0)

        inputs.property("minecraft_version", libs.minecraft.get().version)
        inputs.property("loader_version", forgeVersion)
        inputs.property("forge_kotlin_version", forgeKotlinVersion)
        inputs.property("architectury_version", architecturyVersion)
        inputs.property("cobblemon_version", cobblemonVersion)
        inputs.property("version", rootProject.version)

        filesMatching("META-INF/mods.toml") {
            expand(
                "version" to rootProject.version,
                "architectury_version" to architecturyVersion,
                "forge_kotlin_version" to forgeKotlinVersion,
                "loader_version" to forgeVersion,
                "minecraft_version" to libs.minecraft.get().version,
                "cobblemon_version" to cobblemonVersion,
            )
        }
    }
}
