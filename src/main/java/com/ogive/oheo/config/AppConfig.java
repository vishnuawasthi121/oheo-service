package com.ogive.oheo.config;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
/*import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;*/
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class AppConfig implements WebMvcConfigurer  {

	@Value("${app.email.sender.username}")
	private String sender;

	// This is app password not an email id password
	@Value("${app.mail.password}")
	private String appPassword;

	@Value("${mail.smtp.socketFactory.port}")
	private String socketFactoryPort;

	@Value("${mail.smtp.port}")
	private Integer port;

	@Value("${mail.smtp.host}")
	private String host;

	@Value("${mail.smtp.ssl.enable}")
	private boolean sslEnable;

	@Value("${mail.smtp.auth}")
	private boolean auth;

	@Resource
	protected ApplicationContext applicationContext;

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(sender);
		mailSender.setPassword(appPassword);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", socketFactoryPort);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.ssl.enable", sslEnable);
		props.put("mail.debug", "true");
		return mailSender;
	}

	@Bean
	public SpringTemplateEngine springTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(thymeleafTemplateResolver());
		return templateEngine;
	}
	 

	@Bean
	public InternalResourceViewResolver jspViewResolver() {
		final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean()
	@DependsOn(value = "springTemplateEngine")
	public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine springTemplateEngine) {
		final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setViewNames(new String[] { "thyme/*" });
		viewResolver.setExcludedViewNames(new String[] { "jsp/*" });
		viewResolver.setTemplateEngine(springTemplateEngine);
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}

	@Bean
	public SpringResourceTemplateResolver thymeleafTemplateResolver() {
		SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
		emailTemplateResolver.setPrefix("classpath:/templates/");
		emailTemplateResolver.setSuffix(".html");
		emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
		emailTemplateResolver.setCacheable(false);
		emailTemplateResolver.setOrder(0);
		emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		return emailTemplateResolver;
	}

	@Bean
	public SpringResourceTemplateResolver jspTemplateResolver() {
		final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(applicationContext);
		templateResolver.setPrefix("/WEB-INF/views/");
		templateResolver.setSuffix(".jsp");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(false);
		templateResolver.setOrder(1);
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/views/**").addResourceLocations("/views/");
    }
}
