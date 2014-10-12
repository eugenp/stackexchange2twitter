package org.tweet.meta.persistence.service;

import static org.junit.Assert.assertNotNull;

import org.common.persistence.AbstractRawServicePersistenceIntegrationTest;
import org.common.persistence.IEntityOperations;
import org.common.spring.MyApplicationContextInitializerProv;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keyval.spring.KeyValPersistenceJPAConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.util.TwitterAccountEnum;
import org.tweet.meta.persistence.dao.IRetweetJpaDAO;
import org.tweet.meta.persistence.model.Retweet;
import org.tweet.meta.spring.TwitterMetaPersistenceJPAConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {// @formatter:off
    KeyValPersistenceJPAConfig.class, 
    
    TwitterMetaPersistenceJPAConfig.class, 
}) // @formatter:on
public class RetweetPersistenceIntegrationTest extends AbstractRawServicePersistenceIntegrationTest<Retweet> {

    @Autowired
    private IRetweetJpaDAO api;

    @Autowired
    private RetweetEntityOps retweetEntityOps;

    // fixtures

    @BeforeClass
    public static void beforeClass() {
        System.setProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY, "test");
        System.out.println(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY + ": " + System.getProperty(MyApplicationContextInitializerProv.PERSISTENCE_TARGET_KEY));
    }

    // tests

    @Test
    public void givenEntityExistsWithSameTextAndAccount_whenEntityIsRetrieved_thenEntityIsFound() {
        // Given
        final Retweet existingEntity = persistNewEntity();

        // When
        final Retweet found = getApi().findOneByTextAndTwitterAccount(existingEntity.getText(), existingEntity.getTwitterAccount());

        // Then
        assertNotNull(found);
    }

    @Test
    public void givenEntityExistsAsRtMention_whenSimilarEntityIsSearched_thenEntityIsFound() {
        // Given
        final Retweet newEntity = createNewEntity();
        final String textOfTweet = "Text of tweet bla1";
        newEntity.setText("RT @someuser: " + textOfTweet);
        final Retweet existingEntity = getApi().save(newEntity);

        // When
        final Retweet found = getApi().findOneByTextEndsWithAndTwitterAccount(textOfTweet, existingEntity.getTwitterAccount());

        // Then
        assertNotNull(found);
    }

    // count

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

    // update

    // delete

    // template method

    @Override
    protected IRetweetJpaDAO getApi() {
        return api;
    }

    @Override
    protected Retweet persistNewEntity() {
        return getApi().save(createNewEntity());
    }

    @Override
    protected IEntityOperations<Retweet> getEntityOps() {
        return retweetEntityOps;
    }

}
