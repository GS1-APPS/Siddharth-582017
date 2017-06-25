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
    mvn -B -q clean install
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
Usage: ${0##*/} [-hdv] -s SRC_ROOT [-g] [-l] [-b] [-t TOMCAT_ROOT] [-a] [-c]
  Tools for analyzing data created by inventory.sh
      -h   display this help and exit
      -d   debug
      -v   verbose moode. Can be used multiple times for increased verbosity

      -s SRC_ROOT    where to find your src root
      -t TOMCAT_ROOT where to find tomcat, default is $tomcat_root

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
# Reset is necessary if getopts was used previously in the script.
# It is a good idea to make this local in a function.
OPTIND=1

#
# argument processing
# 
while getopts "hdvglbs:t:ac" opt; do
  case "$opt" in
       h)  show_help; exit 0;;
       v)  verbose=$((verbose+1));;
       d)  debug=1;;
       s)  src_root=$OPTARG;;
       t)  tomcat_root=$OPTARG;;              
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

if test "$verbose" -gt 1
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

[[ "$build" -gt 0 ]] && build_all
[[ "$core" -gt 0 ]] && build_core
[[ "$gl" -gt 0 ]] && deploy_gl
[[ "$gg" -gt 0 ]] && deploy_gg


