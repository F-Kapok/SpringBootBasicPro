package com.fans.utils;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * className: ReflectUtils
 *
 * @author k
 * @version 1.0
 * @description 反射工具类
 * @date 2018-12-20 14:14
 **/
@Slf4j
public class ReflectUtils {

    /**
     * 获取指定包内的 指定类
     *
     * @param packageName 包名
     * @param className   类名
     * @return 反射类
     */
    public static Class<?> getClass(String packageName, String className) {
        ImmutableSet<Class<?>> classSet = getClasses(packageName);
        List<Class<?>> classList = classSet.stream().filter(aClass -> aClass.getSimpleName().equals(className)).collect(Collectors.toList());
        if (classList.isEmpty()) {
            return null;
        }
        return classList.get(0);
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param pack 包名
     * @return 对象集合
     */
    public static ImmutableSet<Class<?>> getClasses(String pack) {
        // 第一个class类的集合
        ImmutableSet.Builder<Class<?>> classSet = ImmutableSet.builder();
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    log.info("--> file类型的扫描");
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, classSet);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    log.info("--> jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果是一个.class文件 而且不是目录
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    // 去掉后面的".class" 获取真正的类名
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    try {
                                        // 添加到classes
                                        classSet.add(Class.forName(packageName.concat(".").concat(className)));
                                    } catch (ClassNotFoundException e) {
                                        log.error("--> 添加用户自定义视图类错误 找不到此类的.class文件");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        log.error("--> 在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (classSet.build().isEmpty()) {
            log.warn("--> file 404 or empty");
        }
        return classSet.build();
    }

    /**
     * 获取maven配置文件对象模型
     *
     * @return maven对象
     */
    public static Model getMavenModel() {
        try {
            File file = new File("pom.xml");
            FileInputStream fileInputStream = new FileInputStream(file);
            MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            return mavenXpp3Reader.read(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return new Model();
        }
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName 包名
     * @param packagePath 包路径
     * @param classSet    类集合
     */
    private static void findAndAddClassesInPackageByFile(String packageName,
                                                         String packagePath,
                                                         ImmutableSet.Builder<Class<?>> classSet) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            log.warn("--> 用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
        File[] dirFiles = dir.listFiles(file -> file.isDirectory() || (file.getName().endsWith(".class")));
        // 循环所有文件
        assert dirFiles != null;
        for (File file : dirFiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName.concat(".").concat(file.getName()), file.getAbsolutePath(), classSet);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    // classSet.add(Class.forName(packageName + '.' + className)); 这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classSet.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }
}
