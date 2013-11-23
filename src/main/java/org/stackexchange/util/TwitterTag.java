package org.stackexchange.util;

public enum TwitterTag {// @formatter:off

    algorithm, 
    algorithms, 
    android, 
    apple, 
    asp, 
    aspnet, 
    authentication, 
    azure, 
    akka, 
    aws,
    
    bash,
    
    cassandra,
    captcha,
    clojure, 
    cloud, 
    cocoa,
    couchdb,
    couchbase,
    cryptography,
    crypto,
    css, 
    css3, 
    
    database,
    datomic,
    ddos,
    dynamobd,
    django, 
    dotnet,
    dynatrace,
    dtrace,
    
    eclipse, 
    eclipselink, 
    ec2,
    encryption,
    
    facebook,
    
    git, 
    google,
    gae,
    googleappengine,
    gmail,
    gdrive,
    
    hadoop,
    hbase,
    html,
    html5,
    // hibernate,
    httpclient,
    httpclient4,

    ibatis,
    ios,
    iphone,
    ipad, 
    
    java, 
    jquery(false), // maybe temporarily - come back to it when the other stuff generates far less logging output
    jpa, 
    jpa2, 
    json, 
    javascript, 
    jvm,
    jsp,
    java8,
    java9, 
    
    lisp, 
    linux, 
    
    multithreading,
    multithread,
    multithreaded,
    mysql, 
    maven, 
    math, 
    macbook,
    mongodb, 
    msbuild,
    
    nosql, 
    neo4j, 
    
    objectivec, 
    opengraph, 
    openjpa, 
    
    passwords, 
    parsing, 
    php, 
    php5, 
    python(false), // moving this to 1 is risky before more classifiers are on 
    perl(false), // again - non-programming classifier
    perl5, 
    perl6, 
    postgresql,
    performance_java,
    performance_http,
    performance_sql,
    performance_clojure,
    performance_programming,
    
    rest, 
    regex, 
    ruby, 
    rails, 
    ruby_rails, 
    regularexpressions, 
    riak, 
    rubyonrails, 
    redis, 
    
    s3, 
    scala, 
    security, 
    seo, 
    sql, 
    sqlinjection, 
    spring, 
    spring4, 
    springsecurity,
    springdata,
    springsocial,
    springframework,
    
    ubuntu,

    wordpress, 
    
    xml,
    xcode,
    
    visualstudio,
    
    wpf;

    private final boolean generateLogs; 

    TwitterTag() {
        this.generateLogs = true;
    }

    TwitterTag(final boolean generateLogs) {
        this.generateLogs = generateLogs;
    }

    public boolean isGenerateLogs() {
        return generateLogs;
    }
    
}// @formatter:on
