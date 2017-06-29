package com.myapplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by alan on 17-6-28.
 */
public class ImageDirs {
    public enum Type {
        IMAGE, VEDIO, AUDIO
    }

    String dirName;
    String dirPath;
    List<String> files = new ArrayList<String>();
    HashSet<String> selectedFiles = new HashSet<String>();
    //List<String> ids=new ArrayList<String>();

    String firstImagePath;
    Type type=Type.IMAGE;

    public ImageDirs(String dirPath) {
        super();
        this.dirPath = dirPath;
    }

    public void addFile(String file) {
        files.add(file);
    }

    public String getDirName() {
        return dirName;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}
