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

## How does it work?

The script roughly follows this series of steps:
1. Launch Stick RPG 2 through Steam.
2. Find the running process to find the path of the game.
3. Backup the game.
4. Download and verify flash player and xdelta.
5. Set the TMP env variable (local scope) to a custom path and launch the game with it. This drops us some useful game files.
6. Close the game and reset the TMP env variable.
7. Rearrange the dropped files to a structure that Stick RPG 2 requires:
   - Combat XMLs go in `data/combat/`.
   - racer-attributes.txt and srpg2 XMLs go in `data/`.
   - maps (`.dat` files) go in `maps/559/` (559 is the map version number).
   - Other files are left as-is in the root directory.
8. Patch the flash player using the included xdelta patch file:
   - The patch file was generated using the tools in `tools/projector_patcher/`, more info is available in `info.txt` but essentially:
     - Use Resource Hacker to change the icon from the Flash logo to the Stick RPG 2 logo.
     - Change the window title with a simple `sed`.
     - Use xdelta to save the patch file so the installer does not need sed nor Resource Hacker.
9. Create backup copies of `dat.swf` and `Minigames.swf`, then patch them with xdelta:
   - The patched SWFs were created with the [JPEXS library](https://github.com/jindrapetrik/jpexs-decompiler) and the Java code in `tools/swf_patcher/`, to create your own patch:
     - Put your SWFs in `swfs/`, also create folders called `img/`, `out/`, and `hd_assets/`.
     - Run the exportAllImages function from the Java code (uncomment it).
     - Get the original images from the `img/` directory, and put your modified images in `hd_assets/[SWF_NAME]/new/`.
     - Run the importAllImages function from the Java code.
     - You'll find the modified SWFs in the `out/` folder, use xdelta to create patches for them.
10. Create the final `Stick RPG 2 Director's Cut.exe` file by appending `Zinc Shell Mac.swf` and `patches/finalize.hex` to the patched flash player.
    - The `finalize.hex` file starts with the magic hex `56 34 12 FA`. Then the size of the swf is written as a 4-byte little-endian value, eg `81342` becomes `BE 3D 01 00`.
    - Adding these two things to the exe magically makes it auto-load the swf on startup.
11. Try to link the save data so that the HD mod and default game version share the same saves.
