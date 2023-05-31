package com.redhat.zgrinber;

import org.jboss.logging.Logger;

public class BeanTemplate {

    private String name;
    private String scope;

    private long waitingTime;
    private static final Logger LOG = Logger.getLogger(BeanTemplate.class);
    public BeanTemplate(String name, String scope) {
        this.name = name;
        this.scope = scope;
        this.waitingTime = (long) ((Math.random() * 4239879872345L) % 1000);
        try {
            Thread.sleep(waitingTime);
        } catch (InterruptedException e) {
            LOG.warn(Thread.currentThread().getName() + " Interrupted in bean template, instance=" + this.name );
        }
        finally {
            LOG.infof("Successfully created Bean of type BeanTemplate, with name = %s , after %s ms ",this.name,this.waitingTime);
        }

    }

    public BeanTemplate(String name, String scope,long waitingTime) {
        this.name = name;
        this.scope = scope;
        this.waitingTime = waitingTime;
        try {
            Thread.sleep(waitingTime);
        } catch (InterruptedException e) {
            LOG.warn(Thread.currentThread().getName() + " Interrupted in bean template, instance=" + this.name );
        }
        finally {
            LOG.infof("Successfully created Bean of type BeanTemplate, with name = %s , after %s ms ",this.name,this.waitingTime);
        }

    }

    public void handleLogic()
    {
        performSomeBusinessLogic();
        LOG.info(Thread.currentThread().getName() + " Done some logic in bean with name=" + this.name );
    }

    private void performSomeBusinessLogic() {

    }
}
