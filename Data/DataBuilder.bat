@echo off

cd Csvs
del /Q *.csv
cd ..
start /wait excel2cvs.exe
xcopy .\Csvs ..\client\Assets\Resources\Tables /Y
xcopy .\Configs ..\client\Assets\Resources\Configs /s /Y