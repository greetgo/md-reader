package kz.greetgo.md_reader.core;

import java.util.Map;
import lombok.extern.java.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Log
@Component
public class EndpointsListener implements ApplicationListener<ContextRefreshedEvent> {
  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    ApplicationContext                     applicationContext = event.getApplicationContext();
    Map<RequestMappingInfo, HandlerMethod> handlerMethods     = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
    for (final Map.Entry<RequestMappingInfo, HandlerMethod> e : handlerMethods.entrySet()) {

      RequestMappingInfo mapping       = e.getKey();
//      HandlerMethod      handlerMethod = e.getValue();

      log.info(() -> "9njEDVSdjQ :: " + mapping);
    }
  }
}
