package com.easygest.springboot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootApplication {

	public static void main(String[] args) {
		loadLocalEnvironment();
		validateMongoUri();
		SpringApplication.run(SpringbootApplication.class, args);
	}

	private static void loadLocalEnvironment() {
		Path envFile = Path.of(System.getProperty("user.dir", ".")).resolve(".env.local");
		if (!Files.isRegularFile(envFile)) {
			return;
		}

		try {
			List<String> lines = Files.readAllLines(envFile, StandardCharsets.UTF_8);
			for (String line : lines) {
				String trimmed = line.trim();
				if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains("=")) {
					continue;
				}

				String[] entry = trimmed.split("=", 2);
				String key = entry[0].trim();
				String value = entry[1].trim();
				if (!key.isEmpty() && value.length() >= 2
						&& ((value.startsWith("\"") && value.endsWith("\""))
								|| (value.startsWith("'") && value.endsWith("'")))) {
					value = value.substring(1, value.length() - 1);
				}

				if (System.getenv(key) == null && System.getProperty(key) == null) {
					System.setProperty(key, value);
				}
			}
		} catch (IOException exception) {
			throw new IllegalStateException("Não foi possível ler o arquivo local de ambiente.", exception);
		}
	}

	private static void validateMongoUri() {
		String uri = System.getenv("SPRING_MONGODB_URI");
		if (uri == null || uri.isBlank()) {
			uri = System.getProperty("SPRING_MONGODB_URI");
		}

		if (uri == null || uri.isBlank()) {
			throw new IllegalStateException(
					"SPRING_MONGODB_URI não está configurada. Configure-a no ambiente ou no arquivo local .env.local.");
		}

		String trimmed = uri.trim();
		if (!trimmed.startsWith("mongodb://") && !trimmed.startsWith("mongodb+srv://")) {
			throw new IllegalStateException(
					"SPRING_MONGODB_URI está inválida. A URI deve começar com mongodb:// ou mongodb+srv://.");
		}
	}

}
