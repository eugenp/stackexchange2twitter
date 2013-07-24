package org.tweet.meta.persistence.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.IDUtil;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
    KeyValPersistenceJPAConfig.class, 
    
    TwitterMetaPersistenceJPAConfig.class, 
}) // @formatter:on
public class RetweetPersistenceIntegrationTest {

    @Autowired
    private IRetweetJpaDAO api;

    // tests

    @Test
    /**/public final void givenResourceDoesNotExist_whenResourceIsRetrieved_thenNoResourceIsReceived() {
        // When
        final Retweet createdResource = getApi().findOne(IDUtil.randomPositiveLong());

        // Then
        assertNull(createdResource);
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenNoExceptions() {
        final Retweet existingResource = persistNewEntity();
        getApi().findOne(existingResource.getId());
    }

    @Test
    public void givenResourceDoesNotExist_whenResourceIsRetrieved_thenNoExceptions() {
        getApi().findOne(IDUtil.randomPositiveLong());
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenTheResultIsNotNull() {
        final Retweet existingResource = persistNewEntity();
        final Retweet retrievedResource = getApi().findOne(existingResource.getId());
        assertNotNull(retrievedResource);
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenResourceIsRetrievedCorrectly() {
        final Retweet existingResource = persistNewEntity();
        final Retweet retrievedResource = getApi().findOne(existingResource.getId());
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
        final List<Retweet> resources = getApi().findAll();

        assertNotNull(resources);
    }

    @Test
    /**/public void givenAtLeastOneResourceExists_whenAllResourcesAreRetrieved_thenRetrievedResourcesAreNotEmpty() {
        persistNewEntity();

        // When
        final List<Retweet> allResources = getApi().findAll();

        // Then
        assertThat(allResources, not(Matchers.<Retweet> empty()));
    }

    @Test
    /**/public void givenAnResourceExists_whenAllResourcesAreRetrieved_thenTheExistingResourceIsIndeedAmongThem() {
        final Retweet existingResource = persistNewEntity();

        final List<Retweet> resources = getApi().findAll();

        assertThat(resources, hasItem(existingResource));
    }

    @Test
    /**/public void whenAllResourcesAreRetrieved_thenResourcesHaveIds() {
        persistNewEntity();

        // When
        final List<Retweet> allResources = getApi().findAll();

        // Then
        for (final Retweet resource : allResources) {
            assertNotNull(resource.getId());
        }
    }

    // count - all

    @Test
    /**/public void whenAllResourcesAreCounted_thenNoExceptions() {
        getApi().count();
    }

    @Test
    /**/public void whenAllResourcesAreCountedByTwitterAccount_thenNoExceptions() {
        final long countAllByTwitterAccount = getApi().countAllByTwitterAccount(TwitterAccountEnum.AskUbuntuBest.name());
        System.out.println(countAllByTwitterAccount);
    }

    // create

    @Test(expected = RuntimeException.class)
    /**/public void whenNullResourceIsCreated_thenException() {
        getApi().save((Retweet) null);
    }

    @Test
    /**/public void whenResourceIsCreated_thenNoExceptions() {
        persistNewEntity();
    }

    @Test
    /**/public void whenResourceIsCreated_thenResourceIsRetrievable() {
        final Retweet existingResource = persistNewEntity();

        assertNotNull(getApi().findOne(existingResource.getId()));
    }

    @Test
    /**/public void whenResourceIsCreated_thenSavedResourceIsEqualToOriginalResource() {
        final Retweet originalResource = createNewEntity();
        final Retweet savedResource = getApi().save(originalResource);

        assertEquals(originalResource, savedResource);
    }

    @Test(expected = RuntimeException.class)
    public void whenResourceWithFailedConstraintsIsCreated_thenException() {
        final Retweet invalidResource = createNewEntity();
        invalidate(invalidResource);

        getApi().save(invalidResource);
    }

    // update

    @Test(expected = RuntimeException.class)
    /**/public void whenNullResourceIsUpdated_thenException() {
        getApi().save((Retweet) null);
    }

    @Test
    /**/public void givenResourceExists_whenResourceIsUpdated_thenNoExceptions() {
        // Given
        final Retweet existingResource = persistNewEntity();

        // When
        getApi().save(existingResource);
    }

    /**
     * - can also be the ConstraintViolationException which now occurs on the update operation will not be translated; as a consequence, it will be a TransactionSystemException
     */
    @Test(expected = RuntimeException.class)
    public void whenResourceIsUpdatedWithFailedConstraints_thenException() {
        final Retweet existingResource = persistNewEntity();
        invalidate(existingResource);

        getApi().save(existingResource);
    }

    @Test
    /**/public void givenResourceExists_whenResourceIsUpdated_thenUpdatesArePersisted() {
        // Given
        final Retweet existingResource = persistNewEntity();

        // When
        change(existingResource);
        getApi().save(existingResource);

        final Retweet updatedResource = getApi().findOne(existingResource.getId());

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
        final Retweet existingResource = persistNewEntity();

        // When
        getApi().delete(existingResource.getId());
    }

    @Test
    /**/public final void givenResourceExists_whenResourceIsDeleted_thenResourceNoLongerExists() {
        // Given
        final Retweet existingResource = persistNewEntity();

        // When
        getApi().delete(existingResource.getId());

        // Then
        assertNull(getApi().findOne(existingResource.getId()));
    }

    // template method

    protected IRetweetJpaDAO getApi() {
        return api;
    }

    protected void change(final Retweet entity) {
        entity.setText(randomAlphabetic(6));
    }

    protected void invalidate(final Retweet entity) {
        entity.setText(null);
        entity.setTwitterAccount(null);
    }

    protected Retweet createNewEntity() {
        return new Retweet(IDUtil.randomPositiveLong(), randomAlphabetic(6), randomAlphabetic(6));
    }

    protected Retweet persistNewEntity() {
        return getApi().save(createNewEntity());
    }

}
