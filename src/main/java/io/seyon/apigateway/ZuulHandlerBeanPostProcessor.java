package io.seyon.apigateway;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import io.seyon.apigateway.interceptor.AuthorizationInterceptor;

//@Configuration
public class ZuulHandlerBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    @Autowired
    private AuthorizationInterceptor authInterceptor;

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {

        if (bean instanceof ZuulHandlerMapping) {

        	ZuulHandlerMapping zuulHandlerMapping = (ZuulHandlerMapping) bean;
            zuulHandlerMapping.setInterceptors(authInterceptor);
        }

        return super.postProcessAfterInstantiation(bean, beanName);
    }

}