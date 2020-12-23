plugins {
	kotlin("jvm") version "1.4.10" apply false
}

allprojects {
	apply {
		plugin("org.jetbrains.kotlin.jvm")
	}

	group = "ru.nsu.manasyan"
	version = "0.0.1-SNAPSHOT"
	configure<JavaPluginExtension> {
		sourceCompatibility = JavaVersion.VERSION_11
	}

	repositories {
		mavenCentral()
	}
}
