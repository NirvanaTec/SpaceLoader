/*
 * 涅槃科技 and 风横
 * https://npyyds.top/
 * https://gitee.com/newNP/
 * https://github.com/NirvanaTec/
 * 最终解释权归涅槃科技所有，涅槃科技版权所有。
 */
package space.loader;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Set;

public class Loader extends Thread {

    private final byte[][] classes;

    public Loader(final byte[][] classes) {
        this.classes = classes;
    }

    public static int a(final byte[][] array) {
        try {
            new Loader(array).start();
        } catch (Exception ignored) {
            System.out.println("Loader!!!");
        }
        return 100;
    }

    public static byte[][] a(final int n) {
        return new byte[n][];
    }

    @Override
    public void run() {
        try {
            System.out.println("----Nirvana AND Space---");
            String className = "space.loader.InjectionEndpoint";

            ClassLoader contextClassLoader = null;
            Set<Thread> threadAllKey = Thread.getAllStackTraces().keySet();

            for (final Thread thread : threadAllKey) {
                String name = thread.getName();
                if (name.equalsIgnoreCase("Render thread")) {
                    contextClassLoader = thread.getContextClassLoader();
                    System.out.println("1A" + name);
                }
            }

            if (contextClassLoader == null) {
                System.out.println("2AContextClassLoader");
                return;
            }

            System.out.println("Unsafe");
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

            System.out.println("Load" + this.classes.length + "....");
            for (final byte[] array : this.classes) {
                if (array == null) {
                    System.out.println("3ANull");
                    continue;
                }
                System.out.println("DefineClass");
                Class<?> clazz = (Class<?>) defineClass.invoke(contextClassLoader, null, array, 0, array.length, contextClassLoader.getClass().getProtectionDomain());
                String name = clazz.getName();
                System.out.println("4A" + name);
                if (name.contains(className)) {
                    System.out.println("Loading....");
                    clazz.getDeclaredMethod("Load").invoke(null);
                }
            }

            System.out.println("----Nirvana AND Space---");

        } catch (Exception e) {
            e.fillInStackTrace();
        }

    }
}