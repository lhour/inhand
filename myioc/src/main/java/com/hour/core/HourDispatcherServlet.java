package com.hour.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hour.annotation.HourAutowired;
import com.hour.annotation.HourController;
import com.hour.annotation.HourRequestMapping;
import com.hour.annotation.HourRequestParam;
import com.hour.annotation.HourService;

public class HourDispatcherServlet extends HttpServlet {

    //配置信息
    private Properties contextConfig = new Properties();    
    //存储所有类名
    private List<String> classNames = new ArrayList<>();
    //简易ioc容器
    private Map<String, Object> ioc = new HashMap<>();
    //请求与方法映射
    private Map<String, Method> handlerMapping = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {

        //加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));
        //扫描指定包
        doScanner(contextConfig.getProperty("scanPackage"));
        //创建实例到ioc
        doInstance();
        //自动注入
        doAutowired();
        //请求方法对应
        initHandlerMapping();

        System.out.println("Hour ioc is init");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception{
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll("contextPath", "").replaceAll("/+", "/");
        if(!this.handlerMapping.containsKey(url)){
            resp.getWriter().write("404");
            return;
        }
        Method method = this.handlerMapping.get(url);
        Map<String, String[]> params = req.getParameterMap();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Map<String, String[]> parameterMap = req.getParameterMap();
        Object[] paramValues = new Object[parameterTypes.length];

        for(int i = 0; i < parameterTypes.length; i ++) {
            Class<?> parameterType = parameterTypes[i];
            if(parameterType == HttpServletRequest.class) {
                paramValues[i] = req;
                continue;
            }else if(parameterType == HttpServletResponse.class) {
                paramValues[i] = resp;
                continue;
            }else if(parameterType == String.class) {
                Annotation[][] pa = method.getParameterAnnotations();
                for(int j = 0; j < pa.length; j ++) {
                    for(Annotation a : pa[i]) {
                        if(a instanceof HourRequestParam) {
                            String paramName = ((HourRequestParam) a).value();
                            if(!paramName.trim().equals("")){
                                String value = Arrays.toString(parameterMap.get(paramName))
                                .replaceAll("\\[|\\[", "")
                                .replaceAll("\\s", ",");
                                paramValues[i] = value;
                            }
                        }
                    }
                }
            }
        }

        String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
        method.invoke(ioc.get(beanName), new Object[]{req, resp, params.get("name")[0]});
    }

    /**
     * 拼接访问路径
     */
    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(HourController.class)) {
                continue;
            }
            String baseurl = "";
            if (clazz.isAnnotationPresent(HourRequestMapping.class)) {
                HourRequestMapping requestMapping = clazz.getAnnotation(HourRequestMapping.class);
                baseurl = requestMapping.value();
            }

            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(HourRequestMapping.class)) {
                    continue;
                }
                HourRequestMapping requestMapping = method.getAnnotation(HourRequestMapping.class);
                String url = "/" + baseurl + "/" + requestMapping.value();
                url = url.replaceAll("/+", "/");
                handlerMapping.put(url, method);
            }
        }
    }

    /**
     * 实现注入
     */
    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(HourAutowired.class)) {
                    continue;
                }
                HourAutowired autowired = field.getAnnotation(HourAutowired.class);
                String beanName = autowired.value().trim();
                if (!beanName.equals("")) {
                    beanName = field.getType().getName();
                }
                field.setAccessible(true);

                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ioc 中放入实例
     */
    private void doInstance() {

        if (classNames.isEmpty()) {
            return;
        }
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(HourController.class)) {
                    Object instance = clazz.newInstance();
                    String beanName = toLowerFirstCase(clazz.getSimpleName());
                    ioc.put(beanName, instance);
                } else if (clazz.isAnnotationPresent(HourService.class)) {
                    HourService service = clazz.getAnnotation(HourService.class);
                    String beanName = service.value();
                    if (beanName.trim().equals("")) {
                        beanName = toLowerFirstCase(clazz.getSimpleName());
                    }
                    Object instance = clazz.newInstance();
                    ioc.put(beanName, instance);
                    for (Class<?> i : clazz.getInterfaces()) {
                        if (ioc.containsKey(i.getName())) {
                            throw new Exception("the " + i.getName() + "is exists!");
                        }
                        ioc.put(i.getName(), instance);
                    }
                } else {
                    continue;
                }
            }
        } catch (Exception e) {
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

    /**
     * 扫描包下的类，添加到 classNames 中
     * 
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName().replace(".class", ""));
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                classNames.add(className);
            }
        }
    }

    /**
     * 加载配置文件
     * 
     * @param contextConfigLocation
     */
    private void doLoadConfig(String contextConfigLocation) {
        //读取配置文件
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(contextConfigLocation));
            contextConfig.load(fis);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }catch (IOException e) {
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

    public static void main(String[] args) {
        
    }
}
