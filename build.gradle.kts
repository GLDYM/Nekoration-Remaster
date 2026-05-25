import java.time.Instant

plugins {
    eclipse
    idea
    `java-library`
    `maven-publish`
    id("net.neoforged.moddev.legacyforge") version "2.0.91"
}

group = providers.gradleProperty("mod_group_id").get()
version = providers.gradleProperty("mod_version").get()

base {
    archivesName.set(providers.gradleProperty("mod_id").get())
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

legacyForge {
    val minecraftVersion = providers.gradleProperty("minecraft_version").get()
    val forgeVersion = providers.gradleProperty("forge_version").get()
    version = "$minecraftVersion-$forgeVersion"

    accessTransformers.from("src/main/resources/META-INF/accesstransformer.cfg")
    validateAccessTransformers = false

    runs {
        val modId = providers.gradleProperty("mod_id").get()

        create("client") {
            client()
            systemProperty("forge.enabledGameTestNamespaces", modId)
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("forge.enabledGameTestNamespaces", modId)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            systemProperty("forge.enabledGameTestNamespaces", modId)
        }

        create("data") {
            data()
            programArguments.addAll(
                "--mod", modId, "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath,
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        val modId = providers.gradleProperty("mod_id").get()
        create(modId) { sourceSet(sourceSets.main.get()) }
    }
}

sourceSets {
    named("main") {
        resources.srcDir("src/generated/resources")
    }
}

repositories {
    maven {
        name = "Progwml6's maven"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        name = "ModMaven"
        url = uri("https://modmaven.dev")
    }
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks.named<ProcessResources>("processResources") {
    val replaceProperties = mapOf(
        "minecraft_version" to providers.gradleProperty("minecraft_version").get(),
        "minecraft_version_range" to providers.gradleProperty("minecraft_version_range").get(),
        "forge_version" to providers.gradleProperty("forge_version").get(),
        "forge_version_range" to providers.gradleProperty("forge_version_range").get(),
        "loader_version_range" to providers.gradleProperty("loader_version_range").get(),
        "mod_id" to providers.gradleProperty("mod_id").get(),
        "mod_name" to providers.gradleProperty("mod_name").get(),
        "mod_license" to providers.gradleProperty("mod_license").get(),
        "mod_version" to providers.gradleProperty("mod_version").get(),
        "mod_authors" to providers.gradleProperty("mod_authors").get(),
        "mod_description" to providers.gradleProperty("mod_description").get(),
    )

    inputs.properties(replaceProperties)
    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) { expand(replaceProperties) }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to providers.gradleProperty("mod_id").get(),
                "Specification-Vendor" to providers.gradleProperty("mod_authors").get(),
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to providers.gradleProperty("mod_authors").get(),
                "Implementation-Timestamp" to Instant.now().toString(),
            )
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(17)
}
