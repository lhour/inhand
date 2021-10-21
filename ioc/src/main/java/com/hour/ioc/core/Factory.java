package com.hour.ioc.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.hour.ioc.annotation.Autowired;
import com.hour.ioc.annotation.Bean;
import com.hour.ioc.annotation.Component;
import com.hour.ioc.annotation.Configuration;
import com.hour.ioc.annotation.ConfigurationProperties;
import com.hour.ioc.annotation.Value;
import com.hour.ioc.core.Factory;

public class Factory {

    // 配置信息
    private Properties config = new Properties();
    // 存储所有类名
    private List<String> classNames = new ArrayList<>();
    // 极简ioc容器
    private Map<String, Object> ioc = new HashMap<>();

    public Object getBean(String beanName) {
        return ioc.get(beanName);
    }

    public void init() {
        // 加载配置文件
        doLoadConfig();
        // 扫描指定包
        doScanner(config.getProperty("scanPackage"));
        // 创建实例到ioc
        doInstance();
        // 自己也放进去
        ioc.put("factory", this);
        // 自动注入
        doAutowired();
    }

    public Factory() {
        init();
    }

    private void doLoadConfig() {

        InputStream fis = this.getClass().getClassLoader().getResourceAsStream("app.properties");
        try {
            config.load(fis);
            System.out.println("==========配置文件加载完成=========");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis == null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doScanner(String scanPackage) {
        System.out.println("=======开始扫描指定包" + scanPackage + "========");
        // 获取项目根路径
        String url = this.getClass().getResource("/").getPath();
        String target = scanPackage.replace(".", "/");
        File classPath = new File(url + target);

        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName().replace(".class", ""));
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                System.out.println("===> " + className);
                classNames.add(className);
            }
        }
    }
    
    private void doInstance() {
        System.out.println("=======开始创建对象========");
        if (classNames.isEmpty()) {
            return;
        }
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                // 根据类注解将整个类实例化为单例对象
                if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Configuration.class)) {
                    Object instance = clazz.getConstructor().newInstance();
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    System.out.println(instance + "=>" + instance.getClass());
                    ioc.put(beanName, instance);

                    // 被properties注解修饰，从中获取值并赋值
                    if (clazz.isAnnotationPresent(ConfigurationProperties.class)) {
                        ConfigurationProperties cp = clazz.getAnnotation(ConfigurationProperties.class);
                        String base = cp.value();
                        Field[] fields = clazz.getDeclaredFields();
                        for (Field f : fields) {

                            if (config.getProperty(base + "." + f.getName()) != null) {
                                f.setAccessible(true);
                                f.set(instance, config.getProperty(base + "." + f.getName()));
                            }

                            // 如果此属性被Value修饰，则使用Value值
                            if (f.isAnnotationPresent(Value.class)) {
                                Value v = f.getAnnotation(Value.class);
                                f.set(instance, v.value());
                            }
                        }
                    }
                    for (Class<?> i : clazz.getInterfaces()) {
                        if (ioc.containsKey(i.getName())) {
                            throw new Exception("the " + i.getName() + "is exists!");
                        }
                        System.out.println(instance + "=>" + instance.getClass());
                        ioc.put(i.getName(), instance);
                    }

                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        // 被 Bean 注解修饰的方法，将对象获取出来
                        if (method.isAnnotationPresent(Bean.class)) {
                            Bean bean = method.getAnnotation(Bean.class);
                            String name = bean.value();
                            if (name.equals("")) {
                                name = method.getReturnType().getSimpleName();
                                name = toLowerFirstCase(name);
                            }
                            System.out.println(method.invoke(instance) + "=>" + method.invoke(instance).getClass());
                            ioc.put(name, method.invoke(instance));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历所有类及其中的属性
     * 如果被autowired修饰就自动赋值
     * 默认为首字母小写的类名
     * 可以自己指定注入类型
     */
    private void doAutowired() {
        System.out.println("=========开始注入==========");
        if (ioc.isEmpty()) {
            return;
        }
        try {
            
            for (String className : classNames) {
                Class<?> entry = Class.forName(className);
                Field[] fields = entry.getDeclaredFields();
                for (Field field : fields) {
                    
                    if (!field.isAnnotationPresent(Autowired.class)) {
                        continue;
                    }
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String beanName = autowired.value().trim();
                    // 如果为空自动判断类型
                    if (beanName.equals("")) {
                        beanName = field.getType().getSimpleName();
                        beanName = toLowerFirstCase(beanName);
                    }
                    field.setAccessible(true);
                    try {
                        field.set(ioc.get(toLowerFirstCase(entry.getSimpleName())), ioc.get(beanName));
                        System.out.println(beanName + "====>");
                        System.out.println(field);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 首字母转小写
     * 
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        chars[0] += 32;
        return new String(chars);
    }
}
