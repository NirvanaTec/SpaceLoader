/*
 * 涅槃科技 and 风横
 * https://npyyds.top/
 * https://gitee.com/newNP/
 * https://github.com/NirvanaTec/
 * 最终解释权归涅槃科技所有，涅槃科技版权所有。
 */
package space.loader;

import sun.misc.Unsafe;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.ProtectionDomain;
import java.util.Set;

public class Loader extends Thread {

    // 开启 Debug.log 打印
    public static boolean isLog;
    // 日志 Debug.log 打印流
    private static PrintWriter writer;

    static {
        File file = new File("C:\\FaithX\\Debug.log");
        if (file.exists()) {
            try {
                writer = new PrintWriter(file, StandardCharsets.UTF_8);
            } catch (IOException e) {
                Loader.println(e);
            }
            isLog = true;
            writer.println("Starting!");
            writer.flush();
        }
    }

    private final byte[][] classes;

    public Loader(final byte[][] classes) {
        this.classes = classes;
    }

    public static int a(final byte[][] array) {
        try {
            Loader loader = new Loader(array);
            loader.start();
            return 100; // 会返回给 火山
        } catch (Exception e) {
            Loader.println(e);
        }
        return -1;
    }

    public static byte[][] a(final int n) {
        return new byte[n][];
    }

    /**
     * 推荐的安全打印方法
     * 避免被 非开发者看见 从而造成影响
     */
    @SafeVarargs
    public static <T> void println(T... values) {
        if (!isLog) {
            return;
        }
        for (T value : values) {
            if (value == null) {
                continue;
            }
            try {
                if(value instanceof Throwable e) {
                    e.printStackTrace(writer);
                    writer.print(e);
                }else {
                    System.out.print(value);
                    writer.print(value);
                }
            } catch (Exception ignored) {
            }
        }
        try {
            System.out.println();
            writer.println();
            writer.flush();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void run() {
        try {
            println("----Nirvana AND Space---");
            String className = this.getClass().getName() + "Injection";

            ClassLoader contextClassLoader = null;

            while (contextClassLoader == null) {
                contextClassLoader = getLoaderThread();
            }

            println("Unsafe");
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            Module baseModule = Object.class.getModule();
            Class<?> currentClass = Loader.class;
            long addr = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
            unsafe.getAndSetObject(currentClass, addr, baseModule);
            this.setContextClassLoader(contextClassLoader);
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
            defineClass.setAccessible(true);

            println("Load" + this.classes.length + "....");

            Class<?> endpoint = null;

            for (final byte[] array : this.classes) {
                if (array == null) {
                    println("3ANull");
                    continue;
                }
                println("DefineClass");
                Class<?> clazz = (Class<?>) defineClass.invoke(contextClassLoader, null, array, 0, array.length, contextClassLoader.getClass().getProtectionDomain());
                String name = clazz.getName();
                println("4A" + name);
                if (name.contains(className)) {
                    println("Loading....");
                    endpoint = clazz;
                }
            }

            if (endpoint == null) {
                println("5ANull");
                return;
            }

            println("Invoke");
            println(endpoint.getName());

            // Load
            // Method method = endpoint.getDeclaredMethod("Load");
            // 避免因为 混淆 导致方法名改变
            for (Method method : endpoint.getDeclaredMethods()) {
                println(method.getName());
                if (!Modifier.isPublic(method.getModifiers())) {
                    continue;
                }
                if (!Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                if (method.getReturnType() != void.class) {
                    continue;
                }
                println("Invoke");
                method.invoke(null);
                break;
            }

            println("----Nirvana AND Space---");

        } catch (Exception e) {
            Loader.println(e);
        }
    }

    public static ClassLoader getLoaderThread() {
        Set<Thread> threadAllKey = Thread.getAllStackTraces().keySet();
        for (final Thread thread : threadAllKey) {
            if (thread == null){
                continue;
            }
            ClassLoader threadLoader = thread.getContextClassLoader();
            if (threadLoader == null) {
                continue;
            }
            String threadName = thread.getName();
            String loaderName = threadLoader.getClass().getName();
            if (threadName != null && threadName.contains("Render thread")) {
                println("1A", threadName, " 2A",loaderName);
                return threadLoader;
            }
            if (loaderName.contains("LaunchClassLoader") ||
                    loaderName.contains("RelaunchClassLoader") ||
                    loaderName.contains("TransformingClassLoader")) {
                println("1A", threadName, " 2A",loaderName);
                return threadLoader;
            }
            println("1B", threadName, " 2B",loaderName);
        }
        println("2AContextClassLoader");
        return null;
    }

}