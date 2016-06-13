package com.alex.fakecall.utils;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FilenameFilter;


public class ExtensionFilter implements FilenameFilter {

    private String[] acceptExt;

    public ExtensionFilter(@NonNull String[] acceptExt) {
        this.acceptExt = acceptExt;
    }

    @Override
    public boolean accept(File dir, String filename) {
        for (String s : acceptExt) {
            if (filename.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }
}
