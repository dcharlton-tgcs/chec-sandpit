# Summary

# POSBC Bridge

POSBC Bridge is a Java application that will run on the lane alongside CHEC, allowing the Self Checkout Lane to interact with a modern POS exposing a REST API.

POSBC Bridge will implement a NGP POS client, allowing it to natively interact with NGP Transaction Engine through the exposed API.


```plantuml
@startuml

node CHEC
node NGP_Transaction_Engine
node TP.NET_POS

package "*** POSBC Bridge ***" {
    CHEC-->[TCP Client]
    
    [TCP Client]-->[Bridge Handler]

    package "*** NGP Interface & Models ***"{

        [Bridge Handler]-->[NGP POS API]

    }
}

package "NGP POS Service" {
    [NGP POS API]-->[NGP API Rest Controller]
    [NGP API Rest Controller]-->[NGP Service Impl]

    [NGP Service Impl]-->[NGP POS Client]
    
}

package "*** TP.NET Adapter implementing NGP API ***" {

    [NGP POS API]-->[API Rest Controller]
    [API Rest Controller]-->[TP.NET Service Impl]
    [TP.NET Service Impl]-->[TP.NET Client]
}

    [NGP POS Client]-->NGP_Transaction_Engine
    [TP.NET Client]-->TP.NET_POS


@enduml
```



# Developer Guide

### Build
`mvn clean install`

### Build (no unit tests)
`mvn clean install -DskipTests`

## Test

```
TODO
```
