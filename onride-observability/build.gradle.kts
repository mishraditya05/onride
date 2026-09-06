plugins {
	`java-library`
	`maven-publish`
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

dependencies {
	api(platform("org.springframework.boot:spring-boot-dependencies:4.1.1"))

	api("org.springframework.boot:spring-boot-starter-opentelemetry")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
		}
	}
}