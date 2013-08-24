package org.stackexchange.util;

public enum TwitterAccountEnum {// @formatter:off
    AskUbuntuBest(false), // SO specific
    AspnetDaily(false), 
    AndroidFact(true), // 18.08 - temporarily true - just to see what's what with #android
    
    BestAlgorithms(true), 
    BestAWS(true), 
    BestBash(false),  
    BestClojure(true), 
    BestEclipse(false), // predefined some accounts
    BestGit(true), // temp true
    BestJavaScript(true),  
    BestJPA(false), // predefined some accounts
    BestJSP(false), // predefined some accounts
    BestJSON(true), 
    BestMaven(false),  
    BestMultithread(true), 
    BestNoSQL(true), 
    BestPHP(true), 
    BestOfCloud(true), 
    BestOfCocoa(false), // newly false because cocoa has multiple meanings 
    BestOfCss(true), 
    BestOfJava(true), 
    BestOfLinux(true), 
    BestOfRuby(false), 
    BestOfSecurity(true), 
    BestRubyOnRails(true), 
    BestSQL(true), 
    BestScala(true), 
    BestXML(true), 
    BestWPF(false), 

    CryptoFact(true),
    
    DjangoDaily(false), 
    
    FacebookDigest(true), 
    
    GoogleDigest(true), 

    HadoopDaily(true), 
    HibernateDaily(false), 
    HTMLdaily(true), 
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
    
    ParsingDaily(false), // the hashtag (parsing) doesn't return any results
    PerlDaily(true),
    PythonDaily(true), 
    
    RESTDaily(false), // 
    RegexDaily(true), 
    
    ServerFaultBest(false),  // SO specific
    SpringAtSO(false); // SO specific
    
    private final boolean rt; 
    
    TwitterAccountEnum(final boolean rt) {
        this.rt = rt;
    }

    public boolean isRt() {
        return rt;
    }
}
// @formatter:on