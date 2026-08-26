plugins {
	`java-library`
	`maven-publish`
	id("io.github.martinsjavacode.avro-gradle-plugin") version "2.0.0"
}

group = "com.onride"
version = "0.1.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(26)
	}
}

repositories {
	mavenCentral()
}

avro {
	sourceDir.set("src/main/avro")
	outputDir.set("generated-sources/avro")
}

sourceSets {
	main {
		java {
			srcDir(layout.buildDirectory.dir("generated-sources/avro"))
		}
	}
}

tasks.compileJava {
	dependsOn("generateAvroClasses")
}

dependencies {
	api("org.apache.avro:avro:1.12.0")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
		}
	}
}