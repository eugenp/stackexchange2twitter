package org.tweet.stackexchange.spring;

import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.stackexchange.api.client.HttpUtil;
import org.stackexchange.api.client.QuestionsApi;

@Configuration
@PropertySource({ "classpath:stackexchange.properties" })
public class StackexchangeConfig {

    //

    @Bean
    public QuestionsApi questionsApi() {
        final DefaultHttpClient rawHttpClient = HttpUtil.httpClient();

        final DecompressingHttpClient httpClient = new DecompressingHttpClient(rawHttpClient);
        return new QuestionsApi(httpClient);
    }

}
