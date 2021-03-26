plugins {
	kotlin("jvm") version "1.4.10" apply false
	`maven-publish`
}

allprojects {
	apply {
		plugin("org.jetbrains.kotlin.jvm")
		plugin("maven-publish")
	}

	group = "ru.nsu.manasyan"
	version = "0.1.0"
	configure<JavaPluginExtension> {
		sourceCompatibility = JavaVersion.VERSION_1_8
	}

	configure<PublishingExtension> {
		repositories {
			maven {
				name = "JitPack"
				url = uri("https://jitpack.io")
			}
		}

		publications {
			create<MavenPublication>("mavenKotlin") {
				from(components["kotlin"])
			}
		}
	}

	repositories {
		mavenCentral()
		google()
	}
}
