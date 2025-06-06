package com.uet.oop.rendering;

import com.uet.oop.core.ResourceFileReader;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

public class TextureManager {
    private Map<String, BufferedImage> textureMap;

    public TextureManager() {
        this.textureMap = new HashMap<>();
    }

    // load texture from files, save & associate it with a name.
    public BufferedImage loadTexture(String name, String texturePath) {
        if (textureMap.containsKey(name)) {
            return textureMap.get(name);
        }
        try {
            InputStream is = getClass().getResourceAsStream(texturePath);
            if (is == null) {
                throw new IOException("Cannot fetch from " + "\u001B[32m" + texturePath + "\u001B[0m" + " (resource not found).");
            }
            BufferedImage texture = ImageIO.read(is);

            if (texture != null && !name.equals("null!")) {
                textureMap.put(name, texture);
                System.out.printf("Texture: %s loaded %s from %s%n","\u001B[40m" + name + "\u001B[0m", "\u001B[32m" + "successfully" + "\u001B[0m" , texturePath);
                return texture;
            } else {
                System.out.println("Could not read texture: " + name);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading texture: " + name);
            return null;
        }
    }

    public void bulkLoadTexture() {
        List<String> filePaths;
        filePaths = ResourceFileReader.readFromFile("texturePaths.txt"); // this already ignores blank lines.

        List<String> fileNames = new ArrayList<>();
        for (String pathString : filePaths) {
            if (pathString != null) {
                try {
                    Path path = Paths.get(pathString);
                    Path fileNamePath = path.getFileName();
                    if (fileNamePath != null) {
                        fileNames.add(fileNamePath.toString());
                    } else {
                        // Handles root directories or paths ending with separator
                        // getFileName() returns null for root (e.g., "/")
                        // For "/path/to/directory/", it returns "directory"
                        fileNames.add("it's root"); // handle
                    }
                } catch (Exception e) {
                    // Handle potential invalid path strings if necessary
                    System.err.println("Error processing path: " + pathString + " - " + e.getMessage());
                    fileNames.add("failed"); // error marker
                }
            } else {
                fileNames.add("null!"); // Handle null paths
            }
        }

        for (int i = 0; i < fileNames.size(); i++) {
            loadTexture(fileNames.get(i), filePaths.get(i));
        }
    }

    public BufferedImage getTexture(String name) {
        BufferedImage texture = textureMap.get(name);
        if (texture == null) {
            System.err.println("Texture requested, but not found: " + name);
        }
        return texture;
    }

    public void unloadTexture(String name) {
        if (textureMap.containsKey(name)) {
            textureMap.remove(name);
            System.out.println("Texture '" + name + "' unloaded.");
        }
    }

    public void unloadAllTextures() {
        textureMap.clear();
        System.out.println("All textures unloaded.");
    }
}
