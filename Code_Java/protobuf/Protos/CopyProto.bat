@echo off

xcopy .\Login.proto ..\..\..\..\Msg\ /S /R /Y
xcopy .\MessageType.proto ..\..\..\..\Msg\ /S /R /Y

EndLocal