#!/usr/bin/env bash

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
    generic_copy GS1USSGL "${src_root}/GS1USSGL/target/GS1USSGL-1.0-SNAPSHOT.war"
}

deploy_gg() {
    generic_copy GS1USSGG "${src_root}/GS1USSGG/GS1USSGG-webapp/target/GS1USSGG-webapp-1.0-SNAPSHOT.war"
    tomcat_files="${tomcat_web}/${app_name}*"
}


show_help() {
cat << EOF
Usage: ${0##*/} [-hdv] -s SRC_ROOT [-g] [-l] [-b] [-t TOMCAT_ROOT] 
  Tools for analyzing data created by inventory.sh
      -h   display this help and exit
      -d   debug
      -v   verbose moode. Can be used multiple times for increased verbosity

      -s SRC_ROOT    where to find your src root
      -t TOMCAT_ROOT where to find tomcat, default is $tomcat_root

      -g   	     deploy GS1USSGG 
      -l   	     deploy GS1USSGL
      -b	     deploy both GS1USSGG and GS1USSGL
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
# Reset is necessary if getopts was used previously in the script.
# It is a good idea to make this local in a function.
OPTIND=1

#
# argument processing
# 
while getopts "hdvglbs:t:" opt; do
  case "$opt" in
       h)  show_help; exit 0;;
       v)  verbose=$((verbose+1));;
       d)  debug=1;;
       s)  src_root=$OPTARG;;
       t)  tomcat_root=$OPTARG;;              
       g)  gg=1;;
       l)  gl=1;;       
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

[[ "$gl" -gt 0 ]] && deploy_gl
[[ "$gg" -gt 0 ]] && deploy_gg


