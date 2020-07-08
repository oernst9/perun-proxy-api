package cz.muni.ics.perunproxyapi.connectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import lombok.AllArgsConstructor;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Connector for calling Perun RPC
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 * @author Pavol Pluta <pavol.pluta1@gmail.com>
 */
//@AllArgsConstructor
public class PerunConnectorRpc {
    private static final Logger log = LoggerFactory.getLogger(PerunConnectorRpc.class);

    private String perunUrl;
    private String perunUser;
    private String perunPassword;
    private boolean isEnabled;
    private RestTemplate restTemplate;

    public PerunConnectorRpc(String perunUrl, String perunUser, String perunPassword, boolean isEnabled) {
        this.perunUrl = perunUrl;
        this.perunUser = perunUser;
        this.perunPassword = perunPassword;
        this.isEnabled = isEnabled;
    }

    // Values from config
    @Value("${connector.rpc.request.timeout}")
    private int REQUEST_TIMEOUT;

    @Value("${connector.rpc.connect.timeout}")
    private int CONNECT_TIMEOUT;

    @Value("${connector.rpc.socket.timeout}")
    private int SOCKET_TIMEOUT;

    @Value("${connector.rpc.max.connections}")
    private int MAX_CONNECTIONS;

    @Value("${connector.rpc.max.per.route}")
    private int MAX_CONN_PER_ROUTE;

    public void setPerunUrl(String perunUrl) {
        if (perunUrl == null || perunUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Perun URL cannot be null or empty");
        } else if (perunUrl.endsWith("/")) {
            perunUrl = perunUrl.substring(0, perunUrl.length() - 1);
        }

        this.perunUrl = perunUrl;
    }

    public void setPerunUser(String perunUser) {
        if (perunUser == null || perunUser.trim().isEmpty()) {
            throw new IllegalArgumentException("Perun USER cannot be null or empty");
        }

        this.perunUser = perunUser;
    }

    public void setPerunPassword(String perunPassword) {
        if (perunPassword == null || perunPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Perun PASSWORD cannot be null or empty");
        }

        this.perunPassword = perunPassword;
    }

    public void setEnabled(String enabled) {
        this.isEnabled = Boolean.parseBoolean(enabled);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    @PostConstruct
    public void postInit() {
        restTemplate = new RestTemplate();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(REQUEST_TIMEOUT) // The timeout when requesting a connection from the connection manager
                .setConnectTimeout(CONNECT_TIMEOUT) // Determines the timeout in milliseconds until a connection is established
                .setSocketTimeout(SOCKET_TIMEOUT) // The timeout for waiting for data
                .build();

        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(MAX_CONNECTIONS); // max total connections
        poolingConnectionManager.setDefaultMaxPerRoute(MAX_CONN_PER_ROUTE);

        ConnectionKeepAliveStrategy connectionKeepAliveStrategy = (response, context) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator
                    (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();

                if (value != null && param.equalsIgnoreCase("timeout")) {
                    return Long.parseLong(value) * 1000;
                }
            }
            return 20000L;
        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnectionManager)
                .setKeepAliveStrategy(connectionKeepAliveStrategy)
                .build();

        HttpComponentsClientHttpRequestFactory poolingRequestFactory = new HttpComponentsClientHttpRequestFactory();
        poolingRequestFactory.setHttpClient(httpClient);

        // basic auth
        List<ClientHttpRequestInterceptor> interceptors =
                Collections.singletonList(new BasicAuthenticationInterceptor(perunUser, perunPassword));
        InterceptingClientHttpRequestFactory authenticatingRequestFactory =
                new InterceptingClientHttpRequestFactory(poolingRequestFactory, interceptors);
        restTemplate.setRequestFactory(authenticatingRequestFactory);
    }


    /**
     * Make post call to Perun RPC
     * @param manager String value representing manager to be called. Use constants from this class.
     * @param method Method to be called (i.e. getUserById)
     * @param map Map of parameters to be passed as request body
     * @return Response from Perun
     */
    public JsonNode post(String manager, String method, Map<String, Object> map) {
        if (!this.isEnabled) {
            return JsonNodeFactory.instance.nullNode();
        }

        String actionUrl = this.perunUrl + "/json/" + manager + '/' + method;

        // make the call
        try {
            log.trace("calling {} with {}", actionUrl, map);
            return restTemplate.postForObject(actionUrl, map, JsonNode.class);
        } catch (HttpClientErrorException ex) {
            MediaType contentType = ex.getResponseHeaders().getContentType();
            String body = ex.getResponseBodyAsString();
            log.error("HTTP ERROR " + ex.getRawStatusCode() + " URL " + actionUrl + " Content-Type: " + contentType);
            if ("json".equals(contentType.getSubtype())) {
                try {
                    log.error(new ObjectMapper().readValue(body,JsonNode.class).path("message").asText());
                } catch (IOException e) {
                    log.error("cannot parse error message from JSON", e);
                }
            } else {
                log.error(ex.getMessage());
            }
            throw new RuntimeException("cannot connect to Perun RPC", ex);
        }
    }
}
