#!/bin/bash
WAR_PATH=~/copy_catalogue
TOMCAT_NAME=tomcat-basics
PROJECT_PATH=/usr/local/server/$TOMCAT_NAME
APP_LIST=$PROJECT_PATH/webapps
PORT=$2
DATE=$(date +%Y-%m-%d--%H-%M-%S)
usage() {
  echo "Usage: sh 执行脚本.sh [start|stop|restart|status|log|peek|add|remove]"
  exit 1
}

is_exist() {
  process_id=$(ps -ef | grep $TOMCAT_NAME | grep -v "$0" | grep -v "grep" | awk '{print $2}')
  if [ ! -z "$process_id" ]; then
    return 1
  else
    return 0
  fi
}

start() {
  is_exist
  if [ $? -eq "1" ]; then
    echo "$TOMCAT_NAME is already running. process id=$process_id ."
  else
    # echo "============[ls $APP_LIST]============="
    set -a file
    i=0
    for f in $(ls $APP_LIST); do
      if [ -f "$APP_LIST/$f" ]; then
        if [ "${f##*.}"x = "war"x ]; then
          file[$i]="$f"
          ((i++))
        fi
      fi
    done
    set -a folder
    m=0
    for f in $(ls $APP_LIST); do
      # 清除webapps下war包的目录文件
      if [ -d "$APP_LIST/$f" ]; then
        for ((k = 0; k < i; k++)); do
          fileName=${file[$k]}
          if [ "$f.war" = $fileName ]; then
            # echo "============删除[$f]文件夹============="
            rm -rf $APP_LIST/$f
          else
            folder[$m]="$f"
            ((m++))
          fi
        done
      fi
      if [ -f "$APP_LIST/$f" ]; then
        if [ "${f##*.}"x = "war"x ]; then
          if [ -f "$WAR_PATH/$f" ]; then
            echo "============删除[$f]包============="
            rm -rf $APP_LIST/$f
            echo "============转移包[$f]到tomcat============="
            mv $WAR_PATH/$f $APP_LIST
          fi
        else
          echo "============删除无效文件[$f]============="
          rm -rf $APP_LIST/$f
        fi
      fi
    done
    # 若指定端口号则先修改端口号
    set -a server
    set -a two
    z=0
    PROJECT_LIST=/usr/local/server
    for f in $(ls $PROJECT_LIST); do
      if [ ${f:0:7} = "tomcat-" ] && [ $f != "$TOMCAT_NAME" ]; then
        server[$z]="$f"
        ((z++))
      fi
    done
    if [ $z -eq 0 ]; then
      # 为空
      if [ -z "$PORT" ]; then
        # 端口二取值
        two_line=$(sed -n '/protocol="HTTP\/1.1"/=' /usr/local/server/$TOMCAT_NAME/conf/server.xml)
        for line in $two_line; do
          two_line=$line
          break
        done
        two_str=$(sed -n ''$two_line','$two_line'p' /usr/local/server/$TOMCAT_NAME/conf/server.xml)
        two_str=${two_str#*\"}
        two_str=${two_str%%\"*}
        $PROJECT_PATH/bin/startup.sh
        echo "$TOMCAT_NAME port:[$two_str] start success $DATE"
      else
        # 端口二取值
        two_line=$(sed -n '/protocol="HTTP\/1.1"/=' /usr/local/server/$TOMCAT_NAME/conf/server.xml)
        for line in $two_line; do
          two_line=$line
          break
        done
        two_str=$(sed -n ''$two_line','$two_line'p' /usr/local/server/$TOMCAT_NAME/conf/server.xml)
        two_str=${two_str#*\"}
        two_str=${two_str%%\"*}
        sudo sed -i 's/port="'$two_str'" protocol="HTTP\/1.1"/port="'$PORT'" protocol="HTTP\/1.1"/g' /usr/local/server/$TOMCAT_NAME/conf/server.xml
        if [ ! $two_str -eq $PORT ]; then
          sudo firewall-cmd --zone=public --add-port=$PORT/tcp --permanent
          sudo firewall-cmd --reload
        fi
        $PROJECT_PATH/bin/startup.sh
        echo "$TOMCAT_NAME port:[$PORT] start success $DATE"
      fi
    else
      for ((i = 0; i < $z; i++)); do
        # 端口二取值
        two_line=$(sed -n '/protocol="HTTP\/1.1"/=' /usr/local/server/${server[i]}/conf/server.xml)
        for line in $two_line; do
          two_line=$line
          break
        done
        two_str=$(sed -n ''$two_line','$two_line'p' /usr/local/server/${server[i]}/conf/server.xml)
        two_str=${two_str#*\"}
        two_str=${two_str%%\"*}
        two[$x]=$two_str
      done
      is_repeat=-1
      for I in ${!two[@]}; do
        if [[ $PORT -eq ${two[${I}]} ]]; then
          is_repeat=$I
        fi
      done
      if [ $is_repeat -lt 0 ]; then
        # 为空
        if [ -z "$PORT" ]; then
          # 端口二取值
          two_line=$(sed -n '/protocol="HTTP\/1.1"/=' /usr/local/server/$TOMCAT_NAME/conf/server.xml)
          for line in $two_line; do
            two_line=$line
            break
          done
          two_str=$(sed -n ''$two_line','$two_line'p' /usr/local/server/$TOMCAT_NAME/conf/server.xml)
          two_str=${two_str#*\"}
          two_str=${two_str%%\"*}
          $PROJECT_PATH/bin/startup.sh
          echo "$TOMCAT_NAME port:[$two_str] start success $DATE"
        else
          # 端口二取值
          two_line=$(sed -n '/protocol="HTTP\/1.1"/=' /usr/local/server/$TOMCAT_NAME/conf/server.xml)
          for line in $two_line; do
            two_line=$line
            break
          done
          two_str=$(sed -n ''$two_line','$two_line'p' /usr/local/server/$TOMCAT_NAME/conf/server.xml)
          two_str=${two_str#*\"}
          two_str=${two_str%%\"*}
          sudo sed -i 's/port="'$two_str'" protocol="HTTP\/1.1"/port="'$PORT'" protocol="HTTP\/1.1"/g' /usr/local/server/$TOMCAT_NAME/conf/server.xml
          if [ ! $two_str -eq $PORT ]; then
            sudo firewall-cmd --zone=public --add-port=$PORT/tcp --permanent
            sudo firewall-cmd --reload
          fi
          $PROJECT_PATH/bin/startup.sh
          echo "$TOMCAT_NAME port:[$PORT] start success $DATE"
        fi
      else
        echo "[${server[${is_repeat}]}]项目已使用$PORT端口，请重新指定端口！"
        exit 1
      fi
    fi
  fi
}

add() {
  for w in $(ls $WAR_PATH); do
    echo "============转移[$w]包到tomcat============="
    rm -rf $APP_LIST/$w
    mv $WAR_PATH/$w $APP_LIST
  done
}
param=$2
remove() {
  # 为空
  if [ -z "$param" ]; then
    echo "Usage: sh 执行脚本.sh [remove projectName]"
    exit 1
  fi
  # 不为空
  if [ -n "$param" ]; then
    if [ -f "$APP_LIST/$param" ]; then
      rm -rf $APP_LIST/${param%%.*}
      rm -rf $APP_LIST/$param
    else
      rm -rf $APP_LIST/$param
    fi
    ehco "remove $APP_LIST/$param success!!!"
  fi
}

stop() {
  is_exist
  if [ $? -eq "1" ]; then
    kill -9 $process_id
    echo "$TOMCAT_NAME  stop success $DATE"
  else
    echo "$TOMCAT_NAME is not running $DATE"
  fi
}

status() {
  echo "$DATE"
  is_exist
  if [ $? -eq "1" ]; then
    echo "$TOMCAT_NAME is running. Process id is $process_id"
  else
    echo "$TOMCAT_NAME is NOT running."
  fi
}

log() {
  is_exist
  if [ $? -eq "1" ]; then
    tail -f $PROJECT_PATH/logs/catalina.out
  else
    echo "$TOMCAT_NAME is NOT running."
  fi
}

peek() {
  set -a file
  i=0
  for f in $(ls $APP_LIST); do
    if [ -f "$APP_LIST/$f" ]; then
      if [ "${f##*.}"x = "war"x ]; then
        file[$i]="$f"
        ((i++))
      fi
    fi
  done
  set -a folder
  m=0
  for f in $(ls $APP_LIST); do
    if [ -d "$APP_LIST/$f" ]; then
      if [ $i -eq 0 ]; then
        if [ $f = "docs" -o $f = "examples" -o $f = "host-manager" -o $f = "manager" -o $f = "ROOT" ]; then
          #  echo "[$f]文件为tomcat默认文件，故不删除"
          cd
        else
          folder[$m]="$f"
          ((m++))
        fi
      else
        for ((k = 0; k < i; k++)); do
          fileName=${file[$k]}
          if [ "$f.war" != $fileName ]; then
            if [ $f = "docs" -o $f = "examples" -o $f = "host-manager" -o $f = "manager" -o $f = "ROOT" ]; then
              #  echo "[$f]文件为tomcat默认文件，故不删除"
              cd
            else
              folder[$m]="$f"
              ((m++))
            fi
          fi
        done
      fi
    fi
  done
  echo "$TOMCAT_NAME 下运行的项目 $(expr ${i} + ${m}) 个："
  for ((j = 0; j < i; j++)); do
    echo "--> ${file[$j]}"
  done
  for ((j = 0; j < m; j++)); do
    echo "--> ${folder[$j]}"
  done
}

restart() {
  stop
  start
}

# 不为空
if [ -n "$2" ]; then
  if [ $1 = "start" ]; then
    if ! echo "$PORT" | grep -q '^[1-9][0-9]\+$'; then
      echo "Port invalid"
      exit 1
    fi
  fi
fi

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
"add")
  add
  ;;
"remove")
  remove
  ;;
*)
  usage
  ;;
esac
