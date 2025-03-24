package com.example.perpusapi;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        // ======= Providers dan Resources =======
        //Providers
        resources.add(com.example.perpusapi.middleware.AuthFilter.class);

        //Resources
        resources.add(com.example.perpusapi.HelloResource.class);
        resources.add(com.example.perpusapi.resource.AuthResource.class);
        resources.add(com.example.perpusapi.resource.AccountResource.class);
        resources.add(com.example.perpusapi.resource.DatabaseResource.class);
        resources.add(com.example.perpusapi.config.JacksonConfig.class);
        // ======= Providers dan Resources =======
        return resources;
    }
}