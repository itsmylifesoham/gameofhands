if exist "..\src-server-website\GameOfHands.Web\bin\Release\PublishOutput\mainapp" del "..\src-server-website\GameOfHands.Web\bin\Release\PublishOutput\mainapp"
mkdir "..\src-server-website\GameOfHands.Web\bin\Release\PublishOutput\mainapp"
xcopy /s "..\src-client\platforms\browser\www" "..\src-server-website\GameOfHands.Web\bin\Release\PublishOutput\mainapp"