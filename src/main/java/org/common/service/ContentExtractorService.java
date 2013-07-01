package org.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.stackexchange.api.client.HttpFactory;

import com.google.api.client.util.Preconditions;

@Service
public class ContentExtractorService implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private DefaultHttpClient client;

    public ContentExtractorService() {
        super();
    }

    // API

    /**
     * - note: may return null
     */
    public final String extractTitle(final String sourceUrl) {
        try {
            final String contentsAtUri = getContentsAtUri(sourceUrl);
            return extractTitleInternal(contentsAtUri);
        } catch (final IOException ex) {
            logger.error("Unable to extract title from: " + sourceUrl, ex);
            return null;
        }
    }

    // util

    final String getContentsAtUri(final String sourceUrl) throws IOException {
        Preconditions.checkNotNull(sourceUrl);

        HttpGet request = null;
        InputStream entityContentStream = null;
        HttpEntity httpEntity = null;
        try {
            request = new HttpGet(sourceUrl);
            final HttpResponse httpResponse = client.execute(request);
            httpEntity = httpResponse.getEntity();
            entityContentStream = httpEntity.getContent();
            final String outputAsEscapedHtml = IOUtils.toString(entityContentStream, Charset.forName("utf-8"));
            return outputAsEscapedHtml;
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
            if (entityContentStream != null) {
                entityContentStream.close();
            }
            if (httpEntity != null) {
                EntityUtils.consume(httpEntity);
            }
        }
    }

    final String extractTitleInternal(final String contents) {
        Preconditions.checkNotNull(contents);

        final Source source = new Source(contents);
        final Element titleElement = source.getFirstElement(HTMLElementName.TITLE);
        if (titleElement == null) {
            return null;
        }

        // TITLE element never contains other tags so just decode it collapsing whitespace
        return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
    }

    // spring

    @Override
    public final void afterPropertiesSet() {
        client = HttpFactory.httpClient(true);
    }

}
