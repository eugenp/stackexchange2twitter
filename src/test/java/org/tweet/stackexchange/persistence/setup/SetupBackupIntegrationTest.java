package org.tweet.stackexchange.persistence.setup;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tweet.spring.SetupPersistenceTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SetupPersistenceTestConfig.class })
public class SetupBackupIntegrationTest {
    class TweetRowMapper implements RowMapper<String> {
        @Override
        public final String mapRow(final ResultSet rs, final int line) throws SQLException {
            System.out.println();
            return "";
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // fixtures

    @Test
    public final void whenSetupContextIsBootstrapped_thenNoExceptions() {
        //
    }

    @Test
    public final void whenQuestionsAreRetrievedFromTheDB_thenNoExceptions() {
        jdbcTemplate.query("SELECT * FROM question_tweet;", new TweetRowMapper());
    }

}
