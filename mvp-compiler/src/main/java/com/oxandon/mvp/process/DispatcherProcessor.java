package com.oxandon.mvp.process;

import com.google.auto.service.AutoService;
import com.oxandon.mvp.annotation.Controller;
import com.oxandon.mvp.annotation.Dispatcher;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * 用于处理生成Dispatcher
 * Created by peng on 2017/7/31.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class DispatcherProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> sets = new HashSet<>();
        sets.add(Controller.class.getCanonicalName());
        sets.add(Dispatcher.class.getCanonicalName());
        return sets;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> controllers = roundEnvironment.getElementsAnnotatedWith(Controller.class);
        if (null == controllers || controllers.size() == 0) {
            return false;
        }
        Set<? extends Element> dispatchers = roundEnvironment.getElementsAnnotatedWith(Dispatcher.class);
        if (null == dispatchers || dispatchers.size() != 1) {
            return false;
        }
        final Element dispatcher = dispatchers.iterator().next();
        ElementInfo info = new ElementInfo(dispatcher);
        if (null == info.getFile()) {
            return false;
        }
        Map<String, List<Element>> maps = collectByModule(controllers);
        String outputName = dispatcher.getAnnotation(Dispatcher.class).value();
        if (null == outputName || outputName.length() == 0) {
            outputName = dispatcher.getSimpleName().toString() + "Dispatcher";
        }
        String content = buildDispatcherContent(info.getPkg(), outputName, maps);
        File outFile = new File(info.getFile().getParent(), outputName + ".java");
        writeToFile(outFile, content);
        return false;
    }

    /**
     * 按module归类
     *
     * @param elements
     * @return
     */
    private Map<String, List<Element>> collectByModule(Set<? extends Element> elements) {
        Map<String, List<Element>> maps = new HashMap<>();
        for (Element element : elements) {
            Controller controller = element.getAnnotation(Controller.class);
            String module = controller.module();
            if (maps.containsKey(module)) {
                maps.get(module).add(element);
            } else {
                List<Element> list = new ArrayList<>();
                list.add(element);
                maps.put(module, list);
            }
        }
        return maps;
    }

    /**
     * 构建输出内容
     *
     * @param pkg       包名
     * @param className 类名
     * @param maps
     * @return
     */
    private String buildDispatcherContent(String pkg, String className, Map<String, List<Element>> maps) {
        List<String> codes = new ArrayList<>();
        List<String> imports = new ArrayList<>();
        imports.add("android.support.annotation.NonNull");
        imports.add("com.oxandon.found.arch.impl.MvpDispatcher");
        imports.add("com.oxandon.found.arch.protocol.IMvpPresenter");
        imports.add("java.util.ArrayList");
        imports.add("java.util.List");

        Iterator<Map.Entry<String, List<Element>>> iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Element>> entry = iterator.next();
            String module = entry.getKey();
            List<Element> list = entry.getValue();
            if (null != module && module.length() > 0) {
                codes.add(String.format("/****module %s***/", module));
            }
            for (Element element : list) {
                ElementInfo info = new ElementInfo(element);
                imports.add(info.getPkg() + "." + info.getName());
                codes.add(String.format("list.add(%s.class);", info.getName()));
            }
        }
        StringBuffer sbf = new StringBuffer();
        sbf.append(String.format("package %s;\n", pkg));
        for (int i = 0; i < imports.size(); i++) {
            sbf.append("\nimport " + imports.get(i) + ";");
        }
        sbf.append("\n\n");
        sbf.append(String.format("public class %s extends MvpDispatcher{", className));
        sbf.append("\n\n\t@NonNull");
        sbf.append("\n\t@Override");
        sbf.append("\n\tprotected List<Class<? extends IMvpPresenter>> support() {");
        sbf.append("\n\t\tList<Class<? extends IMvpPresenter>> list = new ArrayList<>();");
        for (int i = 0; i < codes.size(); i++) {
            sbf.append("\n\t\t" + codes.get(i));
        }
        sbf.append("\n\t\treturn list;");
        sbf.append("\n\t}");
        sbf.append("\n}");
        return sbf.toString();
    }

    /**
     * 文件写入
     *
     * @param file
     * @param content
     */
    private void writeToFile(File file, String content) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            quitClose(bw);
        }
    }

    private void quitClose(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}