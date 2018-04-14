cd ../src-client
START /WAIT phonegap run browser --verbose
cd ../scripts
if exist "..\src-server-website\GameOfHands.Web\mainapp" del "..\src-server-website\GameOfHands.Web\mainapp"
mkdir "..\src-server-website\GameOfHands.Web\mainapp"
xcopy /s "..\src-client\platforms\browser\www" "..\src-server-website\GameOfHands.Web\mainapp"
pause