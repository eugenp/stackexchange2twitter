package org.common.service.external.bitly;

import net.swisstech.bitly.BitlyClient;
import net.swisstech.bitly.model.Response;
import net.swisstech.bitly.model.v3.ShortenResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BitlyService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BitlyService() {
        super();
    }

    // API

    public final String shortenUrl(final String urlLarge) {
        try {
            return shortenUrlInternal(urlLarge);
        } catch (final RuntimeException runEx) {
            logger.error("Unable to shorten url= " + urlLarge + " via bitly!", runEx);
            return urlLarge;
        }
    }

    private final String shortenUrlInternal(final String urlLarge) {
        final String accessToken = "caf0026313bcf9709abfcc08a2270b55953bc95c";
        final Response<ShortenResponse> shortenResponse = new BitlyClient(accessToken).shorten().setLongUrl(urlLarge).call();
        return shortenResponse.data.url;
    }

}
