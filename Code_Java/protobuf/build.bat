@echo off

set COMMAND= ..\ProtoGen\protogen.exe

SetLocal EnableDelayedExpansion

cd Protos

for %%i in ("*.proto") do set COMMAND=!COMMAND! -i:%%i

cd db

for %%i in ("*.proto") do set COMMAND=!COMMAND! -i:db/%%i

cd ..

%COMMAND% -o:../Classes/protobuf-package.cs

cd ..

start /wait Framework/csc.exe /noconfig /r:System.Core.dll /r:protobuf-net.dll /r:System.dll /out:protobuf-package.dll /t:library Classes\\*.cs

start /wait Precompile\\precompile protobuf-package.dll -o:protobuf-serializer.dll -t:ProtoBufSerializer

java -jar handlerBuilder.jar

xcopy .\Handlers\NetHandler.cs ..\..\client\Assets\Script\Bleach\Net\ /S /R /Y
xcopy .\Handlers\CGMessage ..\..\client\Assets\Script\Bleach\Net\CGMessage\ /S /R /Y
xcopy .\protobuf-package.dll ..\..\client\Assets\Plugins\ /S /R /Y
xcopy .\protobuf-serializer.dll ..\..\client\Assets\Plugins\ /S /R /Y

EndLocal