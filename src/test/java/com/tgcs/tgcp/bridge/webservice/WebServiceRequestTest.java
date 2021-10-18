package com.tgcs.tgcp.bridge.webservice;

import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel.VoidOrderRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WebServiceRequest.class, VoidOrderRequest.class})
public class WebServiceRequestTest {

    @Autowired
    WebServiceRequest webServiceRequest;

    @MockBean
    RestTemplate restTemplate;

    @Autowired
    VoidOrderRequest voidOrderRequest;

    @Test
    public void sendRequestTest() throws Exception {
        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
                )
        ).thenReturn(new ResponseEntity<>("test", HttpStatus.OK));
        webServiceRequest.sendRequest("", voidOrderRequest, HttpMethod.POST, String.class);
        verify(restTemplate).exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<String>>any()
        );
    }

    @Test(expected = HttpServerErrorException.class)
    public void sendRequestThrowsHttpServerErrorExceptionTest() throws Exception {
        doThrow(new HttpServerErrorException(HttpStatus.UNAUTHORIZED)).when(restTemplate).exchange
                (ArgumentMatchers.anyString(),
                        ArgumentMatchers.any(HttpMethod.class),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<Class<String>>any()
                );
        webServiceRequest.sendRequest("", voidOrderRequest, HttpMethod.POST, String.class);
    }

    @Test(expected = HttpClientErrorException.class)
    public void sendRequestThrowsHttpClientErrorExceptionTest() throws Exception {
        doThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED)).when(restTemplate).exchange
                (ArgumentMatchers.anyString(),
                        ArgumentMatchers.any(HttpMethod.class),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<Class<String>>any()
                );
        webServiceRequest.sendRequest("", voidOrderRequest, HttpMethod.POST, String.class);
    }
}