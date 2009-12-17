nugoh: simple but powerful integration tool
==============================


In order to run nugoh you will need to install [Pax Runner](http://paxrunner.ops4j.org)

Once installed just run:

    pax-run.sh "--profiles=log,compendium,spring.dm,config" "--repositories=+http://213.186.46.48:9999/archiva/repository/snapshots/@snapshots" "--bootDelegation=sun.*,com.sun.*" "http://github.com/jcheype/nugoh/raw/master/paxrunner/nugoh.conf"


Configuration
--------------------
There 2 configuration files:

 *  [config.xml](http://github.com/jcheype/nugoh/blob/master/paxrunner/runner/config.xml) :
    this file contains the flows definitions in xml
 *  [restful.properties](http://github.com/jcheype/nugoh/blob/master/paxrunner/runner/restful.properties):
    this file contain action id to call and Restful url definition, parameters are set in the context map


Development
--------------------

### Java bundle action service
An action service only needs to have a method "public void run(Map<String, Object>)", the name of the method can be configured
An optional "public void init()" method is called at the first usage of the component.
All attribute in the xml configuration file are set in the action service before the init call.

See [sample](http://github.com/jcheype/nugoh/tree/master/service/sample/) for a sample pojo action service

### Javscript action service
Just add a javascript file in the runner/scripts folder it will be automatically launch/updated
The javascript must contain 2 methods:

    function init(){}
    function run(contextMap){}

### Groovy action service
Just add a groovy class file in the runner/scripts folder it will be automatically launch/updated
The class mush contain a run and optionally an init methods:

    def init(){}
    def run(Map contextMap){}

More component will coming soon.

Performance / Availability
--------------------
The OSGI architecture allow you to update/fix component at runtime with no interruption of service.
Currently on my mac book laptop
with 5 concurrent connections:

    Server Software:        Jetty(6.1.x)
    Server Hostname:        localhost
    Server Port:            8080

    Document Path:          /flow1/21
    Document Length:        11 bytes

    Concurrency Level:      5
    Time taken for tests:   4.972 seconds
    Complete requests:      50000
    Failed requests:        0
    Write errors:           0
    Keep-Alive requests:    50000
    Total transferred:      4800096 bytes
    HTML transferred:       550011 bytes
    Requests per second:    10056.11 [#/sec] (mean)
    Time per request:       0.497 [ms] (mean)
    Time per request:       0.099 [ms] (mean, across all concurrent requests)
    Transfer rate:          942.78 [Kbytes/sec] received

    Connection Times (ms)
                  min  mean[+/-sd] median   max
    Connect:        0    0   0.0      0       0
    Processing:     0    0   0.4      0      16
    Waiting:        0    0   0.4      0      16
    Total:          0    0   0.4      0      16

    Percentage of the requests served within a certain time (ms)
      50%      0
      66%      1
      75%      1
      80%      1
      90%      1
      95%      1
      98%      1
      99%      1
     100%     16 (longest request)

with 100 concurrent connections:

    Server Software:        Jetty(6.1.x)
    Server Hostname:        localhost
    Server Port:            8080

    Document Path:          /flow1/21
    Document Length:        11 bytes

    Concurrency Level:      100
    Time taken for tests:   5.436 seconds
    Complete requests:      50000
    Failed requests:        0
    Write errors:           0
    Keep-Alive requests:    50000
    Total transferred:      4805568 bytes
    HTML transferred:       550638 bytes
    Requests per second:    9197.97 [#/sec] (mean)
    Time per request:       10.872 [ms] (mean)
    Time per request:       0.109 [ms] (mean, across all concurrent requests)
    Transfer rate:          863.31 [Kbytes/sec] received

    Connection Times (ms)
                  min  mean[+/-sd] median   max
    Connect:        0    1  27.1      0     985
    Processing:     0   10   7.1      9     246
    Waiting:        0   10   7.1      9     246
    Total:          0   11  28.2      9    1000

    Percentage of the requests served within a certain time (ms)
      50%      9
      66%     11
      75%     12
      80%     13
      90%     16
      95%     18
      98%     24
      99%     29
     100%   1000 (longest request)



Feel free to contact me if you are interested by this project on twitter: @mushman
