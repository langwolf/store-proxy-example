package com.lioncorp.example.store.proxy;

import com.lioncorp.example.store.proxy.config.RedisConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Objects;

public class RedisProxyRegistrar implements ImportBeanDefinitionRegistrar {

    private static final Logger logger = LoggerFactory.getLogger(RedisProxyRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(RedisConfig.class.getName())) {
            String[] candidates = registry.getBeanDefinitionNames();
            if(ArrayUtils.isNotEmpty(candidates)) {
                for (String candidate : candidates) {
                    BeanDefinition beanDefinition = registry.getBeanDefinition(candidate);
                    if (Objects.equals(beanDefinition.getBeanClassName(), RedisConfig.class.getName())) {
                        return;
                    }
                }
            }
            BeanDefinition annotationProcessor = BeanDefinitionBuilder.genericBeanDefinition(RedisConfig.class).getBeanDefinition();
            registry.registerBeanDefinition(RedisConfig.class.getName(), annotationProcessor);
            return;
        }
    }
}
