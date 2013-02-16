package org.tweet.spring;

import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.stackexchange.api.client.QuestionsApi;

@Configuration
public class StackexchangeConfig {

    @Bean
    public QuestionsApi questionsApi() {
        return new QuestionsApi(new DecompressingHttpClient(new DefaultHttpClient()));
    }

}
