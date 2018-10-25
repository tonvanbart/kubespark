Quick testdrive of Spark-Java with configuration file.
Configuration location is read from an environment variable, if not set a default
configuration is read from the classpath.


To build an executable jar and run it:

    mvn clean compile assembly:single
    java -jar target/hello-spark-1.0-SNAPSHOT-jar-with-dependencies.jar
    
To get a response (I'm using the excellent [HTTPie](https://httpie.org/) tool):

    http :4567/greeting
    
Which should give you something like

    HTTP/1.1 200 OK
    Content-Type: text/html;charset=utf-8
    Date: Thu, 25 Oct 2018 21:00:27 GMT
    Server: Jetty(9.4.8.v20171121)
    Transfer-Encoding: chunked

    greeting from classpath

Apart from Spark-Java, the only dependencies are slf4j and project Lombok.
The sample integration test is based on code from 
[https://github.com/mscharhag/blog-examples/tree/master/sparkdemo](https://github.com/mscharhag/blog-examples/tree/master/sparkdemo).

    
    