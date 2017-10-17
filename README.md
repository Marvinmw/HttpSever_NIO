# HttpSever_NIO
This is a simple Http Sever implemented by Java NIO

## Guidance

also see ```/code/readme.pdf```

1. The program structure is depicted below and it uses the reactor pattern.
2. Reactor accepts the connection from the client.
3. Dispatcher monitors the events and distributes the
event to the handler by the threadpool.
4. Handler process the request.
5. Http parser is a tool to parse the http request.
6. After process the request and response, the connection
is close.
7. The program is based on Java NIO.

![strea](/code/reactor.png)

## Run Jar
1. httpserver.jar is the jar file of the server. [ ] means options. The dictionary webroot should be put into the same dictionary with httpserver.jar.
Run it : java -jar httpserver.jar serverIP port [NumberOfDispetcher NumOfThreadforEachDispetcher ] E.g. java -jar httpserver.jar 128.178.158.110 8000 4 10
Or just java -jar httpserver.jar 128.178.158.110 8000
2. httpclient.jar is to test the server. [ ] means options
You can use : java -jar httpclient.jar serverIP port [NumberOfSendingThreads] E.g. java -jar httpclient.jar 128.178.158.110 8000 1000000
Or just java -jar httpclient.jar 128.178.158.110 8000

# Author
[Wei Ma](https://github.com/Marvinmw)
