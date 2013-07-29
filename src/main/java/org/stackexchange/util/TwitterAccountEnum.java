package org.stackexchange.util;

public enum TwitterAccountEnum {// @formatter:off
    AskUbuntuBest(false), // SO specific
    AspnetDaily(false), // 
    
    BestAlgorithms(true), 
    BestAWS(true), 
    BestBash(false), // 
    BestClojure(true), 
    BestEclipse(false), // predefined some accounts
    BestGit(false), // predefined some accounts
    BestJavaScript(true),  
    BestJPA(false), // 
    BestJSP(false), // 
    BestJSON(true), 
    BestMaven(false), // 
    BestNoSQL(true), 
    BestPHP(true), 
    BestOfCloud(true), 
    BestOfCocoa(true), 
    BestOfCss(true), 
    BestOfJava(true), 
    BestOfLinux(true), 
    BestOfRuby(false), // 
    BestOfSecurity(true), 
    BestRubyOnRails(true), 
    BestSQL(true), 
    BestScala(true), 
    BestXML(true), 
    
    DjangoDaily(false), // 
    
    FacebookDigest(true), 
    
    GoogleDigest(true), 

    HibernateDaily(false), // 
    HTMLdaily(true), 
    
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
    
    PythonDaily(true), 
    ParsingDaily(true), 
    
    RESTDaily(false), // 
    RegexDaily(true), 
    
    ServerFaultBest(false),  // SO specific
    SpringAtSO(false), // SO specific
    
    PerlDaily(true);
    
    private final boolean rt; 
    
    TwitterAccountEnum(final boolean rt) {
        this.rt = rt;
    }

    public boolean isRt() {
        return rt;
    }
}
// @formatter:ons