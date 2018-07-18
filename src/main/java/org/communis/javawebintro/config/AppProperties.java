package org.communis.javawebintro.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "JavaWebIntro", ignoreUnknownFields = false)
@Getter
@Setter
public class AppProperties {

    @NotNull
    private String timezone;


}
