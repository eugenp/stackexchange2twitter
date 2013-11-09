package org.stackexchange;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.common.spring.CommonServiceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.stackexchange.spring.StackexchangeContextConfig;
import org.stackexchange.util.TwitterAccountEnum;
import org.stackexchange.util.TwitterTag;
import org.tweet.spring.TwitterConfig;
import org.tweet.spring.TwitterLiveConfig;
import org.tweet.spring.util.SpringProfileUtil;
import org.tweet.twitter.service.live.UserLiveService;

import com.fasterxml.jackson.core.JsonProcessingException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {//@formatter:off
    CommonServiceConfig.class, 
    
    TwitterConfig.class, 
    TwitterLiveConfig.class, 
    
    StackexchangeContextConfig.class 
}) //@formatter:on
@ActiveProfiles(SpringProfileUtil.LIVE)
public class UserLiveServiceReadOnlyLiveTest {

    @Autowired
    private UserLiveService instance;

    // tests

    @Test
    public final void whenRetrievingTwitterProfile_thenNoxceptions() throws JsonProcessingException, IOException {
        final TwitterProfile profileOfUser = instance.getProfileOfUser("selsaber");
        System.out.println(profileOfUser);
        System.out.println(profileOfUser.getLanguage());
    }

    // users - multiple

    @Test
    public final void whenUsersAreSearchedByKeyword1_thenSomeUsersAreFound() {
        final List<TwitterProfile> usersByTag = instance.searchForUsers(TwitterTag.scala.name());
        assertThat(usersByTag, not(empty()));
    }

    // users - many requests - multiple

    @Test
    public final void whenMoreThanOnePageUsersAreSearchedByKeyword_thenCorrectNumberOfUsersIsFound() {
        final List<TwitterProfile> usersByTag = instance.searchForUsers(TwitterTag.scala.name(), 3);
        assertThat(usersByTag, hasSize(60));
    }

    // friends

    @Test
    public final void whenFriendIdsAreRetrievedFromAccount1_thenCorrect() {
        final TwitterProfile account = instance.getProfileOfUser("GreatestQuotes");
        final Collection<Long> friendsOfHashtag = instance.getFriendIds(account, 2);
        assertThat(friendsOfHashtag.size(), equalTo(10000));
    }

    @Test
    public final void whenFriendIdsAreRetrievedFromAccount2_thenCorrect() {
        final TwitterProfile account = instance.getProfileOfUser("skillsmatter");
        final Collection<Long> friendsOfHashtag = instance.getFriendIds(account, 2);
        assertThat(friendsOfHashtag.size(), greaterThan(3300));
    }

    @Test
    public final void whenFriendIdsAreRetrievedFromAccount3_thenCorrect() {
        final TwitterProfile account = instance.getProfileOfUser("davesbargains");
        final Collection<Long> friendsOfHashtag = instance.getFriendIds(account, 2);
        assertThat(friendsOfHashtag.size(), greaterThan(8000));
    }

    // follower ids

    @Test
    public final void whenFollowerIdsAreRetrievedForAccount_thenNoExceptions() {
        instance.getFollowerIdsOfMyAccount(TwitterAccountEnum.BestScala.name());
    }

    @Test
    public final void whenFollowerIdsAreRetrievedForAccount_thenCorrectNumberOfIds() {
        final Set<Long> followerIdsOfMyAccount = instance.getFollowerIdsOfMyAccount(TwitterAccountEnum.BestScala.name());
        assertThat(followerIdsOfMyAccount, hasSize(greaterThan(50)));
    }

}
