package org.rss.service;

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
import org.rss.persistence.dao.IRssEntryJpaDAO;
import org.rss.persistence.model.RssEntry;
import org.rss.spring.RssPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.IDUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RssPersistenceJPAConfig.class })
public class RssEntryPersistenceIntegrationTest {

    @Autowired
    private IRssEntryJpaDAO api;

    // tests

    @Test
    /**/public final void givenResourceDoesNotExist_whenResourceIsRetrieved_thenNoResourceIsReceived() {
        // When
        final RssEntry createdResource = getApi().findOne(IDUtil.randomPositiveLong());

        // Then
        assertNull(createdResource);
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenNoExceptions() {
        final RssEntry existingResource = persistNewEntity();
        getApi().findOne(existingResource.getId());
    }

    @Test
    public void givenResourceDoesNotExist_whenResourceIsRetrieved_thenNoExceptions() {
        getApi().findOne(IDUtil.randomPositiveLong());
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenTheResultIsNotNull() {
        final RssEntry existingResource = persistNewEntity();
        final RssEntry retrievedResource = getApi().findOne(existingResource.getId());
        assertNotNull(retrievedResource);
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenResourceIsRetrievedCorrectly() {
        final RssEntry existingResource = persistNewEntity();
        final RssEntry retrievedResource = getApi().findOne(existingResource.getId());
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
        final List<RssEntry> resources = getApi().findAll();

        assertNotNull(resources);
    }

    @Test
    /**/public void givenAtLeastOneResourceExists_whenAllResourcesAreRetrieved_thenRetrievedResourcesAreNotEmpty() {
        persistNewEntity();

        // When
        final List<RssEntry> allResources = getApi().findAll();

        // Then
        assertThat(allResources, not(Matchers.<RssEntry> empty()));
    }

    @Test
    /**/public void givenAnResourceExists_whenAllResourcesAreRetrieved_thenTheExistingResourceIsIndeedAmongThem() {
        final RssEntry existingResource = persistNewEntity();

        final List<RssEntry> resources = getApi().findAll();

        assertThat(resources, hasItem(existingResource));
    }

    @Test
    /**/public void whenAllResourcesAreRetrieved_thenResourcesHaveIds() {
        persistNewEntity();

        // When
        final List<RssEntry> allResources = getApi().findAll();

        // Then
        for (final RssEntry resource : allResources) {
            assertNotNull(resource.getId());
        }
    }

    // count - all

    @Test
    /**/public void whenAllResourcesAreCounted_thenNoExceptions() {
        getApi().count();
    }

    // create

    @Test(expected = RuntimeException.class)
    /**/public void whenNullResourceIsCreated_thenException() {
        getApi().save((RssEntry) null);
    }

    @Test
    /**/public void whenResourceIsCreated_thenNoExceptions() {
        persistNewEntity();
    }

    @Test
    /**/public void whenResourceIsCreated_thenResourceIsRetrievable() {
        final RssEntry existingResource = persistNewEntity();

        assertNotNull(getApi().findOne(existingResource.getId()));
    }

    @Test
    /**/public void whenResourceIsCreated_thenSavedResourceIsEqualToOriginalResource() {
        final RssEntry originalResource = createNewEntity();
        final RssEntry savedResource = getApi().save(originalResource);

        assertEquals(originalResource, savedResource);
    }

    @Test(expected = RuntimeException.class)
    public void whenResourceWithFailedConstraintsIsCreated_thenException() {
        final RssEntry invalidResource = createNewEntity();
        invalidate(invalidResource);

        getApi().save(invalidResource);
    }

    // update

    @Test(expected = RuntimeException.class)
    /**/public void whenNullResourceIsUpdated_thenException() {
        getApi().save((RssEntry) null);
    }

    @Test
    /**/public void givenResourceExists_whenResourceIsUpdated_thenNoExceptions() {
        // Given
        final RssEntry existingResource = persistNewEntity();

        // When
        getApi().save(existingResource);
    }

    /**
     * - can also be the ConstraintViolationException which now occurs on the update operation will not be translated; as a consequence, it will be a TransactionSystemException
     */
    @Test(expected = RuntimeException.class)
    public void whenResourceIsUpdatedWithFailedConstraints_thenException() {
        final RssEntry existingResource = persistNewEntity();
        invalidate(existingResource);

        getApi().save(existingResource);
    }

    @Test
    /**/public void givenResourceExists_whenResourceIsUpdated_thenUpdatesArePersisted() {
        // Given
        final RssEntry existingResource = persistNewEntity();

        // When
        chance(existingResource);
        getApi().save(existingResource);

        final RssEntry updatedResource = getApi().findOne(existingResource.getId());

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
        final RssEntry existingResource = persistNewEntity();

        // When
        getApi().delete(existingResource.getId());
    }

    @Test
    /**/public final void givenResourceExists_whenResourceIsDeleted_thenResourceNoLongerExists() {
        // Given
        final RssEntry existingResource = persistNewEntity();

        // When
        getApi().delete(existingResource.getId());

        // Then
        assertNull(getApi().findOne(existingResource.getId()));
    }

    // template method

    protected IRssEntryJpaDAO getApi() {
        return api;
    }

    protected void chance(final RssEntry entity) {
        entity.setRssUri(randomAlphabetic(6));
    }

    protected void invalidate(final RssEntry entity) {
        entity.setRssUri(null);
    }

    protected RssEntry createNewEntity() {
        return new RssEntry(randomAlphabetic(6), randomAlphabetic(6), randomAlphabetic(6));
    }

    protected RssEntry persistNewEntity() {
        return getApi().save(createNewEntity());
    }

}
