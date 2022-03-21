plugins {
	`java-library-conventions`
	`java-multi-release-sources`
	`java-repackage-jars`
	`java-test-fixtures`
}

description = "JUnit Platform Commons"

dependencies {
	api(platform(projects.junitBom))

	compileOnlyApi(libs.apiguardian)
}

//def fingerprinter = services.get(org.gradle.internal.fingerprint.classpath.ClasspathFingerprinter)

tasks.jar {
	val release9ClassesDir = sourceSets.mainRelease9.get().output.classesDirs.singleFile

	inputs.dir(release9ClassesDir)
        .withPropertyName("release9ClassesDir")
        .withPathSensitivity(PathSensitivity.RELATIVE)

	doLast {
        rootSpec.eachFile {
            println("${this.name} copied to ${this.path}")
        }

		exec {
			executable = project.the<JavaToolchainService>().launcherFor(java.toolchain).get()
				.metadata.installationPath.file("bin/jar").asFile.absolutePath
			args(
				"--update",
				"--file", archiveFile.get().asFile.absolutePath,
				"--release", "9",
				"-C", release9ClassesDir.absolutePath, "."
			)
		}
	}
}

eclipse {
	classpath {
		sourceSets -= project.sourceSets.mainRelease9.get()
	}
}
