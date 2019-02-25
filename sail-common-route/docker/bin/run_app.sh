#!/bin/bash

#取当前目录
BASE_PATH=`cd "$(dirname "$0")"; pwd`

#设置java运行参数
DEFAULT_JAVA_OPTS=" -server -Xmx3g -Xms3g -Xmn512m -XX:PermSize=1g -Xss512k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Dfile.encoding=UTF-8"

#引入外部参数配置文件:
SHELL_PARAMS="$BASE_PATH/params.conf"
if [ -f "$SHELL_PARAMS" ]; then 
	. $SHELL_PARAMS
fi

#定义变量:
APP_PATH=${APP_PATH:-`dirname "$BASE_PATH"`}
CLASS_PATH=${CLASS_PATH:-$APP_PATH/config:$APP_PATH/lib/*}
JAVA_OPTS=${JAVA_OPTS:-$DEFAULT_JAVA_OPTS}
DEFAULT_JAR=$(find $APP_PATH/lib/ -name *.jar)
SERVER_ACCESSABLE_IP=$(ifconfig eth0 |grep 'inet add'|awk -F ":" '{print $2}'|awk '{print $1}')
SERVER_ACCESSABLE_PORT=10000
export SERVER_ACCESSABLE_IP=${SERVER_ACCESSABLE_IP}
export SERVER_ACCESSABLE_PORT=${SERVER_ACCESSABLE_PORT}
export SERVER_ENVIROMENT=${PROJECTENV}
echo "APP_PATH=$APP_PATH"
echo "BASE_PATH=$BASE_PATH"
echo "DEFAULT_JAR=$DEFAULT_JAR"
echo "CLASS_PATH=$CLASS_PATH"
echo "traceXApi is started."
cd $APP_PATH
ulimit -HSn 20480
java $JAVA_OPTS -cp $CLASS_PATH -jar $DEFAULT_JAR $APP_PATH








