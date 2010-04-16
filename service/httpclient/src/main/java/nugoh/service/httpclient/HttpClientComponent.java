package nugoh.service.httpclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.util.Map;

public class HttpClientComponent {

    private String username = "";
    private String password = "";
    private String url;
    private String method = "GET";
    private HttpClient httpclient;
    private String paramKey = null;
    private String resultKey = "data";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public void init() {
        httpclient = new HttpClient();
    }

    public void run(Map<String, Object> context) throws IOException {
        HttpMethodParams params = new HttpMethodParams();
        if (paramKey != null && !paramKey.equals("")) {
            Map<String, Object> paramMap = (Map<String, Object>) context.get(paramKey);
            if (paramMap != null) {
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    params.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        HttpMethod httpMethod;
        if ("GET".equals(getMethod())) {
            httpMethod = new GetMethod(url);
        } else {
            httpMethod = new PostMethod(url);
        }

        httpMethod.setParams(params);
        try {
            httpclient.executeMethod(httpMethod);

            context.put(resultKey, httpMethod.getResponseBodyAsString());
        } finally{
            httpMethod.releaseConnection();
        }
    }
}