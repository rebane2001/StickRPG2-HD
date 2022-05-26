package com.rebane2001.swf_patcher;

import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.SwfOpenException;
import com.jpexs.decompiler.flash.exporters.modes.ImageExportMode;
import com.jpexs.decompiler.flash.exporters.settings.ImageExportSettings;
import com.jpexs.decompiler.flash.importers.ShapeImporter;
import com.jpexs.decompiler.flash.tags.Tag;
import com.jpexs.decompiler.flash.tags.base.CharacterIdTag;
import com.jpexs.decompiler.flash.exporters.ImageExporter;
import com.jpexs.decompiler.flash.ReadOnlyTagList;
import com.jpexs.decompiler.flash.tags.base.ShapeTag;
import com.jpexs.decompiler.flash.types.FILLSTYLE;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        String swfsInPath = "../swfs/";
        String imgOutPath = "../img/";
        String swfsOutPath = "../out/";
        String imgInPath = "../hd_assets/";
        // exportAllImages(swfsInPath, imgOutPath);
        importAllImages(swfsInPath, swfsOutPath, imgInPath);
    }

    private static void importAllImages(String swfsInPath, String swfsOutPath, String imgInPath) {
        for (File file : Objects.requireNonNull(new File(imgInPath).listFiles())) {
            System.out.println(file.getName());
            try (FileInputStream fis = new FileInputStream(swfsInPath + file.getName())) { //open up a file
                SWF swf = new SWF(fis, true);
                String out = swfsOutPath + file.getName();
                Files.createDirectories(Paths.get(swfsOutPath));
                importImages(swf, String.valueOf(file), out);
                System.out.println("OK");
            } catch (SwfOpenException ex) {
                System.out.println("ERROR: Invalid SWF file");
            } catch (IOException ex) {
                System.out.println("ERROR: Error during SWF opening");
            } catch (InterruptedException ex) {
                System.out.println("ERROR: Parsing interrupted");
            }
        }
    }

    private static void exportAllImages(String swfsPath, String imgOutPath) {
        for (File file : Objects.requireNonNull(new File(swfsPath).listFiles())) {
            System.out.println(file.getName());
            try (FileInputStream fis = new FileInputStream(file)) { //open up a file
                SWF swf = new SWF(fis, true);
                String out = imgOutPath + file.getName();
                Files.createDirectories(Paths.get(out));
                exportImages(swf, out);
                System.out.println("OK");
            } catch (SwfOpenException ex) {
                System.out.println("ERROR: Invalid SWF file");
            } catch (IOException ex) {
                System.out.println("ERROR: Error during SWF opening");
            } catch (InterruptedException ex) {
                System.out.println("ERROR: Parsing interrupted");
            }
        }
    }

    private static void importImages(SWF swf, String in, String out) throws IOException {
        for (Tag t : swf.getTags().toArrayList()) {
            if (t instanceof ShapeTag) {
                int bitmapId = getBitmapId((ShapeTag) t);
                if (bitmapId > 0) {
                    Path path = Paths.get(in + "/new/" + bitmapId + ".png");
                    if (!Files.exists(path)) {
                        path = Paths.get(in + "/upsies/" + bitmapId + ".png");
                        if (!Files.exists(path)) {
                            continue;
                        }
                    }
                    System.out.println("Importing: " + bitmapId);
                    byte[] image = Files.readAllBytes(path);
                    new ShapeImporter().importImage((ShapeTag) t, image);
                }
            }
        }

        OutputStream os = new FileOutputStream(out);
        try {
            swf.saveTo(os);
        } catch (IOException e) {
            System.out.println("ERROR: Error during SWF saving");
        }
    }

    private static void exportImages(SWF swf, String outFolder) throws IOException, InterruptedException {
        List<Integer> bitmapIds = new ArrayList<>();
        List<Tag> bitmapTags = new ArrayList<>();

        System.out.println("Filtering ShapeTags...");

        for (Tag t : swf.getTags()) {
            if (t instanceof ShapeTag) {
                int bitmapId = getBitmapId((ShapeTag) t);
                if (bitmapId > 0) {
                    bitmapIds.add(bitmapId);
                }
            }
        }

        System.out.println("Collecting bitmaps...");

        for (Tag t : swf.getTags()) {
            if (t instanceof CharacterIdTag) {
                int characterId = ((CharacterIdTag) t).getCharacterId();
                if (bitmapIds.contains(characterId)) {
                    bitmapTags.add(t);
                }
            }
        }

        System.out.println("Exporting bitmaps...");

        new ImageExporter().exportImages(
                null,
                outFolder, new ReadOnlyTagList(bitmapTags),
                new ImageExportSettings(ImageExportMode.PNG),
                null);
    }

    private static int getBitmapId(ShapeTag t) {
        int bitmapId = 0;
        for (FILLSTYLE fillstyle : t.getShapes().fillStyles.fillStyles) {
            if (fillstyle.bitmapId > 0) {
                bitmapId = fillstyle.bitmapId;
            } else {
                return 0;
            }
        }
        return bitmapId;
    }
}