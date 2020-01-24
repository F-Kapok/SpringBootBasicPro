#!/bin/bash
WAR_PATH=~/copy
TOMCAT_NAME=tomcat-crm
PROJECT_PATH=/pm/server/$TOMCAT_NAME
APP_LIST=$PROJECT_PATH/webapps
DATE=`date +%Y-%m-%d--%H-%M-%S`
usage() {
    echo "Usage: sh 执行脚本.sh [start|stop|restart|status|log|peek]"
    exit 1
}

is_exist(){
process_id=`ps -ef | grep $TOMCAT_NAME| grep -v "$0" | grep -v "grep" | awk '{print $2}'`
if [ ! -z "$process_id" ]; then
 return 1
else
 return 0
fi
}

start(){
  is_exist
  if [ $? -eq "1" ]; then
    echo "$TOMCAT_NAME is already running. process id=$process_id ."
  else
    echo "============[ls $APP_LIST]============="
    for f in `ls $APP_LIST`
    do
        if [ -d "$APP_LIST/$f" ]; then
            echo "============删除[$f]文件============="
            rm -rf $APP_LIST/$f
        else
            if [ -f "$WAR_PATH/$f" ]; then
                echo "============删除[$f] war包============="
                rm -rf $APP_LIST/$f
                echo "============转移war包到tomcat============="
                mv $WAR_PATH/$f $APP_LIST
            else
               echo "$WAR_PATH 中没有该 $f 包，需要更新请手动添加，重启服务则忽略"
            fi
        fi
    done
    $PROJECT_PATH/bin/startup.sh
    echo "$TOMCAT_NAME start success $DATE"
  fi
}

stop(){
  is_exist
  if [ $? -eq "1" ]; then
    kill -9 $process_id
    echo "$TOMCAT_NAME  stop success $DATE"	
  else
    echo "$TOMCAT_NAME is not running $DATE"
  fi  
}


status(){
  echo "$DATE"
  is_exist
  if [ $? -eq "1" ]; then
    echo "$TOMCAT_NAME is running. Process id is $process_id"
  else
    echo "$TOMCAT_NAME is NOT running."
  fi
}


log(){
  is_exist
  if [ $? -eq "1" ]; then
    tail -f $PROJECT_PATH/logs/catalina.out
  else
    echo "$TOMCAT_NAME is NOT running."
  fi
}

peek(){
  set -a file
  i=0
  for f in `ls $APP_LIST`
  do
    if [ -f "$APP_LIST/$f" ]; then
      file[$i]="$f"
      ((i++))    	
    fi
  done
  echo "$TOMCAT_NAME 下运行的war包 $i 个："
  for (( j=0;j<i;j++ )) 
  do
    fileName=${file[$j]}
    echo "--> $fileName"
  done
}

restart(){
  stop
  start
}

case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  "log")
    log
    ;;
  "peek")
    peek
    ;;
  *)
    usage
    ;;
esac


