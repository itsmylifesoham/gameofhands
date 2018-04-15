if exist "..\src-server-website\GameOfHands.Web\bin\Debug\PublishOutput\mainapp" del "..\src-server-website\GameOfHands.Web\bin\Debug\PublishOutput\mainapp"
mkdir "..\src-server-website\GameOfHands.Web\bin\Debug\PublishOutput\mainapp"
xcopy /s "..\src-client\platforms\browser\www" "..\src-server-website\GameOfHands.Web\bin\Debug\PublishOutput\mainapp"