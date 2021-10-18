# POSBC Bridge Application Overview

# Table of contents
- [Table of contents](#table-of-contents)
- [Introduction](#introduction)
- [Overview](#overview)
- [Add Item Example](#additem-flow-example)

# Introduction

POSBC Bridge is a Java application created to connect standard CHEC lanes with modern WebService POSes

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