package org.stackexchange.api.client;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stackexchange.api.constants.StackSite;

public class QuestionsApi {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String LINK = "link";
    public static final String TITLE = "title";
    public static final String QUESTION_ID = "question_id";

    private HttpClient client;

    public QuestionsApi(final HttpClient client) {
        super();

        this.client = client;
    }

    // API

    public final String questions(final int minScore, final StackSite stackSite, final int page) {
        final String questionsUri = ApiUris.getQuestionsUri(minScore, stackSite, page);
        logger.debug("Retrieving Questions on page= {} of stackSite= {} via URI= {}", page, stackSite.name(), questionsUri);
        try {
            return questionsInternal(minScore, questionsUri);
        } catch (final IOException ioEx) {
            logger.error("", ioEx);
        }

        return null;
    }

    public final String questions(final int minScore, final StackSite stackSite, final String tag, final int page) {
        final String questionsUriForTag = ApiUris.getTagUri(minScore, stackSite, tag, page);

        logger.debug("Retrieving Questions on page= {} of stackSite= {} via URI= {}", page, stackSite.name(), questionsUriForTag);
        try {
            return questionsInternal(minScore, questionsUriForTag);
        } catch (final IOException ioEx) {
            logger.error("", ioEx);
        }

        return null;
    }

    public final String questionById(final StackSite stackSite, final long id) {
        final String questionUriForId = ApiUris.getSingleQuestionUri(stackSite, id);

        logger.debug("Retrieving Question by id= {} of stackSite= {} via URI= {}", id, stackSite.name(), questionUriForId);
        try {
            return questionByIdInternal(questionUriForId);
        } catch (final IOException ioEx) {
            logger.error("", ioEx);
        }

        return null;
    }

    // non-API

    final String questionsInternal(final int min, final String questionsUri) throws IOException {
        HttpGet request = null;
        HttpEntity httpEntity = null;
        try {
            request = new HttpGet(questionsUri);
            final HttpResponse httpResponse = client.execute(request);
            httpEntity = httpResponse.getEntity();
            final InputStream entityContentStream = httpEntity.getContent();
            final String outputAsEscapedHtml = IOUtils.toString(entityContentStream, Charset.forName("utf-8"));
            return outputAsEscapedHtml;
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
            if (httpEntity != null) {
                EntityUtils.consume(httpEntity);
            }
        }
    }

    final String questionByIdInternal(final String questionUri) throws IOException {
        HttpGet request = null;
        HttpEntity httpEntity = null;
        try {
            request = new HttpGet(questionUri);
            final HttpResponse httpResponse = client.execute(request);
            httpEntity = httpResponse.getEntity();
            final InputStream entityContentStream = httpEntity.getContent();
            final String outputAsEscapedHtml = IOUtils.toString(entityContentStream, Charset.forName("utf-8"));
            return outputAsEscapedHtml;
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
            if (httpEntity != null) {
                EntityUtils.consume(httpEntity);
            }
        }
    }

    final String questions(final int min, final StackSite site) {
        return questions(min, site, 1);
    }

    final HttpResponse questionsAsResponse(final int min, final StackSite site) {
        try {
            return client.execute(new HttpGet(ApiUris.getQuestionsUri(min, site)));
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

}
