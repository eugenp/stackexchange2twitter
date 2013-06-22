package org.common.service;

import org.springframework.stereotype.Service;

import com.google.api.client.util.Preconditions;

@Service
public class ContentExtractorService {

    public ContentExtractorService() {
        super();
    }

    // API

    public final String extractTitle(final String url) {
        Preconditions.checkNotNull(url);

        return null;
    }

}
