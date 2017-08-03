package me.anilkc.hacknight.alexa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

import me.anilkc.hacknight.alexa.speechlet.HackNightSpeechlet;

@Configuration
public class AlexaConfig {

  @Value("classpath:alexa.properties")
  private Resource alexaPropsResource;

  @Autowired
  private HackNightSpeechlet hackNightSpeechlet;

  @Bean
  public ServletRegistrationBean alexaNotes() throws IOException {
    final Properties props = new Properties(System.getProperties());
    try (InputStream propsIn = alexaPropsResource.getInputStream()) {
      props.load(propsIn);
      System.setProperties(props);
    }

    SpeechletServlet servlet;
    servlet = new SpeechletServlet();
    servlet.setSpeechlet(hackNightSpeechlet);

    final ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "/alexaHackNight");
    return servletRegistrationBean;
  }
}
