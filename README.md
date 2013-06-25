stackexchange2twitter
=====================

Twitter Bot that tweets the Top Questions from various StackExchange Q&amp;A Sites


# Goals
- the project is very new and still in progress (this is uptodate)
- the project is **not stand-alone** - it uses the [java-stackexchange](https://github.com/eugenp/java-stackexchange) project to talk to the StackExchange APIs


# Technologies
- [Spring Social](https://github.com/SpringSource/spring-social/wiki/Quick-Start) is used for integration with **Twitter**


# Introducing a new account
- add the account to `SimpleTwitterAccount`
- add a tag into `Tag` (or `TagAdvanced`)
- add the words to become hashtags into `twitterInternal.properties`
- create the connection between the twitter account and the stackexchange site: `TwitterAccountToStackAccount`
- add the twitter account OAuth credentials in /opt/stack/twitter.properties
- add a test into `TwitterTemplateCreatorLiveTest`