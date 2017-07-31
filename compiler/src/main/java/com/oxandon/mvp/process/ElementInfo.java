package com.oxandon.mvp.process;

import java.io.File;
import java.lang.reflect.Field;

import javax.lang.model.element.Element;

/**
 * Created by peng on 2017/7/31.
 */

public class ElementInfo {
    private String name;
    private String pkg;
    private File file;

    public ElementInfo(Element element) {
        name = element.getSimpleName().toString();
        try {
            //decode pkg
            Field field = element.getClass().getField("owner");
            Object packageSymbol = field.get(element);
            field = packageSymbol.getClass().getField("fullname");
            pkg = field.get(packageSymbol).toString();
            //decode file
            field = element.getClass().getField("sourcefile");
            Object sourceFile = field.get(element);
            field = sourceFile.getClass().getDeclaredField("file");
            field.setAccessible(true);
            file = (File) field.get(sourceFile);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        }
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public String getPkg() {
        return pkg;
    }
}
