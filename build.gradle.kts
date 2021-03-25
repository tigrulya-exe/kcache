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
	version = "0.0.1-SNAPSHOT"
	configure<JavaPluginExtension> {
		sourceCompatibility = JavaVersion.VERSION_11
	}

	repositories {
		mavenCentral()
		google()
	}

	configure<PublishingExtension> {
		repositories {
			maven {
				name = "GitHubPackages"
				url = uri("https://maven.pkg.github.com/tigrulya-exe/kcache")
				credentials {
					username = System.getenv("GITHUB_ACTOR")
					password = System.getenv("GITHUB_TOKEN")
				}
			}
		}
	}
}
