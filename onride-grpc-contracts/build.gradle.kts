plugins {
	`java-library`
	`maven-publish`
	id("com.google.protobuf") version "0.9.4"
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
	api(platform("io.grpc:grpc-bom:1.63.0"))
	api("io.grpc:grpc-protobuf")
	api("io.grpc:grpc-stub")
	compileOnly("javax.annotation:javax.annotation-api:1.3.2")
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:3.25.3"
	}
	plugins {
		create("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.63.0"
		}
	}
	generateProtoTasks {
		all().forEach {
			it.plugins {
				create("grpc") {}
			}
		}
	}
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			from(components["java"])
		}
	}
}