@echo off

set jar_lib=.\config
SetLocal EnableDelayedExpansion

for %%i in (".\bin\*.jar") do set jar_lib=!jar_lib!;%%i

java -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=n -Dserver.name=game_server_1 -Xmx1024M -Xms512M -Xss128K -XX:NewRatio=2 -XX:PermSize=64m -XX:MaxPermSize=64m -XX:+UseConcMarkSweepGC -cp %jar_lib% com.mokylin.bleach.launcher.Launcher 

EndLocal