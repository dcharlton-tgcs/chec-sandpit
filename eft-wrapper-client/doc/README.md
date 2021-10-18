# EFT Wrapper Client

The project is autoconfigurable.

## usage

- add dependency to project
```xml  
<dependency>
    <groupId>com.tgcs.posbc.bridge</groupId>
    <artifactId>eft-wrapper-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

- Update your application.yml
```yaml
pos:
  handle:
    eft:
      wrapper:
        context-path: </path>    # default: /
        proto: <http|https>      # default: http
        address: <ip of wrapper> # default: localhost
        port: <port of wrapper>  # default: 8181
```

- Autowire the client EftWrapperClient

```java
@Autowire
private EftWrapperClient eftWrapperClient;
```

###
NB: start the project with VM Options to select the spring profile 
```
-Dspring.profiles.active=dev
```