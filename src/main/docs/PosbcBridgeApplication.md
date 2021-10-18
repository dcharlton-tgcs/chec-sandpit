# POSBC Bridge Application Overview

# Table of contents
- [Table of contents](#table-of-contents)
- [Introduction](#introduction)
- [Business Goals](#business-goals)
- [Architecture Goals](#architecture-goals)
- [Overview](#overview)
- [Add Item Example](#additem-flow-example)

# Introduction

POSBC Bridge is a Java application created to connect standard CHEC lanes with modern WebService POSes

# Business Goals

    Use POSBC Bridge to leverage CHEC with WebService POS
    Reuse POSBC Bridge infrastructure on different POS platforms
    Allow CHEC to be a viable product in Europe
    Allow the reuse of the same patterns for a different POS solution
        change the webservice handler but maintain the CHEC handler implementation
    
# Architecture Goals

    CHEC should not have to change to meet initial interface goals
    POSBC Bridge should easily handle the interface between POS and CHEC
    Allow easy and quick development, without writing a specific POSBC layer for NGP
    Should have no dependencies on either CHEC or POS
    Mapping between data points between systems should be accomplished without a code release of CHEC or NGP.
    Should be flexible and easily maintainable
    Should be extendable, with low code possibilities
    
    
# Bridge Overview

```plantuml
@startuml

"CHEC Lane"-->Bridge : POSBC XML Request 
Bridge-->"WS POS" : HTTP Request + JSON body
"WS POS"-->Bridge : JSON Order Response
Bridge-->"CHEC Lane" : POSBC XML Response

@enduml
```

# AddItem flow example

```plantuml
@startuml
actor "CHEC Lane"

"CHEC Lane"-->"TCP Client": Bridge receives an XML \nRequest message
"TCP Client"-->"Bridge Handler": Bridge instantiate a specific \nhandler for the request received
"Bridge Handler"-->"Service Handler": Decode request
"Service Handler"-->"WS POS": Build and send Request to WS  
alt 200 OK
"WS POS"-->"Service Handler": WS returns a JSON object
"Service Handler"-->"Bridge Handler": JSON to Object
"Bridge Handler"-->"TCP Client": Bridge will map the data into \nthe XML response
opt POS Events
"TCP Client"-->"CHEC Lane": Receipt events (header and/or line)
"TCP Client"-->"CHEC Lane": Totals events
"TCP Client"-->"CHEC Lane": Transaction status events
"TCP Client"-->"CHEC Lane": POSBC status events
end
"TCP Client"-->"CHEC Lane":  Bridge will send the response \nXML object back to CHEC
else 400 Bad Request
"WS POS"-->"Service Handler": WS returns an exception
"Service Handler"-->"CHEC Lane": CHEC Exception
end

@enduml
```