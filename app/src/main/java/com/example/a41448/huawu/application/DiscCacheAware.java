package com.example.a41448.huawu.application;

import java.io.File;

public interface DiscCacheAware {
    void put(String var1, File var2);

    File get(String var1);

    void clear();
}
