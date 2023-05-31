package com.redhat.zgrinber;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Startup
@ApplicationScoped
@Path("/hello")
public class RestResource {

    private static final Logger LOG = Logger.getLogger(RestResource.class);
    @Inject
    @Named("bean1")
    private BeanTemplate bean1;

    @Inject
    @Named("bean2")
    private BeanTemplate bean2;

    @Inject
    @Named("bean3")
    private BeanTemplate bean3;

    @Inject
    @Named("bean4")
    private BeanTemplate bean4;


    @Inject
    @Named("bean5")
    private BeanTemplate bean5;

    @Inject
    @Named("bean6")
    private BeanTemplate bean6;

    @Inject
    @Named("bean7")
    private BeanTemplate bean7;

    @Inject
    @Named("bean8")
    private BeanTemplate bean8;


    @Inject
    @Named("bean9")
    private BeanTemplate bean9;

    @Inject
    @Named("bean10")
    private BeanTemplate bean10;

    @Inject
    @Named("bean11")
    private BeanTemplate bean11;

    @Inject
    @Named("bean12")
    private BeanTemplate bean12;


    @Inject
    @Named("bean13")
    private BeanTemplate bean13;

    @Inject
    @Named("bean14")
    private BeanTemplate bean14;

    @Inject
    @Named("bean15")
    private BeanTemplate bean15;

    @Inject
    @Named("bean16")
    private BeanTemplate bean16;



    @GET
    @Path("/{bean}")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@PathParam("bean") String beanName) {
        Method handleLogic;
        try {
            Field field = Arrays.stream(this.getClass().getDeclaredFields()).toList().stream().filter(f-> f.getName().equals(beanName)).collect(Collectors.toList()).get(0);
            ((BeanTemplate)field.get(this)).handleLogic();

        }  catch (IllegalAccessException e) {
            return returnError(beanName);
        }
        return "say hello and invoked bean with name=" + beanName;
    }

    private static String returnError(String beanName) {
        return "couldn't found the bean you wanted, bean=" + beanName + " doesn't exists";
    }

    @PostConstruct
    public void doSomethingWhenCreated()
    {
        LOG.info("Created Rest Resource Bean Eagerly And Injected all Bean1...Bean16");
    }
}
