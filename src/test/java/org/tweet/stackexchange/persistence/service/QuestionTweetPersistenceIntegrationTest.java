package org.tweet.stackexchange.persistence.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.PersistenceJPAConfig;
import org.tweet.stackexchange.persistence.dao.IQuestionTweetJpaDAO;
import org.tweet.stackexchange.persistence.model.QuestionTweet;
import org.tweet.stackexchange.util.IDUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJPAConfig.class })
public class QuestionTweetPersistenceIntegrationTest {

    @Autowired
    private IQuestionTweetJpaDAO api;

    // tests

    @Test
    /**/public final void givenResourceDoesNotExist_whenResourceIsRetrieved_thenNoResourceIsReceived() {
        // When
        final QuestionTweet createdResource = getApi().findOne(IDUtil.randomPositiveLong());

        // Then
        assertNull(createdResource);
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenNoExceptions() {
        final QuestionTweet existingResource = persistNewEntity();
        getApi().findOne(existingResource.getId());
    }

    @Test
    public void givenResourceDoesNotExist_whenResourceIsRetrieved_thenNoExceptions() {
        getApi().findOne(IDUtil.randomPositiveLong());
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenTheResultIsNotNull() {
        final QuestionTweet existingResource = persistNewEntity();
        final QuestionTweet retrievedResource = getApi().findOne(existingResource.getId());
        assertNotNull(retrievedResource);
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenResourceIsRetrievedCorrectly() {
        final QuestionTweet existingResource = persistNewEntity();
        final QuestionTweet retrievedResource = getApi().findOne(existingResource.getId());
        assertEquals(existingResource, retrievedResource);
    }

    // find - one - by name

    // find - all

    @Test
    /**/public void whenAllResourcesAreRetrieved_thenNoExceptions() {
        getApi().findAll();
    }

    @Test
    /**/public void whenAllResourcesAreRetrieved_thenTheResultIsNotNull() {
        final List<QuestionTweet> resources = getApi().findAll();

        assertNotNull(resources);
    }

    @Test
    /**/public void givenAtLeastOneResourceExists_whenAllResourcesAreRetrieved_thenRetrievedResourcesAreNotEmpty() {
        persistNewEntity();

        // When
        final List<QuestionTweet> allResources = getApi().findAll();

        // Then
        assertThat(allResources, not(Matchers.<QuestionTweet> empty()));
    }

    @Test
    /**/public void givenAnResourceExists_whenAllResourcesAreRetrieved_thenTheExistingResourceIsIndeedAmongThem() {
        final QuestionTweet existingResource = persistNewEntity();

        final List<QuestionTweet> resources = getApi().findAll();

        assertThat(resources, hasItem(existingResource));
    }

    @Test
    /**/public void whenAllResourcesAreRetrieved_thenResourcesHaveIds() {
        persistNewEntity();

        // When
        final List<QuestionTweet> allResources = getApi().findAll();

        // Then
        for (final QuestionTweet resource : allResources) {
            assertNotNull(resource.getId());
        }
    }

    // create

    @Test(expected = RuntimeException.class)
    /**/public void whenNullResourceIsCreated_thenException() {
        getApi().save((QuestionTweet) null);
    }

    @Test
    /**/public void whenResourceIsCreated_thenNoExceptions() {
        persistNewEntity();
    }

    @Test
    /**/public void whenResourceIsCreated_thenResourceIsRetrievable() {
        final QuestionTweet existingResource = persistNewEntity();

        assertNotNull(getApi().findOne(existingResource.getId()));
    }

    @Test
    /**/public void whenResourceIsCreated_thenSavedResourceIsEqualToOriginalResource() {
        final QuestionTweet originalResource = createNewEntity();
        final QuestionTweet savedResource = getApi().save(originalResource);

        assertEquals(originalResource, savedResource);
    }

    @Test(expected = RuntimeException.class)
    public void whenResourceWithFailedConstraintsIsCreated_thenException() {
        final QuestionTweet invalidResource = createNewEntity();
        invalidate(invalidResource);

        getApi().save(invalidResource);
    }

    /**
     * -- specific to the persistence engine
     */
    @Test(expected = DataAccessException.class)
    @Ignore("Hibernate simply ignores the id silently and still saved (tracking this)")
    public void whenResourceWithIdIsCreated_thenDataAccessException() {
        final QuestionTweet resourceWithId = createNewEntity();
        resourceWithId.setId(IDUtil.randomPositiveLong());

        getApi().save(resourceWithId);
    }

    // update

    @Test(expected = RuntimeException.class)
    /**/public void whenNullResourceIsUpdated_thenException() {
        getApi().save((QuestionTweet) null);
    }

    @Test
    /**/public void givenResourceExists_whenResourceIsUpdated_thenNoExceptions() {
        // Given
        final QuestionTweet existingResource = persistNewEntity();

        // When
        getApi().save(existingResource);
    }

    /**
     * - can also be the ConstraintViolationException which now occurs on the update operation will not be translated; as a consequence, it will be a TransactionSystemException
     */
    @Test(expected = RuntimeException.class)
    public void whenResourceIsUpdatedWithFailedConstraints_thenException() {
        final QuestionTweet existingResource = persistNewEntity();
        invalidate(existingResource);

        getApi().save(existingResource);
    }

    @Test
    /**/public void givenResourceExists_whenResourceIsUpdated_thenUpdatesArePersisted() {
        // Given
        final QuestionTweet existingResource = persistNewEntity();

        // When
        chance(existingResource);
        getApi().save(existingResource);

        final QuestionTweet updatedResource = getApi().findOne(existingResource.getId());

        // Then
        assertEquals(existingResource, updatedResource);
    }

    // delete

    @Test(expected = RuntimeException.class)
    public void givenResourceDoesNotExists_whenResourceIsDeleted_thenException() {
        // When
        getApi().delete(IDUtil.randomPositiveLong());
    }

    @Test(expected = RuntimeException.class)
    public void whenResourceIsDeletedByNegativeId_thenException() {
        // When
        getApi().delete(IDUtil.randomNegativeLong());
    }

    @Test
    public void givenResourceExists_whenResourceIsDeleted_thenNoExceptions() {
        // Given
        final QuestionTweet existingResource = persistNewEntity();

        // When
        getApi().delete(existingResource.getId());
    }

    @Test
    /**/public final void givenResourceExists_whenResourceIsDeleted_thenResourceNoLongerExists() {
        // Given
        final QuestionTweet existingResource = persistNewEntity();

        // When
        getApi().delete(existingResource.getId());

        // Then
        assertNull(getApi().findOne(existingResource.getId()));
    }

    // template method

    protected IQuestionTweetJpaDAO getApi() {
        return api;
    }

    protected void chance(final QuestionTweet entity) {
        entity.setQuestionId(randomAlphabetic(6));
    }

    protected void invalidate(final QuestionTweet entity) {
        entity.setQuestionId(null);
    }

    protected QuestionTweet createNewEntity() {
        return new QuestionTweet(randomAlphabetic(6));
    }

    protected QuestionTweet persistNewEntity() {
        return getApi().save(createNewEntity());
    }

}
