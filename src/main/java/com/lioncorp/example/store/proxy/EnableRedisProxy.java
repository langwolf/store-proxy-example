package com.lioncorp.example.store.proxy;

import com.lioncorp.example.store.proxy.RedisProxyRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
@Import(value = RedisProxyRegistrar.class)
public @interface EnableRedisProxy {
}
