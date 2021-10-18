package com.tgcs.tgcp.bridge.webservice;


import com.tgcs.tgcp.bridge.webservice.ngp.ngpconnection.ngprequestmodel.WebRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class WebServiceRequest {

    private static final Logger logger = LoggerFactory.getLogger(WebServiceRequest.class);
//    private static Log loggerPosbcMessage = LogFactory.getLog(LogType.POSBC_MESSAGE);

    @Autowired
    RestTemplate restTemplate;

    public <T extends WebRequestBody, V> ResponseEntity<V> sendRequest(String url, T request, HttpMethod requestMethod, Class<V> responseType) throws Exception {
        return sendRequest(url, null, request, requestMethod, responseType);
    }

    public <T extends WebRequestBody, V> ResponseEntity<V> sendRequest(String url, MultiValueMap<String, String> headers, T request, HttpMethod requestMethod, Class<V> responseType) throws Exception {
        HttpEntity<T> httpEntity = new HttpEntity<>(request, headers);
        ResponseEntity<V> response = null;

        try {
            logger.info("URL: " + url + ",\nrequestMethod: " + requestMethod +
                    ",\nbody: " + new JSONObject(httpEntity.getBody()) + ",\nresponseType: " + responseType.getName());
            response = restTemplate.exchange(url, requestMethod, httpEntity, responseType);
            logger.info("Body response from PosbcService: \n" + new JSONObject(response.getBody()));
        } catch (Exception e) {
            String err = "";
            if (e instanceof HttpServerErrorException) {
                err = ((HttpServerErrorException) e).getResponseBodyAsString();
            }

            if (e instanceof HttpClientErrorException) {
                err = ((HttpClientErrorException) e).getResponseBodyAsString();
            }


            logger.error(err, e);
            throw e;

        }
        return response;
    }
}
