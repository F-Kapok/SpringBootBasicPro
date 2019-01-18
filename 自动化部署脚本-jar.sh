#! /bin/bash
echo "===========进入git项目WeChatShop目录=============" 
cd /usr/local/developer/WeChatShop/buyer 
echo "==========git切换分之到WeChatShop-master===============" 
git checkout master 
echo "==================git fetch======================" 
git fetch 
echo "==================git pull======================" 
git pull 
echo "===========编译并跳过单元测试====================" 
mvn clean package -Dmaven.test.skip=true 
echo "============关闭旧的进程============="
process_id=$(ps -ef | grep java | grep -v grep | awk '{print $2}')
if [[ ! -z "$process_id" ]]
then
echo not empty
kill -9 $process_id
else
echo empty
fi
echo "============重启nginx============="
service nginx restart
echo "============重启mysql============="
service mysqld restart
echo "============关闭redis============="
process_id=$(ps -ef | grep redis | grep -v grep | awk '{print $2}')
if [[ ! -z "$process_id" ]]
then
echo not empty
kill -9 $process_id
service redis stop
rm -rf /var/run/redis_6379.pid
else
echo empty
fi
service redis start
echo "================删除旧的log日志=========================" 
rm -rf /usr/local/developer/sellLog.log
echo "================sleep 10s=========================" 
for i in {1..10} 
do 
   	echo $i"s" 
   	sleep 1s 
done 
echo "====================启动新的jar=====================" 
nohup java -jar -Dserver.port=8080 -Dspring.profiles.active=prod /usr/local/developer/WeChatShop/buyer/target/sell.jar > /usr/local/developer/sellLog.log 2>&1 &

