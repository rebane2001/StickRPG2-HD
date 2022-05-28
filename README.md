# StickRPG2-HD
StickRPG2-HD is an unofficial HD mod for Stick RPG 2 that unlocks window resizing, allows native resolution fullscreen, enables the Steam overlay, and replaces some textures with HD versions.

## Installation
1. Download the `StickRPG2-HD-vX.X.X.zip` file from the [latest release](https://github.com/rebane2001/StickRPG2-HD/releases).
2. Extract the zip file somewhere and run the `install_stickrpg2_hd.bat` file by double-clicking it.
3. Stick RPG 2 should launch automatically through Steam, if it doesn't you'll need to launch it yourself.
4. Wait for the mod to install. You can close the installer window once you see the "Done! Press any key to exit." message in it.
5. Launch Stick RPG 2 as you usually would and enjoy!

## Compatibility
StickRPG2-HD requires **Windows 10 or higher** from 2019 or later. HOWEVER, if you don't have that, there are still two ways to proceed:

1. If you're running an older Windows, you can manually download the [flashplayer_32_sa.exe](https://archive.org/download/flashplayer_32_sa_202107/flashplayer_32_sa.exe) and [xdelta3-3.1.0-x86_64.exe.zip](https://github.com/jmacd/xdelta-gpl/releases/download/v3.1.0/xdelta3-3.1.0-x86_64.exe.zip) files into the same folder the game is installed in, and also unzip the latter. The installer should now work fine.
2. If you're running an OS that's unsupported altogether, you can install the mod on a Windows 10 computer and then copy the game installation folder over from that computer to the other one. This method also lets you run the HD mod on devices such as the Steam Deck or a Mac.

I have tested this mod both on the **Steam Deck (Linux)** and the **M1 MacBook Air running macOS 12** and Wine, it works on both.

## Known Issues

- The fullscreen button in the settings no longer works. Instead you can just press ctrl+f to go fullscreen and esc to go back.
- The quit game button doesn't work, but you can just close the game from the `x` or press alt+f4.
- Since rendering the game in a higher resolution is more demanding (especially so with Flash), the game may lag on weaker hardware.
- On macOS, entering and leaving buildings can cause the game to freeze for a couple seconds sometimes.

## Saves

The installer attempts to automatically link your old saves with the HD version saves so your saves should be the same no matter which version of the game you play. However, if for example copying this game to a different device without using the installer, you'll need to manage your saves manually, so here are the paths:

- The default (unmodded) save location: `C:\Users\USERNAME\AppData\Roaming\Macromedia\Flash Player\#SharedObjects\RANDOM\localhost\Program Files (x86)\Steam\steamapps\common\Minigames.swf\srpg2Save.sol`
- The HD mod save location: `C:\Users\USERNAME\AppData\Roaming\Macromedia\Flash Player\#SharedObjects\RANDOM\localhost\Program Files (x86)\Steam\steamapps\common\Stick RPG 2\Minigames.swf\srpg2Save.sol`

The `USERNAME` in the path is your Windows username, and the `RANDOM` is just some random characters. The path will differ if you've installed the game in a different location, so you'll need to change the paths accordingly. The installer should automatically find the right path though.

The online save functionality works in the HD mod without any changes.

## HD Textures
While most of the Stick RPG 2 assets have been kept original for authenticity, some have been remade or upscaled to a higher resolution.

You can find the source files for the custom textures in the hd_assets folder of this repo, where the `new/` and `proj/` subfolders contain manually remade art and `upsies/` contains textures upscaled with AI.

These textures are automatically installed with the mod installer, but if you'd like to disable all custom textures you can replace find the `dat_original.swf` and `Minigames_original.swf` files in the game install directory, which can be used to replace the modified swf files to restore the original textures.
