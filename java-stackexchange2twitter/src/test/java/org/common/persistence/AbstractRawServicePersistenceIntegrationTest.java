package org.common.persistence;

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
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.stackexchange.util.IDUtil;
import org.tweet.spring.util.SpringProfileUtil;

@ActiveProfiles(SpringProfileUtil.PERSISTENCE)
public abstract class AbstractRawServicePersistenceIntegrationTest<T extends IEntity> {

    // tests

    // find - one

    @Test
    /**/public final void givenResourceDoesNotExist_whenResourceIsRetrieved_thenNoResourceIsReceived() {
        // When
        final T createdResource = getApi().findOne(IDUtil.randomPositiveLong());

        // Then
        assertNull(createdResource);
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenNoExceptions() {
        final T existingResource = persistNewEntity();
        getApi().findOne(existingResource.getId());
    }

    @Test
    public void givenResourceDoesNotExist_whenResourceIsRetrieved_thenNoExceptions() {
        getApi().findOne(IDUtil.randomPositiveLong());
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenTheResultIsNotNull() {
        final T existingResource = persistNewEntity();
        final T retrievedResource = getApi().findOne(existingResource.getId());
        assertNotNull(retrievedResource);
    }

    @Test
    public void givenResourceExists_whenResourceIsRetrieved_thenResourceIsRetrievedCorrectly() {
        final T existingResource = persistNewEntity();
        final T retrievedResource = getApi().findOne(existingResource.getId());
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
        final Iterable<T> resources = getApi().findAll();

        assertNotNull(resources);
    }

    @Test
    /**/public void givenAtLeastOneResourceExists_whenAllResourcesAreRetrieved_thenRetrievedResourcesAreNotEmpty() {
        persistNewEntity();

        // When
        final List<T> allResources = (List<T>) getApi().findAll();

        // Then
        assertThat(allResources, not(Matchers.<T> empty()));
    }

    @Test
    /**/public void givenAnResourceExists_whenAllResourcesAreRetrieved_thenTheExistingResourceIsIndeedAmongThem() {
        final T existingResource = persistNewEntity();

        final Iterable<T> resources = getApi().findAll();

        assertThat(resources, hasItem(existingResource));
    }

    @Test
    /**/public void whenAllResourcesAreRetrieved_thenResourcesHaveIds() {
        persistNewEntity();

        // When
        final Iterable<T> allResources = getApi().findAll();

        // Then
        for (final T resource : allResources) {
            assertNotNull(resource.getId());
        }
    }

    @Test(expected = RuntimeException.class)
    /**/public void whenNullResourceIsCreated_thenException() {
        getApi().save((T) null);
    }

    @Test
    /**/public void whenResourceIsCreated_thenNoExceptions() {
        persistNewEntity();
    }

    @Test
    /**/public void whenResourceIsCreated_thenResourceIsRetrievable() {
        final T existingResource = persistNewEntity();

        assertNotNull(getApi().findOne(existingResource.getId()));
    }

    @Test
    /**/public void whenResourceIsCreated_thenSavedResourceIsEqualToOriginalResource() {
        final T originalResource = createNewEntity();
        final T savedResource = getApi().save(originalResource);

        assertEquals(originalResource, savedResource);
    }

    @Test(expected = RuntimeException.class)
    public void whenResourceWithFailedConstraintsIsCreated_thenException() {
        final T invalidResource = createNewEntity();
        getEntityOps().invalidate(invalidResource);

        getApi().save(invalidResource);
    }

    /**
     * -- specific to the persistence engine
     */
    @Test(expected = DataAccessException.class)
    @Ignore("Hibernate simply ignores the id silently and still saved (tracking this)")
    public void whenResourceWithIdIsCreated_thenDataAccessException() {
        final T resourceWithId = createNewEntity();
        resourceWithId.setId(IDUtil.randomPositiveLong());

        getApi().save(resourceWithId);
    }

    // update

    @Test(expected = RuntimeException.class)
    /**/public void whenNullResourceIsUpdated_thenException() {
        getApi().save((T) null);
    }

    @Test
    /**/public void givenResourceExists_whenResourceIsUpdated_thenNoExceptions() {
        // Given
        final T existingResource = persistNewEntity();

        // When
        getApi().save(existingResource);
    }

    /**
     * - can also be the ConstraintViolationException which now occurs on the update operation will not be translated; as a consequence, it will be a TransactionSystemException
     */
    @Test(expected = RuntimeException.class)
    public void whenResourceIsUpdatedWithFailedConstraints_thenException() {
        final T existingResource = persistNewEntity();
        getEntityOps().invalidate(existingResource);

        getApi().save(existingResource);
    }

    @Test
    /**/public void givenResourceExists_whenResourceIsUpdated_thenUpdatesArePersisted() {
        // Given
        final T existingResource = persistNewEntity();

        // When
        getEntityOps().change(existingResource);
        getApi().save(existingResource);

        final T updatedResource = getApi().findOne(existingResource.getId());

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
        final T existingResource = persistNewEntity();

        // When
        getApi().delete(existingResource.getId());
    }

    @Test
    /**/public final void givenResourceExists_whenResourceIsDeleted_thenResourceNoLongerExists() {
        // Given
        final T existingResource = persistNewEntity();

        // When
        getApi().delete(existingResource.getId());

        // Then
        assertNull(getApi().findOne(existingResource.getId()));
    }

    // template method

    protected T createNewEntity() {
        return getEntityOps().createNewEntity();
    }

    protected abstract IOperations<T> getApi();

    protected abstract IEntityOperations<T> getEntityOps();

    protected T persistNewEntity() {
        return getApi().save(createNewEntity());
    }

}
