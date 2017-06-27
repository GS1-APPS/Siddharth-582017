# GS1-PDS (Portable Data Store)

## Build Database

This project is using [Liquibase](http://www.liquibase.org/) to manage the database. [Here is a 
quick description](https://stackoverflow.com/questions/29760629/why-and-when-liquibase) of why 
you might want to use a schema versioning tool like Liquibase. 
 
### How to build your schema

To build a local version of the database, you'll need to do the follow:

1. If you have not started the local Docker Compose Postgres instance:
```bash
    cd $project_root
    docker-compose up -d
```
    
 2. Run Liquibase to create your database (see [multi-module projects](#multi_module)below for why 
 `-pl`):
```bash
   cd $project_root/gs1-pds
   mvn -pl gs1-pds-webapp process-resources -Pdatabase-update
```


### <a name="multi_module"></a>multi-module projects

When you have a multi-module Maven project, that is a project with more than one `pom.xml` file 
and multiple subdirectories, any maven command you execute will be executed across all modules. 
When you are in the parent directory and run `mvn clean install` all modules are cleaned and 
installed. 
 
To build your local schema, there is only one project that has the Liquibase commands 
`gs1-pds-webapp`. When you use the `-pl` command with Maven, you are telling Maven, only run 
against this module. Since module names and directory names are the same for this project, `-pl` 
is telling Maven which directory to run the commands in. 

The other GS1 projects are NOT multi-module projects so you will not need the `-pl` option with 
them.

Here is the Maven [Guide to Working with Multiple Modules](https://maven.apache.org/guides/mini/guide-multiple-modules.html) for more.

