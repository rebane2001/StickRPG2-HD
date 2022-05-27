@echo off
cls

set "PATCH=%CD%\patch"
start steam://launch/307640
timeout /t 3 >nul

goto start
:retry
echo Couldn't find Stick RPG 2, please launch the game exe to continue.
timeout /t 5 >nul
cls
:start
echo Looking for Stick RPG 2 instances...
wmic process where "name='Stick RPG 2 Director\'s Cut.exe'" get ExecutablePath > srpg2hd_temp_path
for /f "skip=1delims=" %%a in (
 'type srpg2hd_temp_path'
) do set "exepath=%%a"&goto next
:next
del srpg2hd_temp_path

IF ["%exepath%"] == [""] GOTO retry

echo Found Stick RPG 2 in %exepath%
for %%a in ("%exepath%") do (
    cd /d "%%~dpa"
)

taskkill /F /IM "Stick RPG 2 Director's Cut.exe"

IF NOT EXIST original.exe (
    echo Creating a backup of the original game as original.exe
    copy "Stick RPG 2 Director's Cut.exe" "original.exe"
)
rem Reset the main file and SWFs just in case we need to reinstall
del *.swf
del "Stick RPG 2 Director's Cut.exe"
copy "original.exe" "Stick RPG 2 Director's Cut.exe"

echo Downloading required files...

echo Downloading flashplayer_32_sa.exe...
IF NOT EXIST flashplayer_32_sa.exe (
    %WINDIR%\System32\curl.exe -L -o flashplayer_32_sa.exe https://archive.org/download/flashplayer_32_sa_202107/flashplayer_32_sa.exe
)
echo Download complete, verifying...
certutil -hashfile flashplayer_32_sa.exe SHA256 | %WINDIR%\System32\find.exe "a4b333ac1da12026989549015303d82231982838bccfb544ba5fd188746066f0"
if errorlevel 1 (
    echo Error verifying the hash of flashplayer_32_sa.exe, the file may be corrupted or modified.
    pause
    EXIT
)
echo Download verified successfully!

echo Downloading xdelta3-3.1.0-x86_64.exe.zip...
IF NOT EXIST xdelta3-3.1.0-x86_64.exe.zip (
    %WINDIR%\System32\curl.exe -L -o xdelta3-3.1.0-x86_64.exe.zip https://github.com/jmacd/xdelta-gpl/releases/download/v3.1.0/xdelta3-3.1.0-x86_64.exe.zip
)
echo Download complete, verifying...
certutil -hashfile xdelta3-3.1.0-x86_64.exe.zip SHA256 | %WINDIR%\System32\find.exe "b10e0d1df3e212e5a2d37d5c088d2f72ecec760bc3c26caeca51499c3118b93a"
if errorlevel 1 (
    echo Error verifying the hash of xdelta3-3.1.0-x86_64.exe.zip, the file may be corrupted or modified.
    pause
    EXIT
)
echo Download verified successfully!

echo Forcing TMP env and collecting files...
set "OLD_TMP=%TMP%"
set "TMP=%CD%"
echo Launching Stick RPG 2...
start "stick" "Stick RPG 2 Director's Cut.exe"
echo Waiting 10 seconds for the game to start...
timeout /t 10 /nobreak>nul
set "TMP=%OLD_TMP%"
echo Killing the game process...
taskkill /F /IM "Stick RPG 2 Director's Cut.exe"
echo Rearranging files...
mkdir data
mkdir data\combat
move racer-attributes.txt data\
move srpg2_*.xml data\
move *.xml data\combat\
mkdir maps
mkdir maps\559
move __*.dat maps\559\
echo Preparing to patch files...
IF NOT EXIST xdelta3-3.1.0-x86_64.exe (
    %WINDIR%\System32\tar.exe -xf xdelta3-3.1.0-x86_64.exe.zip
)
echo Patching flashplayer_32_sa...
xdelta3-3.1.0-x86_64.exe -d -s flashplayer_32_sa.exe "%PATCH%\projector.patch" flashplayer_patched.exe
echo Patching HD textures for dat.swf...
ren dat.swf dat_original.swf
xdelta3-3.1.0-x86_64.exe -d -s dat_original.swf "%PATCH%\dat.swf.texturepatch" dat.swf
echo Patching HD textures for Minigames.swf...
ren Minigames.swf Minigames_original.swf
xdelta3-3.1.0-x86_64.exe -d -s Minigames_original.swf "%PATCH%\Minigames.swf.texturepatch" Minigames.swf

echo Creating final HD exe...
del "Stick RPG 2 Director's Cut.exe"
copy /b flashplayer_patched.exe + "Zinc Shell Mac.swf" + "%PATCH%\finalize.hex" "Stick RPG 2 Director's Cut.exe"

echo Done! Press any key to exit.
pause>nul
