package org.common.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Preconditions;

@Service
public class ContentExtractorService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ContentExtractorService() {
        super();
    }

    // API

    /**
     * - note: may return null
     */
    public final String extractTitle(final String sourceUrl) {
        Preconditions.checkNotNull(sourceUrl);

        final Source source;
        try {
            source = new Source(new URL(sourceUrl));
        } catch (final FileNotFoundException ex) {
            logger.warn("404 resulted from the URL: " + sourceUrl);
            logger.debug("404 resulted from the URL: " + sourceUrl, ex);
            return null;
        } catch (final IOException ex) {
            logger.error("", ex);
            return null;
        }

        final Element titleElement = source.getFirstElement(HTMLElementName.TITLE);
        if (titleElement == null) {
            return null;
        }

        // TITLE element never contains other tags so just decode it collapsing whitespace
        return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
    }
}
