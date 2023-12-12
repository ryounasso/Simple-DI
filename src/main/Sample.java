package main;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Sample {
    public void test (Object obj) throws InvocationTargetException, IllegalAccessException {
        //  何クラスがわからないとき
        Class clazz = obj.getClass();
        System.out.println(clazz.getName());

        // フィールドを取得
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }

        // メソッドを取得
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println(m.getName());
            System.out.println(m.invoke(obj));
        }
    }
}
