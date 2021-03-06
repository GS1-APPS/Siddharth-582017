# Developer Guide

## Table of Contents

1. [tl;dr](#tldr)
1. [Helpful commands](#helpful-commands)
1. [Too Many Properties Files](#too-many) 
1. [Build Properties](#build-properties)
1. [Database Migration](#database-migration)
1. [Helpful Tomcat Commands](#tomcat)
1. [Eclipse Setup](#eclipse)
 
## tl;dr

Install the following
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/download.cgi)
* [Docker CE](https://docs.docker.com/engine/installation/)
* [Tomcat 8](https://tomcat.apache.org/download-80.cgi)
* [Cygwin](https://www.cygwin.com/) - Windows only


Clone the github repo
```bash
    $ cd $root
    $ clone git@github.com:GS1-APPS/Siddharth-582017.git
    $ cd Siddharth-582017
```
    
<a name="one_command"></a>update databases and deploy everything with one command
```bash
    $ cd $root/Siddharth-582017
    $ docker-compose up -d
    $ ./install.sh  -s $(pwd) -t $tomcat_path -e b -a -vv
```

Once your application is running, you can rebuild all modules and re-deploy them to Tomcat like 
this:
```bash
    $ cd $root/Siddharth-582017
    $ ./install.sh  -s $(pwd) -t $tomcat_path -e b -a -vv
```

For more on the `install.sh` command use
```bash
    $./install.sh -h
```

## <a name="helpful-commands"></a>Helpful commands

create DB from scratch, will drop existing DB and recreate all tables and populate data

    $ cd $root/gs1-portal
    $ mvn process-resources -Pdatabase-create
    $ cd $root/gs1-pds
    $ mvn process-resources -Pdatabase-create
    
update you database    

    $ cd $root/gs1-portal
    $ mvn process-resources -Pdatabase-update
    $ cd $root/gs1-pds
    $ mvn process-resources -Pdatabase-update


build and redeploy both apps to the same tomcat instance

    $ cd $root
    $ ./install.sh  -s $root -t $tomcat_path -b -v

or just [use one command](#one_command) to build and deploy everything 

## <a name="too-many"></a>Too Many Properties Files
There are two sets of properties files used in the GS1 projects. One set of files is used for 
`Liquibase` and lives outside of this project (you specify the file used by `Liquibase` as a 
command line argument).

The other set of properties files are used by Spring. These properties files are contained in the
 source code and can be overridden on the server the WAR is deployed by placing a `.properties` 
 file in the appropriate directory:
   ```bash
   /opt/gs1/gs1-pds/conf/application.properties
   /opt/gs1/gs1-portal/conf/application.properties
   ```
As you can see, each application has it's own path to override it's own Spring configuration 
values. See each projects `applicationContext.xml` file and look for `${xxxx}` where `xxx` is the
 property being defined in one of the above files.

## <a name="build-properties"></a>Build Properties

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


## <a name="database-migration"></a>Database Migration

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

## <a name="tomcat"></a>Helpful Tomcat Commands
To better manage tomcat and to make sure nothing is cached as you deploy and test your 
application, I use the following sequence of steps to test my application in Tomcat:
```bash
    
    # tomcat-root => /usr/local/apache-tomcat-8.0.44
    
    $ cd $project-root 

    # stop tomcat and clear all logs 
    $ $tomcat-root/bin/catalina.sh stop;rm -fr $tomcat-root/logs/*
    
    # install.sh will not only build your war files, it will also remove all appropriate war
    # files from $tomcat-root/webapps (we use option -c below to only build the war files, this 
    # assumes you've built everything at least once using the -a option)
        $ ./install.sh -s $(pwd) -t $tomcat-root -c -b -vv
    
    # start tomcat in jpda mode, that's remote debugging mode, by default tomcat listens on port 
    # 8000 for jpda debugging.
    $ $tomcat-root/bin/catalina.sh jpda start;tail -f $tomcat-root/logs/catalina.out
```

If you followed the steps above, you should see all tomcat logs being displayed in your terminal.
 If you want to see Postgres logs, do the following:
```bash
    $ cd $project-root/Docker
    $ docker-compose logs -f
```

Now you should be able to see exactly what's happening with your applications. To tweak the 
logging level, update the `logging.properties` file in either the `gs1-pds` or `gs1-portal` 
projects. If you change the file, repeat the steps in this section:

1. stop tomcat
1. rebuild sources
1. start tomcat 

## <a name="eclipse"></a>Eclipse Setup
Make sure you follow the [tl;dr](#tldr) section above to install all the tools you'll need. While
 you can check out all your code via Eclipse, we recommend cloning the repo and then importing 
 each project as you need it into Eclipse. If you are running on Windows, we highly recommend 
 [Cygwin](https://www.cygwin.com/) so you can use the one `install.sh` script included wiht this 
 project.
 
 1. Start Eclipse
 1. Choose File -> Import...
 1. Select the `Maven` import wizard and choose "Existing Maven Projects"
 ![Eclipse Import Wizard Screen Shot](/images/maven-wizard.png?raw=true "Eclipse Import Wizard")
 1. Click `Next >`
 1. Click `Browse...` and navigate to either `gs1-portal` or `gs1-pds`
 ![Eclipse Select Maven projects Screen Shot](/images/import-project.png?raw=true "Eclipse Select 
 Maven projects")
 1. To edit classes or resources use `Open Type...` or `Open Resource...`. You want to navigate 
 to classes using the `Java Resources` section in the `Project Explorer`
 ![Eclipse Project Explorer Screen Shot](/images/project-explorer.png?raw=true "Eclipse Project 
 Explorer")
 
 Note: you can ignore the `build`, `target` and `src` directories. While this is where the code 
 and `.class` files live, you can access all source code you need through the `Java Resources` 
 section. 