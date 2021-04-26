plugins {
    id("org.springframework.boot") version "2.4.0"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("java")
    kotlin("plugin.spring") version "1.4.10"
    kotlin("plugin.allopen") version "1.3.61"
    kotlin("plugin.jpa") version "1.4.10"
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.redisson:redisson:3.14.1")
    implementation("com.google.code.gson:gson:2.8.6")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("mysql:mysql-connector-java")

    implementation(project(":library"))

    implementation("org.flywaydb:flyway-core")

    testImplementation("com.natpryce:hamkrest:1.8.0.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest:kotest-assertions-core:4.0.7")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks {
    test {
        useJUnitPlatform()
    }

    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }

    publishMavenKotlinPublicationToJitPackRepository {
        this.enabled = false
    }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

