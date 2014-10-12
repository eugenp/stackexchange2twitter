package org.stackexchange.util;

public enum TwitterAccountEnum {// @formatter:off

    AskUbuntuFact(false), // SO specific
    AspnetDaily(true), // new - only partially true (for some keywords)
    AndroidFact(true), // 18.08 - temporarily true - just to see what's what with #android

    BestAlgorithms(true),
    BestAWS(true),
    BashWatch(false),
    ClojureFact(true),
    EclipseFacts(false), // predefined some accounts
    BestGit(true), // temp true
    ThinkJavaScript(true),
    BestJPA(true),
    BestJSP(false), // predefined some accounts
    BestJSON(true),
    MavenFact(false),
    MultithreadFact(true),
    BestNoSQL(true),
    BestPHP(true),
    CloudDaily(true),
    BestOfCocoa(false), // newly false because cocoa has multiple meanings
    CssFact(true),
    JavaFact(true),
    LinuxFact(true),
    BestOfRuby(false),
    SecurityFact(true),
    BestRubyOnRails(true),
    BestSQL(true),
    ScalaFact(true),
    BestXML(true),
    BestWPF(false),

    CryptoFact(true),

    DjangoDaily(false),

    DotNetFact(true),

    FacebookDigest(true),

    GoogleDigest(true),

    HadoopDaily(true),
    HibernateDaily(false),
    HTMLdaily(true),
    HttpClient4(true),
    BestOfHTML5(true),

    iOSdigest(true),
    InTheAppleWorld(true),

    JavaTopSO(false), // SO specific
    jQueryDaily(true),

    LispDaily(true),
    LandOfSeo(true), // not yet sure
    LandOfWordpress(true),

    MathDaily(true),
    MysqlDaily(true),

    ObjectiveCDaily(true),

    ParsingDaily(true), // the hashtag (parsing) doesn't return any results - but it can't hurt either
    PerlDaily(true),
    PerformanceTip(true),
    PythonDaily(true),

    RESTDaily(false), //
    RegexDaily(true),

    ServerFaultFact(false),  // SO specific
    SpringTip(true),

    thedogbreeds(true, false);

    private final boolean rt;
    private final boolean technical;

    TwitterAccountEnum(final boolean rt, final boolean technical) {
        this.rt = rt;
        this.technical = technical;
    }

    TwitterAccountEnum(final boolean rt) {
        this(rt, true);
    }

    public boolean isRt() {
        return rt;
    }

    public boolean isTechnical() {
        return technical;
    }

}
// @formatter:on