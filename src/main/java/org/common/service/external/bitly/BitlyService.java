package org.common.service.external.bitly;

import net.swisstech.bitly.BitlyClient;
import net.swisstech.bitly.model.Response;
import net.swisstech.bitly.model.v3.ShortenResponse;

import org.springframework.stereotype.Service;

@Service
public class BitlyService {

    public BitlyService() {
        super();
    }

    // API

    @SuppressWarnings("unused")
    public final String shortenUrl(final String urlLarge) {
        final String user = "21561954926d6a8f9b86165be007c4568d1affcd";
        final String apiKey = "3139531e5285fe0fdb930deb2b1c52eb2ba8973d";
        final String accessToken = "caf0026313bcf9709abfcc08a2270b55953bc95c";

        final BitlyClient client = new BitlyClient(accessToken);
        final Response<ShortenResponse> shortenResponse = client.shorten().setLongUrl(urlLarge).call();

        return shortenResponse.data.url;
    }

}
