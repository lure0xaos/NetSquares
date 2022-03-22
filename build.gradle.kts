val javaVersion: String = JavaVersion.VERSION_16.toString()

val appMainClass: String = "gargoyle.netsquares.NetSquares"
val appModule: String = "NetSquares"
val appName: String = "NetSquares"

val os: org.gradle.internal.os.OperatingSystem = org.gradle.internal.os.OperatingSystem.current()
val appInstallerType: String = "msi"
val appIconIco: String = "src/main/resources/gargoyle/netsquares/res/ns.ico"
val appIconPng: String = "src/main/resources/gargoyle/netsquares/res/ns.gif"
val appCopyright: String = "Lure of Chaos"
val appVendor: String = "Lure of Chaos"


group = "gargoyle.netsquares"
version = "1.0"
description = "NetSquares"

plugins {
    java
    application
    kotlin("jvm") version "1.6.10"

    id("org.javamodularity.moduleplugin") version ("1.8.10")
    id("org.openjfx.javafxplugin") version ("0.0.12")
    id("org.beryx.jlink") version ("2.25.0")
}

repositories {
    mavenCentral()
}

dependencies {
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    }
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.jetbrains:annotations:23.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.mockito:mockito-core:4.3.1")
}

tasks.compileJava {
    modularity.inferModulePath.set(true)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    options.encoding = Charsets.UTF_8.toString()
}

tasks.compileKotlin {
    destinationDirectory.set(tasks.compileJava.get().destinationDirectory)
    targetCompatibility = javaVersion
    kotlinOptions {
        jvmTarget = javaVersion
    }
}

tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = javaVersion
    }
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.build {
    dependsOn += tasks.jpackage
}

tasks.startScripts {
    (unixStartScriptGenerator as org.gradle.api.internal.plugins.DefaultTemplateBasedStartScriptGenerator).template =
        resources.text.fromFile("unixStartScript.txt")
    (windowsStartScriptGenerator as org.gradle.api.internal.plugins.DefaultTemplateBasedStartScriptGenerator).template =
        resources.text.fromFile("windowsStartScript.txt")
}

application {
    mainClass.set(appMainClass)
    mainModule.set(appModule)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.8.2")
        }
    }
}


jlink {
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = appName
        mainClass.set(appMainClass)
    }
    jpackage {
        installerType = appInstallerType
        installerName = appName
        appVersion = project.version.toString()
        if (os.isWindows) {
            icon = rootProject.file(appIconIco).path
            installerOptions = listOf(
                "--description", rootProject.description,
                "--copyright", appCopyright,
                "--vendor", appVendor,
                "--win-dir-chooser",
                "--win-menu",
                "--win-per-user-install",
                "--win-shortcut"
            )
        }
        if (os.isLinux) {
            icon = rootProject.file(appIconPng).path
            installerOptions = listOf(
                "--description", rootProject.description,
                "--copyright", appCopyright,
                "--vendor", appVendor,
                "--linux-shortcut"
            )
        }
        if (os.isMacOsX) {
            icon = rootProject.file(appIconPng).path
        }
    }
}
