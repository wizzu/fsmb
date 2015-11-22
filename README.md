FSMB - Fine and Simple Message Board
====================================

FSMB is a simple demo message board application, providing two alternative
REST API implementations with identical functionality.


Building
---------

Maven is used as the build system.

Run this command:

> mvn package

A war package will be built in target/fsmb.war.


Installing
----------

Copy the fsmb.war package to the Web Container's webapps directory.

You may test that the installation succeeded by using the ping service:

> curl http://server:port/fsmb/ping
 
The ping service responds with "fsmb: pong" if FSMB has been successfully
deployed and is running.


API Documentation
-----------------

There are two REST API services provided, one for creating messages and
another for listing messages.


### createMessage

* /fsmb/plain/createMessage
* /fsmb/marshal/createMessage

The plain/createMessage API accepts application/x-www-form-urlencoded POST
data for creating messages. The possible parameters are 'sender', 'content',
'title' and 'url'.  

The marsha/createMessage API accepts application/json POST data for creating
messages. The POST data should contain a single JSON object with string
values for each of 'sender', 'content', 'title' and 'url'.

Testing examples:

> curl -X POST -d @create-message.txt http://localhost:8080/fsmb/plain/createMessage
> curl -X POST -d @create-message.json -H "Content-Type:application/json" http://localhost:8080/fsmb/marshal/createMessage

### listMessages

* /fsmb/plain/listMessages
* /fsmb/marshal/listMessages

Both the plain/listMessages and marshal/listMessages APIs respond to GET
request. The GET requests may specify the 'version' parameter, with valid
values being 1 or 2. For version 2, also the 'format' parameter may be
additionally specified, with valid values being 'json' or 'xml'. The default
format is JSON.

Response JSON data for version 1 does not contain the URL field.

Testing examples:

> curl http://localhost:8080/fsmb/plain/listMessages?version=1'
> curl http://localhost:8080/fsmb/plain/listMessages?version=2'
> curl http://localhost:8080/fsmb/plain/listMessages?version=2&format=json'
> curl http://localhost:8080/fsmb/plain/listMessages?version=2&format=xml'

> curl http://localhost:8080/fsmb/marshal/listMessages?version=1'
> curl http://localhost:8080/fsmb/marshal/listMessages?version=2'
> curl http://localhost:8080/fsmb/marshal/listMessages?version=2&format=json'
> curl http://localhost:8080/fsmb/marshal/listMessages?version=2&format=xml'
