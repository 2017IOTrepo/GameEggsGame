package com.example.a41448.huawu.application;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;

import java.io.File;

public abstract class BaseDiscCache implements DiscCacheAware {
    private static final String ERROR_ARG_NULL = "\"%s\" argument must be not null";
    protected File cacheDir;
    private FileNameGenerator fileNameGenerator;

    public BaseDiscCache(File cacheDir) {
        this(cacheDir, DefaultConfigurationFactory.createFileNameGenerator());
    }

    public BaseDiscCache(File cacheDir, FileNameGenerator fileNameGenerator) {
        if (cacheDir == null) {
            throw new IllegalArgumentException(String.format("\"%s\" argument must be not null", "cacheDir"));
        } else if (fileNameGenerator == null) {
            throw new IllegalArgumentException(String.format("\"%s\" argument must be not null", "fileNameGenerator"));
        } else {
            this.cacheDir = cacheDir;
            this.fileNameGenerator = fileNameGenerator;
        }
    }

    public File get(String key) {
        String fileName = this.fileNameGenerator.generate(key);
        return new File(this.cacheDir, fileName);
    }

    public void clear() {
        File[] files = this.cacheDir.listFiles();
        if (files != null) {
            File[] arr$ = files;
            int len$ = files.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                File f = arr$[i$];
                f.delete();
            }
        }

    }
}