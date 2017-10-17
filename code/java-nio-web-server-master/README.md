# java-nio-web-server
[![Build Status](https://travis-ci.org/julesbond007/java-nio-web-server.svg)](https://travis-ci.org/julesbond007/java-nio-web-server)
[![Coverage Status](https://coveralls.io/repos/julesbond007/java-nio-web-server/badge.svg?branch=master&service=github)](https://coveralls.io/github/julesbond007/java-nio-web-server?branch=master)

This is a rather simple web server I'm working on using Java NIO.  It's a work in progress, but would like to keep it as simple as possible.

To try the server:

if gradle is already installed: `gradle <task-name>` otherwise `chmod +x gradlew` then `./gradlew <task-name>`

```{r, engine='bash', count_lines}
$ gradle run
```

or create executable script using gradle
```{r, engine='bash', count_lines}
$ gradle installApp
```

execute the script gradle has created in below bin folder: 
```{r, engine='bash', count_lines}
$ build/install/java-nio-web-server/bin/java-nio-web-server
```

I have a sample backbone & marionette app deployed under the `htdocs` folder called `jsoft`:

```{r, engine='bash', count_lines}
http://localhost:9000/jsoft/index.html
```

Using the dev tools in Chrome, we see the application loading and the subsequent requests the browser makes to build the page:

![Settings Window](https://raw.githubusercontent.com/julesbond007/simple-java-nio-web-server/master/htdocs/jsoft/images/app.png)


Starting up the server - [Main.java](https://github.com/julesbond007/simple-java-nio-web-server/blob/master/src/main/java/com/nio/http/server/Main.java)

```java
public class Main {
    private static AbstractApplicationContext ctx;

    public static void main(final String[] args) {
        ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        ctx.registerShutdownHook();

        final Server server = ctx.getBean(Server.class);

        while (true) {
            server.run();
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                server.shutdown();
            }
        }
    }
}
```

Sample logs on startup:

```{r, engine='bash', count_lines}
2016-01-11 00:37:00,873 DEBUG [main] c.n.h.server.Server: Initializing server...
2016-01-11 00:37:00,887 DEBUG [main] c.n.h.server.Server: Server is running at http://localhost:9000
```

Sample request logs:

```{r, engine='bash', count_lines}
2016-01-11 00:37:07,681 DEBUG [main] c.n.h.server.Server: GET /jsoft/index.html HTTP/1.1
2016-01-11 00:37:07,681 DEBUG [main] c.n.h.server.Server: Host: localhost:9000
2016-01-11 00:37:07,681 DEBUG [main] c.n.h.server.Server: Connection: keep-alive
2016-01-11 00:37:07,681 DEBUG [main] c.n.h.server.Server: Pragma: no-cache
2016-01-11 00:37:07,681 DEBUG [main] c.n.h.server.Server: Cache-Control: no-cache
2016-01-11 00:37:07,681 DEBUG [main] c.n.h.server.Server: Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
2016-01-11 00:37:07,681 DEBUG [main] c.n.h.server.Server: Upgrade-Insecure-Requests: 1
2016-01-11 00:37:07,682 DEBUG [main] c.n.h.server.Server: User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36
2016-01-11 00:37:07,682 DEBUG [main] c.n.h.server.Server: Accept-Encoding: gzip, deflate, sdch
2016-01-11 00:37:07,682 DEBUG [main] c.n.h.server.Server: Accept-Language: en-US,en;q=0.8
```
