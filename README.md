# lazy-vs-eager

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Goal 

To compare lazy loaded beans versus eagerly loaded beans, and see how it can dramatically reduce cold start up time of the application.

## Plan of action 
We'll define 16 custom beans, and one consumer bean, which is the HTTP REST API Endpoint:
1. first we will run the application with all 16 beans defined to be lazily loaded( only when injected and called by the injecting class - AKA 'the client', the bean is created), and we'll record the startup time.
2. Then we'll invoke methods on all the beans to trigger their creation, and to see how it takes time each time it's called, depending on the logic of each one ( it's only demo off course).
3. Afterward we will modify the application and run it with all the beans eagerly loaded, and will record the cold startup, which will be significantly longer.
4. REST API Endpoint client/consumer bean `RestResource` (which inject all the 16 custom beans for usage) , will be loaded eagerly anyway in both modes.
5. Second part TBD - We'll build a native image to reduce even more the cold startup, and will run it using a knative service and revisions:
   1. One revision with above beans lazily loaded
   2. Second revision with above beans eagerly loaded
   3. We'll split traffic between the two revisions.


## Procedure with Demo Sections 1-4

### Lazily Loaded Mode:
1. Start a maven build for Creating Jar of the application
```shell
mvn package
```
Output:
```shell
[INFO] Scanning for projects...
[INFO] 
[INFO] -----------------< com.redhat.zgrinber:lazy-vs-eager >------------------
[INFO] Building lazy-vs-eager 1.0.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ lazy-vs-eager ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 2 resources
[INFO] 
[INFO] --- quarkus-maven-plugin:3.0.4.Final:generate-code (default) @ lazy-vs-eager ---
[INFO] 
[INFO] --- maven-compiler-plugin:3.11.0:compile (default-compile) @ lazy-vs-eager ---
[INFO] Changes detected - recompiling the module! :source
[INFO] Compiling 4 source files with javac [debug release 17] to target/classes
[INFO] 
[INFO] --- quarkus-maven-plugin:3.0.4.Final:generate-code-tests (default) @ lazy-vs-eager ---
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ lazy-vs-eager ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /home/zgrinber/git/quarkus-lazy-vs-eager-instantiation/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.11.0:testCompile (default-testCompile) @ lazy-vs-eager ---
[INFO] No sources to compile
[INFO] 
[INFO] --- maven-surefire-plugin:3.0.0:test (default-test) @ lazy-vs-eager ---
[INFO] No tests to run.
[INFO] 
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ lazy-vs-eager ---
[INFO] Building jar: /home/zgrinber/git/quarkus-lazy-vs-eager-instantiation/target/lazy-vs-eager-1.0.0-SNAPSHOT.jar
[INFO] 
[INFO] --- quarkus-maven-plugin:3.0.4.Final:build (default) @ lazy-vs-eager ---
[INFO] [io.quarkus.deployment.QuarkusAugmentor] Quarkus augmentation completed in 1109ms
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.882 s
[INFO] Finished at: 2023-06-01T12:44:00+03:00
[INFO] ------------------------------------------------------------------------

```
2. Run the application
```shell
 java -jar target/quarkus-app/quarkus-run.jar
```
Output
```shell
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2023-06-01 12:45:50,417 INFO  [com.red.zgr.RestResource] (main) Created Rest Resource Bean Eagerly And Injected all Bean1...Bean16
2023-06-01 12:45:50,516 INFO  [io.quarkus] (main) lazy-vs-eager 1.0.0-SNAPSHOT on JVM (powered by Quarkus 3.0.4.Final) started in 0.662s. Listening on: http://0.0.0.0:8080
2023-06-01 12:45:50,518 INFO  [io.quarkus] (main) Profile prod activated. 
2023-06-01 12:45:50,518 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy-reactive, smallrye-context-propagation, vertx]
```
Note: As you can see, the Application started in 662 ms.

3. All the beans are lazily loaded, so they'll be created once a consumer/client will invoke a method on them, let's do it by opening a new shell terminal , We'll invoke a method on all the beans using a for loop, through a REST API GET call
```shell
for i in {1..16}; do curl -i -X GET http://localhost:8080/hello/bean$i ; echo;  echo  ; done
```

4. Let's see the log of the application what happened:
```shell
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2023-06-01 12:45:50,417 INFO  [com.red.zgr.RestResource] (main) Created Rest Resource Bean Eagerly And Injected all Bean1...Bean16
2023-06-01 12:45:50,516 INFO  [io.quarkus] (main) lazy-vs-eager 1.0.0-SNAPSHOT on JVM (powered by Quarkus 3.0.4.Final) started in 0.662s. Listening on: http://0.0.0.0:8080
2023-06-01 12:45:50,518 INFO  [io.quarkus] (main) Profile prod activated. 
2023-06-01 12:45:50,518 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy-reactive, smallrye-context-propagation, vertx]
2023-06-01 12:51:25,824 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean1 , after 100 ms 
2023-06-01 12:51:25,826 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean1
2023-06-01 12:51:26,071 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean2 , after 200 ms 
2023-06-01 12:51:26,071 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean2
2023-06-01 12:51:26,406 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean3 , after 300 ms 
2023-06-01 12:51:26,407 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean3
2023-06-01 12:51:26,840 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean4 , after 400 ms 
2023-06-01 12:51:26,841 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean4
2023-06-01 12:51:27,371 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean5 , after 500 ms 
2023-06-01 12:51:27,372 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean5
2023-06-01 12:51:28,004 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean6 , after 600 ms 
2023-06-01 12:51:28,005 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean6
2023-06-01 12:51:28,716 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean7 , after 700 ms 
2023-06-01 12:51:28,717 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean7
2023-06-01 12:51:29,531 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean8 , after 800 ms 
2023-06-01 12:51:29,532 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean8
2023-06-01 12:51:30,448 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean9 , after 900 ms 
2023-06-01 12:51:30,449 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean9
2023-06-01 12:51:31,258 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean10 , after 800 ms 
2023-06-01 12:51:31,259 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean10
2023-06-01 12:51:31,974 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean11 , after 700 ms 
2023-06-01 12:51:31,975 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean11
2023-06-01 12:51:32,589 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean12 , after 600 ms 
2023-06-01 12:51:32,590 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean12
2023-06-01 12:51:33,107 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean13 , after 500 ms 
2023-06-01 12:51:33,108 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean13
2023-06-01 12:51:33,524 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean14 , after 400 ms 
2023-06-01 12:51:33,524 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean14
2023-06-01 12:51:33,837 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean15 , after 300 ms 
2023-06-01 12:51:33,838 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean15
2023-06-01 12:51:34,053 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) Successfully created Bean of type BeanTemplate, with name = bean16 , after 200 ms 
2023-06-01 12:51:34,053 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean16
```
Note: So when we invoked a method on each of the bean, then the framework created the bean, and only then the method invoked, this is called lazily loaded ( bean only created when it first needed )  

5. Now quit the application (press CTRL+C )
6. Let's change all 16 custom beans to be eagerly loaded( loaded when injected by client classes/beans that created at startup ), we will do all of that with 2 `sed` commands:
```shell
sed --in-place 's/@ApplicationScoped/@Singleton/g'  src/main/java/com/redhat/zgrinber/BeansDefinitions.java
sed --in-place 's/import jakarta.enterprise.context.ApplicationScoped;/import jakarta.inject.Singleton;/g'  src/main/java/com/redhat/zgrinber/BeansDefinitions.java
```
We changed the Scope annotation of all beans from `@ApplicationScoped` to `@Singleton`, This will cause the creation of the beans once injected somewhere in the application, in our case , in the `RestResource` Bean, which is loaded on startup, so they expected to be created and loaded on startup as well.

7. Now with this change at hands, let's start a maven build once again to create an application JAR file
```shell
mvn package
```
8. Now run the modified Application using this JAR
```shell
java -jar target/quarkus-app/quarkus-run.jar
```

Output:
```shell
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2023-06-01 13:41:12,420 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean1 , after 100 ms 
2023-06-01 13:41:13,257 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean10 , after 800 ms 
2023-06-01 13:41:13,959 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean11 , after 700 ms 
2023-06-01 13:41:14,560 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean12 , after 600 ms 
2023-06-01 13:41:15,061 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean13 , after 500 ms 
2023-06-01 13:41:15,462 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean14 , after 400 ms 
2023-06-01 13:41:15,763 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean15 , after 300 ms 
2023-06-01 13:41:15,964 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean16 , after 200 ms 
2023-06-01 13:41:16,165 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean2 , after 200 ms 
2023-06-01 13:41:16,467 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean3 , after 300 ms 
2023-06-01 13:41:16,868 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean4 , after 400 ms 
2023-06-01 13:41:17,369 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean5 , after 500 ms 
2023-06-01 13:41:17,969 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean6 , after 600 ms 
2023-06-01 13:41:18,670 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean7 , after 700 ms 
2023-06-01 13:41:19,471 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean8 , after 800 ms 
2023-06-01 13:41:20,371 INFO  [com.red.zgr.BeanTemplate] (main) Successfully created Bean of type BeanTemplate, with name = bean9 , after 900 ms 
2023-06-01 13:41:20,373 INFO  [com.red.zgr.RestResource] (main) Created Rest Resource Bean Eagerly And Injected all Bean1...Bean16
2023-06-01 13:41:20,460 INFO  [io.quarkus] (main) lazy-vs-eager 1.0.0-SNAPSHOT on JVM (powered by Quarkus 3.0.4.Final) started in 8.715s. Listening on: http://0.0.0.0:8080
2023-06-01 13:41:20,460 INFO  [io.quarkus] (main) Profile prod activated. 
2023-06-01 13:41:20,460 INFO  [io.quarkus] (main) Installed features: [cdi, resteasy-reactive, smallrye-context-propagation, vertx]
```

It takes the application to startup 8.715 seconds!

9. Now if you will invoke methods on all the beans, then it will be performed instantly, because it already created them, On another shell terminal, run the following loop:
```shell
for i in {1..16}; do curl -i -X GET http://localhost:8080/hello/bean$i ; echo;  echo  ; done
```

Output of application logs:
```shell
2023-06-01 13:44:54,334 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean1
2023-06-01 13:44:54,349 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean2
2023-06-01 13:44:54,359 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean3
2023-06-01 13:44:54,369 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean4
2023-06-01 13:44:54,380 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean5
2023-06-01 13:44:54,389 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean6
2023-06-01 13:44:54,401 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean7
2023-06-01 13:44:54,413 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean8
2023-06-01 13:44:54,424 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean9
2023-06-01 13:44:54,434 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean10
2023-06-01 13:44:54,445 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean11
2023-06-01 13:44:54,453 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean12
2023-06-01 13:44:54,466 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean13
2023-06-01 13:44:54,477 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean14
2023-06-01 13:44:54,486 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean15
2023-06-01 13:44:54,496 INFO  [com.red.zgr.BeanTemplate] (executor-thread-1) executor-thread-1 Done some logic in bean with name=bean16
```

10. Exit the application.

11. Reset beans definition to be lazily loaded
```shell
git restore --worktree src/main/java/com/redhat/zgrinber/BeansDefinitions.java
```

### Conclusion

There is a gap of approximately ~ 8.1 seconds between the two modes, the startup of the application with the beans being lazily loaded was roughly 13 times faster than the eagerly loaded beans application.
In the Lazily loaded version - The creation time of beans moved to the actual consumption time of the bean logic, that is, whenever a method was invoked on the bean , whereas in the eagerly loaded version, the method was invoked right away without waiting for the bean to be created, as it was already created on startup.

off course, it's only an academic/laboratory conditions extreme example, but still it reflects that this is very important consideration when you want to reduce cold startup time of an application.
if used correctly and choosing the right tradeoffs , in reality you can reduce the cold start-up time by a factor of 2-3 times. 
So the recommendations are, given a bean, you should:

- If you have `Bean`/`Class Instance` that is being used **in all** use cases and paths of the application scenario , it's worthwhile defining it to be eagerly loaded.
- Otherwise, if it's only in part of the applications use cases, then it's better to define it to be lazily loaded.
- Avoid using beans that their creation time takes more than 1-2 seconds, but if you need bean with long time creation logic, you can delegate the heavy part of the logic to [Quarkus Scheduler' Scheduled task](https://quarkus.io/guides/scheduler), to do it in a separate worker thread that Quarkus framework is using, and you can also use [Quarkus Scheduler Conditional Execution feature](https://quarkus.io/guides/scheduler-reference#conditional_execution) to make sure that the task will run only once.      

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```



> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/lazy-vs-eager-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.