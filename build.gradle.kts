plugins {
    id("java")
    id("idea")
    id("com.matthewprenger.cursegradle") version "1.4.0"
    id("net.neoforged.moddev") version "2.0.136"
    id("eclipse")
}

val minecraftVersion: String = "1.21.1"
val neoForgeVersion: String = "21.1.197"
val modVersion: String = System.getenv("VERSION") ?: "0.0.0-indev"
val jeiVersion: String = "19.21.0.247"
val jadeVersion: String = "7545219"
val topVersion: String = "7292875"
val tfcVersion: String = "7634816"

val modId: String = "dttfc"

base {
    archivesName.set("DynamicTreesTFC-$minecraftVersion")
    group = "org.labellum.mc"
    version = modVersion
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

idea {
    module {
        excludeDirs.add(file("run"))
    }
}


repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://dvs1.progwml6.com/files/maven/") // JEI
    maven(url = "https://modmaven.k-4u.nl") // Mirror for JEI
    maven(url = "https://maven.blamejared.com") // Patchouli
    maven(url = "https://www.cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation("net.neoforged:neoforge:$neoForgeVersion")
    // TFC
    implementation("curse.maven:tfc-302973:${tfcVersion}")

	implementation("curse.maven:dt-252818:7661136")
	implementation("curse.maven:dtplus-478155:7698617")

    // JEI
    compileOnly("mezz.jei:jei-$minecraftVersion-neoforge-api:$jeiVersion")
    compileOnly("mezz.jei:jei-$minecraftVersion-common-api:$jeiVersion")
    runtimeOnly("mezz.jei:jei-$minecraftVersion-neoforge:$jeiVersion")

    // Jade / The One Probe
    compileOnly("curse.maven:jade-324717:${jadeVersion}")
    compileOnly("curse.maven:top-245211:${topVersion}")

    // Only use Jade at runtime
    runtimeOnly("curse.maven:jade-324717:${jadeVersion}")

}

neoForge {
    version = neoForgeVersion
    validateAccessTransformers = true
}

tasks {
    jar {
        manifest {
            attributes["Implementation-Version"] = project.version
            attributes["MixinConfigs"] = "$modId.mixins.json"
        }
    }
}
