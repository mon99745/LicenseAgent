package com.licensecommon.config;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@ConfigurationProperties(prefix = "common.api-doc")
public class SpringDocConfig {

	private String title;
	private String description;
	private String version;
	private String email;
	private String url;

	@Bean
	public OpenAPI openAPI() {
		Contact contact = new Contact()
				.name("License Agent")
				.url("https://www.example.com")
				.email(email);

		Info info = new Info()
				.title(title)
				.version(version)
				.description(description)
				.contact(contact);

		OpenAPI openAPI = new OpenAPI().info(info);

		Optional.ofNullable(url)
				.filter(StringUtils::isNotEmpty)
				.ifPresent(a -> {
					List<Server> servers = Collections.singletonList(new Server().url(a));
					openAPI.servers(servers);
				});

		return openAPI;
	}
}