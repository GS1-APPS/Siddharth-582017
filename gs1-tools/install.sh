#!/usr/bin/env bash

#
# Script to automate building ALL modules, parent-pom, substrate and GL/GG maven projects
#
# example: deploy all artifacts to tomcat:
# ./install.sh  -s /Users/chrismaki/dev/rcs/gs1/Siddharth-582017 -b -v
#
# example: build all maven projects
# ./install.sh  -s /Users/chrismaki/dev/rcs/gs1/Siddharth-582017 -a -v
#

generic_copy() {
    app_name=$1
    webapp=$2
    tomcat_files="${tomcat_web}/${app_name}"*

    [[ "$verbose" -gt 0 ]] && echo "removing files $tomcat_files"
    rm -fr $tomcat_files

    [[ "$verbose" -gt 0 ]] && echo "cp -pr  $webapp $tomcat_web/${app_name}.war"
    cp -pr $webapp $tomcat_web/${app_name}.war
}

generic_liquibase() {
    path=$1

    [[ "$verbose" -gt 0 ]] && echo "cd $path"
    cd $path
    [[ "$verbose" -gt 0 ]] && echo "mvn process-resources -Pdatabase-update"
    mvn process-resources -Pdatabase-update

}

build_liquibase() {
    if [ "$liquibase" != "g" ] && [ "$liquibase" != "l" ] && [ "$liquibase" != "b" ]; then
        cat << EOF
        In order to run liquibase with this script, you need to provide one of the following values:
                     g - gs1-pds
                     l - gs1-portal
                     b - gs1-pds and then gs1-portal

        You provided the values: $liquibase
EOF
        exit 1;
    fi

    gs1_pds="${src_root}/gs1-pds/gs1-pds-webapp"
    gs1_portal="${src_root}/gs1-portal"

    [[ "$verbose" -gt 0 ]] && echo "about to build liquibase with param $liquibase"

    [[ "$liquibase" == "g" ]] && generic_liquibase $gs1_pds
    [[ "$liquibase" == "l" ]] && generic_liquibase $gs1_portal
    [[ "$liquibase" == "b" ]] && generic_liquibase  $gs1_pds && generic_liquibase $gs1_portal
}

deploy_gl() {
    generic_copy gs1-portal "${src_root}/gs1-portal/target/gs1-portal-1.0-SNAPSHOT.war"
}

deploy_gg() {
    generic_copy gs1-pds "${src_root}/gs1-pds/gs1-pds-webapp/target/gs1-pds-webapp-1.0-SNAPSHOT.war"
}

generic_build() {
    dir=$1
    [[ "$verbose" -gt 0 ]] && echo "cd to dir $dir"
    cd $dir

    [[ "$verbose" -gt 0 ]] && echo "mvn clean install"

    quiet=-q
    if test "$verbose" -gt 1
    then
        quiet=
        echo "    disabling maven quiet mode."
        echo "    mvn -B $quiet clean install"
    fi

    mvn -B $quiet clean install
}

build_all() {
    [[ "$verbose" -gt 0 ]] && echo "building all gs1  modules"
    generic_build $src_root/gs1-parent-pom
    generic_build $src_root/gs1-substrate
    generic_build $src_root/gs1-pds
    generic_build $src_root/gs1-portal
}

build_core() {
    [[ "$verbose" -gt 0 ]] && echo "building core modules only gs1-pds and gs1-portal"    
    generic_build $src_root/gs1-pds
    generic_build $src_root/gs1-portal
}

show_help() {
cat << EOF
Usage: ${0##*/} [-hdv] -s SRC_ROOT [-g] [-l] [-b] [-t TOMCAT_ROOT] [-a] [-c] [-e g|l|b]
  Tools for analyzing data created by inventory.sh
      -h   display this help and exit
      -d   debug
      -v   verbose moode. Can be used multiple times for increased verbosity

      -s SRC_ROOT    where to find your src root
      -t TOMCAT_ROOT where to find tomcat, default is $tomcat_root
      -e g|l|b       run liquibase on projects using the -P datatbase-update profile
                     g - gs1-pds
                     l - gs1-portal
                     b - gs1-pds and then gs1-portal

      -a   build ALL modules
      -c   buld CORE modules (gs1-pds and gs1-portal)
      -g   deploy gs1-pds
      -l   deploy gs1-portal
      -b   deploy both gs1-pds and gs1-portal
EOF
}

#
# Variables
#
verbose=0
debug=0
gg=0
gl=0
src_root=
tomcat_root=/usr/local/apache-tomcat-8.0.44
build=0
core=0
liquibase=
# Reset is necessary if getopts was used previously in the script.
# It is a good idea to make this local in a function.
OPTIND=1

#
# argument processing
# 
while getopts "hdvglbs:t:ace:" opt; do
  case "$opt" in
       h)  show_help; exit 0;;
       v)  verbose=$((verbose+1));;
       d)  debug=1;;
       s)  src_root=$OPTARG;;
       t)  tomcat_root=$OPTARG;;              
       e)  liquibase=$OPTARG;;
       g)  gg=1;;
       l)  gl=1;;       
       a)  build=1;;
       c)  core=1;;       
       b)
       gg=1
       gl=1
       ;;
       \?) show_help >&2; exit 1;;
   esac
done

if test "$verbose" -gt 2
then
    echo "turning on bash debugging"
    set -x
fi

if [ -z "$src_root" ]; then
    echo -e "\n-s SRC_ROOT must be specified\n"
    show_help >&2
    exit 1
fi

tomcat_web=$tomcat_root/webapps

[[ ! -z "$liquibase" ]] && build_liquibase
[[ "$build" -gt 0 ]] && build_all
[[ "$core" -gt 0 ]] && build_core
[[ "$gl" -gt 0 ]] && deploy_gl
[[ "$gg" -gt 0 ]] && deploy_gg


