package com.redhat.zgrinber;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
//import jakarta.inject.Singleton;

public class BeansDefinitions {

@Produces
@ApplicationScoped
@Named("bean1")
public BeanTemplate bean1()
{
    return new BeanTemplate("bean1","@ApplicationScoped",100);
}

@Produces
@ApplicationScoped
@Named("bean2")
public BeanTemplate bean2()
{
    return new BeanTemplate("bean2","@ApplicationScoped",200);
}

@Produces
@ApplicationScoped
@Named("bean3")
public BeanTemplate bean3()
{
    return new BeanTemplate("bean3","@ApplicationScoped",300);
}

@Produces
@ApplicationScoped
@Named("bean4")
public BeanTemplate bean4()
{
    return new BeanTemplate("bean4","@ApplicationScoped",400);
}

@Produces
@ApplicationScoped
@Named("bean5")
public BeanTemplate bean5()
{
    return new BeanTemplate("bean5","@ApplicationScoped",500);
}

@Produces
@ApplicationScoped
@Named("bean6")
public BeanTemplate bean6()
{
    return new BeanTemplate("bean6","@ApplicationScoped",600);
}

@Produces
@ApplicationScoped
@Named("bean7")
public BeanTemplate bean7()
{
    return new BeanTemplate("bean7","@ApplicationScoped",700);
}

@Produces
@ApplicationScoped
@Named("bean8")
public BeanTemplate bean8()
{
    return new BeanTemplate("bean8","@ApplicationScoped",800);
}


@Produces
@ApplicationScoped
@Named("bean9")
public BeanTemplate bean9()
{
    return new BeanTemplate("bean9","@ApplicationScoped",900);
}

@Produces
@ApplicationScoped
@Named("bean10")
public BeanTemplate bean10()
{
    return new BeanTemplate("bean10","@ApplicationScoped",800);
}

@Produces
@ApplicationScoped
@Named("bean11")
public BeanTemplate bean11()
{
    return new BeanTemplate("bean11","@ApplicationScoped",700);
}

@Produces
@ApplicationScoped
@Named("bean12")
public BeanTemplate bean12()
{
    return new BeanTemplate("bean12","@ApplicationScoped",600);
}

@Produces
@ApplicationScoped
@Named("bean13")
public BeanTemplate bean13()
{
    return new BeanTemplate("bean13","@ApplicationScoped",500);
}

@Produces
@ApplicationScoped
@Named("bean14")
public BeanTemplate bean14()
{
    return new BeanTemplate("bean14","@ApplicationScoped",400);
}

@Produces
@ApplicationScoped
@Named("bean15")
public BeanTemplate bean15()
{
    return new BeanTemplate("bean15","@ApplicationScoped",300);
}

@Produces
@ApplicationScoped
@Named("bean16")
public BeanTemplate bean16()
{
    return new BeanTemplate("bean16","@ApplicationScoped",200);
}



}
