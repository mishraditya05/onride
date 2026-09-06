plugins {
	`java-library`
	`maven-publish`
}

group = "com.onride"
version = "0.2.0"

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

	// Spring's BOM strictly pins opentelemetry-api to 1.62.0. 2.28.1-alpha is the
	// opentelemetry-grpc-1.6 release built against exactly that api version, so it
	// resolves with zero conflict instead of fighting the strict pin.
	implementation("io.opentelemetry.instrumentation:opentelemetry-grpc-1.6:2.28.1-alpha")

	compileOnly("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")
	compileOnly("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
		}
	}
}