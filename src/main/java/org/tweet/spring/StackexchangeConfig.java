package org.tweet.spring;

import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.stackexchange.api.client.QuestionsApi;

@Configuration
@PropertySource({ "classpath:stackexchange.properties" })
public class StackexchangeConfig {

    //

    @Bean
    public QuestionsApi questionsApi() {
        final PoolingClientConnectionManager cxMgr = new PoolingClientConnectionManager(SchemeRegistryFactory.createDefault());
        cxMgr.setMaxTotal(100);
        cxMgr.setDefaultMaxPerRoute(20);

        final HttpParams httpParameters = new BasicHttpParams();
        final int timeoutConnection = 6000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        final int timeoutSocket = 6000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        final DefaultHttpClient rawHttpClient = new DefaultHttpClient(cxMgr, httpParameters);

        final DecompressingHttpClient httpClient = new DecompressingHttpClient(rawHttpClient);
        return new QuestionsApi(httpClient);
    }
}
