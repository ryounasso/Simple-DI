package Container;
import anntations.InjectClass;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DIContainer {
    static Map<String, Class> maps = new HashMap<>();
    static Map<String, Object> beans = new HashMap<>();

    static void register(String name, Class type) {
        maps.put(name, type);
    }

    public Object getInstance(Class clazz) throws Exception {
        // インスタンスを生成
        Object obj = clazz.getConstructor().newInstance();

        // 宣言済みのフィールドの定義情報を取得する
        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields) {
            // アノテーションを取得し、関係性を構築する
            Annotation[] annotations = f.getAnnotations();
            for (Annotation annotation : annotations) {
                // インジェクションのアノテーションだったら
                if (annotation instanceof InjectClass) {
                    Class target = this.findImplClass(f.getType());
                    Object instance = this.getInstance(target);

                    f.setAccessible(true);
                    f.set(obj, instance);
                }
            }
        }
        return obj;
    }

    public static void autoRegister() throws Exception {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        String classPath = contextClassLoader.getResource("").getPath();
        File root = new File(classPath);
        List<File> list = getClassList(root);
        for (File file : list) {
            // 完全修飾クラス名が欲しい
            String name = file.getAbsolutePath().replace(classPath, "")
                    .replace("/", ".").replace(".class", "");
            // ここからリフレクション
            // クラスの定義情報が取得できる
            Class clazz = Class.forName(name);
            if (clazz.isInterface()) {
                continue;
            }
            if (clazz.isAnnotationPresent(Named.class)) {
                register(clazz.getSimpleName(), clazz);
            }
        }
    }

    public static Object getBean(String name) {
        return beans.computeIfAbsent(name, key -> {
            Class type = maps.get(name);
            try {
                return createObject(type);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static <T> T createObject(Class<T> type) throws InstantiationException, IllegalAccessException {
        T object = type.newInstance();
        for (Field field : type.getDeclaredFields()) {
            Inject inject = field.getAnnotation(Inject.class);
            if (inject == null) {
                continue;
            }
            field.setAccessible(true);
            field.set(object, getBean(field.getName()));
        }
        return  object;
    }

    public Class findImplClass(Class target) throws Exception {
        // 現在実行中のスレッドのクラスローダーを取得。
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

        String classPath = contextClassLoader.getResource("").getPath();
        File root = new File(classPath);
        // クラスファイルの一覧を取得
        List<File> list = getClassList(root);
        for (File file : list) {
            // 完全修飾クラス名が欲しい
            String name = file.getAbsolutePath().replace(classPath, "")
                    .replace("/", ".").replace(".class", "");
            // ここからリフレクション
            // クラスの定義情報が取得できる
            Class clazz = Class.forName(name);
            if (clazz.isInterface()) {
                continue;
            }
            if (target.isAssignableFrom(clazz)) {
                return clazz;
            }
        }
        return null;
    }

    private static List<File> getClassList(File targetDir) {
        List<File> list = new ArrayList<>();
        // どんな種類のディレクトリとファイルがあるかがわかる
        File[] files = targetDir.listFiles();
        if (files == null) {
            return null;
        }
        for (File file : files) {
            // ディレクトリだった場合は再帰呼び出しを行う
            if (file.isDirectory()) {
                list.addAll(getClassList(file));
                continue;
            }
            if (file.getName().endsWith(".class")) {
                list.add(file);
            }
        }
        return list;
    }
}
