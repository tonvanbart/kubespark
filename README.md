## Docker and Kubernetes examples

Quick overview of running and configuring a simple Java application in Docker and Kubernetes.
Please note that this is for my own education and based on my (limited...) understanding.<br>
The example is a trivial Dockerized [Spark/Java](http://sparkjava.com/) "hello world" service which
gets its greeting from a JSON config file. The location of the file is read from the
environment variable `CONFIGLOC`, if this is not set it will fall back to a default configuration.
<br>
See the [Makefile](Makefile) in the project for details on building and running on Docker.

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
    
### Running on minikube

Running on minikube will fail with an image pull error if the image can't be downloaded.
To work around this, build the image using the Docker daemon from Minikube:

    eval $(minikube docker-env)
    make image
    
In the Kubernetes deployment, `imagePullPolicy` has to be set to `Never`, or Kubernetes
will still try to pull the image. See for example [plain.yaml](deployments/plain.yaml) for details. 

With minikube running, deploy the app from the top level project directory:

    kubectl create -f ./deployments/plain.yaml
    
Check if everything came up, and note the name of the new service:

    kubectl get deployments,services,pods,configmaps,secrets
    
Minikube can give you the services NodePort base URL with `minikube service --url <servicename>`, so to see
the greeting from the command line:

    curl `minikube service --url simple`/greeting
    
You can also do `minikube service list` or `kubectl get services` to see the available services.

### kubernetes configuration using configMap
The file [configmap.yaml](deployments/configmap.yaml) has an example of a deployment configured with a configMap.
To try it, deploy again from the top directory:

    kubectl create -f deployments/configmap.yaml
    
To keep a distinction, the service name is slightly different; using HTTPie instead of curl:

    http $(minikube service --url configmap)/greeting
    
If all went well, you should see the value configured in `configmap.yaml`:

    HTTP/1.1 200 OK
    Content-Type: text/html;charset=utf-8
    Date: Mon, 17 Dec 2018 22:11:25 GMT
    Server: Jetty(9.4.z-SNAPSHOT)
    Transfer-Encoding: chunked

    greeting from configmap
    
### kubernetes configuration using Secret
The file [secret.yaml](deployments/secret.yaml) shows the greeting being configured using a Kubernetes `Secret`:

    kubectl create -f deployments/secret.yaml
    
The resulting service will be exposed on a separate port again.

    http `minikube service --url secret`/greeting
    
The output should show a different greeting.

