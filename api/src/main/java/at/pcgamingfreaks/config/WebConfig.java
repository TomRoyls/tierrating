package at.pcgamingfreaks.config;

import at.pcgamingfreaks.mapper.converter.StringToMediaTypeConverter;
import at.pcgamingfreaks.mapper.converter.StringToMediaSourceConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToMediaSourceConverter());
		registry.addConverter(new StringToMediaTypeConverter());
	}
}
