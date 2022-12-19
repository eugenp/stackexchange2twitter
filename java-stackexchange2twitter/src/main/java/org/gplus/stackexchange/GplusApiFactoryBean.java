package org.gplus.stackexchange;

import java.io.File;
import java.util.Collections;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.PlusScopes;

@Component
public class GplusApiFactoryBean implements FactoryBean<Plus> {

    private HttpTransport httpTransport;

    private final JsonFactory jsonFactory = new JacksonFactory();

    /** E-mail address of the service account. */
    private final String serviceAccountEmail = "197652125488-ju7ev3nc4lcmdlhr83gjrhb7gk03g552@developer.gserviceaccount.com";

    private final String APPLICATION_NAME = "BestScala";

    // API

    @Override
    public Plus getObject() throws Exception {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        // service account credential (uncomment setServiceAccountUser for domain-wide delegation)
        final GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport).setJsonFactory(jsonFactory).setServiceAccountId(serviceAccountEmail).setServiceAccountScopes(Collections.singleton(PlusScopes.PLUS_ME))
                .setServiceAccountPrivateKeyFromP12File(new File("key.p12"))
                // .setServiceAccountUser("user@example.com")
                .build();

        return new Plus.Builder(httpTransport, jsonFactory, credential).setApplicationName(APPLICATION_NAME).build();
    }

    @Override
    public Class<Plus> getObjectType() {
        return Plus.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
