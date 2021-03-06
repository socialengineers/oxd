### BEGIN INIT INFO
# Provides:          oxd-server
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start daemon at boot time
# Description:       Enable service provided by daemon.
### END INIT INFO

SERVICE_NAME=oxd-server
PID_PATH_NAME=/var/run/oxd-server.pid
TMP_PID_PATH_NAME=/tmp/oxd-server.pid
BASEDIR=/opt/oxd-server/bin/
CONF=/opt/oxd-server/conf
LIB=/opt/oxd-server/lib
OXD_INIT_LOG=/var/log/oxd-server.log

get_pid() {
	if [ -f $PID_PATH_NAME ]; then                
		PID_NUM=$(cat $PID_PATH_NAME)
		echo "$PID_NUM"
	else
		REMAINING_PID="`ps -eaf|grep -i java|grep -v grep|grep -i 'oxd-conf.json'|awk '{print $2}'`"                
		###For one more possible bug, find and kill oxd                
		if [ "x$REMAINING_PID" != "x" ]; then
			echo "$REMAINING_PID"
		fi
	fi
}

do_start () {        
	PID_NUM=`get_pid`
        if [ "x$PID_NUM" = "x" ]; then                
                echo "Starting $SERVICE_NAME ..."                

                nohup java -Doxd.server.config=$CONF/oxd-conf.json -Dlog4j.configuration=$CONF/log4j.xml -cp $LIB/bcprov-jdk15on-1.54.jar:$LIB/resteasy-jaxrs-2.3.7.Final.jar:$LIB/oxd-server-jar-with-dependencies.jar org.xdi.oxd.server.ServerLauncher 2>>/dev/null>>/dev/null&                

                sleep 10
                echo $! > $TMP_PID_PATH_NAME        
                START_STATUS=`tail -n 10 $OXD_INIT_LOG|grep -i 'Start listening for notifications'`
                ERROR_STATUS=`tail -n 10 $OXD_INIT_LOG|grep -i 'Failed to start oxd server'`
                if [ "x$START_STATUS" = "x" ]; then
			###If by chance log file doesn't provide necessary string, sleep another 10 seconds and check again PID of process
			sleep 10
			PID_NUM=`get_pid`
        		if [ "x$PID_NUM" = "x" ]; then
                        	### Since error occurred, we should remove the PID file at this point itself.                        
                        	echo "Some error encountered..."                        
                        	echo "See log below: "                        
                        	echo ""                        
                        	echo "$ERROR_STATUS"                        
                        	echo ""                        
                        	echo "For details please check $OXD_INIT_LOG ."                        
                        	echo "Exiting..."                        
                        	exit 255
                	fi

		fi
                mv $TMP_PID_PATH_NAME $PID_PATH_NAME                        
        	PID_NUM=$(cat $PID_PATH_NAME)
        else                
                echo "$SERVICE_NAME is already running ..."        
        fi        
        echo "PID: [$PID_NUM]"
}

do_stop () {        
	PID_NUM=`get_pid`
        if [ "x$PID_NUM" != "x" ]; then                
                echo "$SERVICE_NAME stoping ..."            
                kill -s 9 $PID_NUM;            
                rm -f $PID_PATH_NAME        
        else   
                echo "$SERVICE_NAME is not running ..."        
        fi
}

case $1 in
    start)
            do_start
    ;;
    stop)
            do_stop
    ;;
    restart)
            do_stop
            do_start
    ;;
    status)
        if [ -f $PID_PATH_NAME ]; then
            echo "$SERVICE_NAME is running ...";
            PID_NUM=$(cat $PID_PATH_NAME)                
            echo "PID: [$PID_NUM]"
        else
           echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    *)
        echo "Usage: $0 {start|stop|status|restart}"
        RETVAL=2
    ;;
esac 
