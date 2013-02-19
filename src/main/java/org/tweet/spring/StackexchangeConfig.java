package org.tweet.spring;

import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
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

        final DefaultHttpClient rawHttpClient = new DefaultHttpClient(cxMgr);
        final DecompressingHttpClient httpClient = new DecompressingHttpClient(rawHttpClient);
        return new QuestionsApi(httpClient);
    }

}
