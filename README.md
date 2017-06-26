# Developer Guide

## Table of Contents

1. [tl;dr](#tldr)
2. [Helpful commands](#Helpful-commands)
3. [Build Properties](#Build-Properties)
4. [Database Migration](#Database-Migration)
 
## tl;dr

Install the following
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Postgres](https://www.postgresql.org/download/)
    * Create two databases in your database
        * gs1ussgl - DB for the UI (gs1-portal)
        * sgg      - DB for the backend (gs1-pds)
    * Create two files in your home directory
        * gs1-portal.properties
            * `jdbc.url=jdbc:postgres://localhost/gs1ussgl`
            * `jdbc.username=yyy`
            * `jdbc.password=zzz`
            * `liquibase.contexts=dev`
        * gs1-pds.properties
            * `jdbc.url=jdbc:postgres://localhost/sgg`
            * `jdbc.username=yyy`
            * `jdbc.password=zzz`
            * `liquibase.contexts=dev`
* [Maven](https://maven.apache.org/download.cgi)
* [Tomcat 8](https://tomcat.apache.org/download-80.cgi)

Clone the github repo

    $ cd $root
    $ clone git@github.com:GS1-APPS/Siddharth-582017.git
    $ cd Siddharth-582017
    
update databases and deploy everything

    $ cd $root
    $ ./install.sh  -s $root -t $tomcat_path -e b -a -v

## Helpful commands

create DB from scratch, will drop existing DB and recreate all tables and populate data

    $ cd $root/gs1-portal
    $ mvn process-resources -Pdatabase-create

update you database    

    $ cd $root/gs1-portal
    $ mvn process-resources -Pdatabase-update

biuld and redeploy both apps to the same tomcat instance

    $ cd $root
    $ ./install.sh  -s $root -t $tomcat_path -b -v

## Build Properties

To assist in making your Maven `pom.xml` a little more flexible, we've added the 
`properties-maven-plugin` to enable Maven to read a properties file and inject those properties 
into the `pom.xml` file. This plugin is used by the `liquibase` plugin to allow dynamic database 
connection information. Here is the plugin home page: 
http://www.mojohaus.org/properties-maven-plugin/  

### Items to add to your `pom.xml`'s properties
There are two properties defined in `gs1-parent-pom` that  you need to override in your projects:

    1. build.properties.file
    2. liquibase.changelog
    
Here is how you'd define the properties above in your `pom.xml` file:

     <properties>
        <build.properties.file>${user.home}/gs1-pds.properties</build.properties.file>
        <liquibase.changelog>src/main/liquibase/dbchangelog.xml</liquibase.changelog>
    </properties>

The settings above allow you to set the URL, username and password to a database you will create,
 update or rebuild.  

If you want to use the `properties-maven-plugin` you need to define a path to your local jdbc 
settings, the properties you need to define are: 

    jdbc.username
    jdbc.password
    jdbc.url
    liquibase.contexts
    jdbc.driver

The `liquibase.changelog` is the path to your projects `dbchangelog.xml` file. `jdbc.driver` is 
optional, the default value is `org.postgresql.Driver`.


## Database Migration

All projects inherit from the `gs1-parent-pom` file which defines liquibase:

    <plugin>
        <groupId>org.liquibase</groupId>
        <artifactId>liquibase-maven-plugin</artifactId>
        <version>3.5.3</version>
        <configuration>
            <changeLogFile>${liquibase.changelog}</changeLogFile>
            <promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
            <username>${jdbc.username}</username>
            <password>${jdbc.password}</password>
            <url>${jdbc.url}</url>
            <contexts>${liquibase.contexts}</contexts>
            <driver>${jdbc.driver}</driver>
        </configuration>
        <dependencies>
            <dependency>
                <groupId>org.postgresql</groupId>
                <artifactId>postgresql</artifactId>
                <version>9.3-1102-jdbc4</version>
            </dependency>
        </dependencies>
    </plugin>

There are a few properties that you can override for your local environment:
    
    jdbc.username
    jdbc.password
    jdbc.url
    jdbc.driver
    
You can define these properties at the command line, like this:

    mvn process-resoruces -Djdbc.username=foo -Djdbc.password=pass -Pdatabase-create
    
The above would recreate your database, using the `username/password` specified. While this makes
 it possible to dynamically change your credentials, it's easier to store you property values in 
 a file, say `~/gs1-pds-database.properties`, then you could run the above like this:

    mvn process-resources -Dbuild.properties.file=~/gs1-pds-database.properties -Pdatabase-create
    
This will read all your properties from the `.properties` file and re-create your DB for you.

### DB Profiles

There are a couple of pre-defined profiles in the `gs1-parent-pom` to make creating and 
destroying your local database easier. The profiles are:

    1. database-update
    2. database-create
    
#### database-update

If you have new DDL or DML defined for your project, Liquibase will update your database. If 
there are no changes, nothing will hapen.

#### database-create

Use this if you want to destroy your database and start fresh.