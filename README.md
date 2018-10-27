## Externalized config examples

Quick overview of externalizing config files.
The example is a trivial Dockerized [Spark/Java](http://sparkjava.com/) "hello world" service which
gets its greeting from a JSON config file. The location of the file is read from the
environment variable `CONFIGLOC`, if this is not set it will fall back to a default configuration.
<br>
See the [Makefile](Makefile) in the project for details.

### make targets

command | action
--- | ---
 `mvn compile exec:java` | Run the project without packaging as a Docker image.
 `make image` | creates a Docker image with the app inside. 
 `make run` | runs the image with the internal config. 
 `make run-bindmount` | Does a bind mount of `bindmount/` and runs the app with the config inside that.
 `make volume-data` | Create a Docker volume, and copy `volumedata/configuration.json` into it.
 `make run-volume` | Run the image with the volume from the previous step mounted.
 `make inspect-volume` | Run an image in interactive mode with the volume mounted (so the contents can be inspected).
 
In all cases, use `curl` or [HttpIE](https://httpie.org/) to see the configured message. For example
when running with `make run-bindmount`:

    http localhost:4567/greeting
    HTTP/1.1 200 OK
    Content-Type: text/html;charset=utf-8
    Date: Sat, 27 Oct 2018 22:38:28 GMT
    Server: Jetty(9.4.z-SNAPSHOT)
    Transfer-Encoding: chunked
    
    greeting from bind mount
    
 

