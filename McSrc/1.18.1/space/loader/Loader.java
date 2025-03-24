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

            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            Module baseModule = Object.class.getModule();
            Class<?> currentClass = Loader.class;
            long addr = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
            unsafe.getAndSetObject(currentClass, addr, baseModule);
            this.setContextClassLoader(contextClassLoader);
            final Method declaredMethod = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
            declaredMethod.setAccessible(true);

            Class<?> Inject = null;
            for (final byte[] array : this.classes) {
                if (array != null) {
                    final Object clazz1 = declaredMethod.invoke(contextClassLoader, null, array, 0, array.length, contextClassLoader.getClass().getProtectionDomain());
                    if (clazz1 != null) {
                        final Class<?> clazz = (Class<?>) clazz1;
                        String name = clazz.getName();
                        System.out.println("4A" + name);
                        if (name.contains(className)) {
                            Inject = clazz;
                            System.out.println("4AOK");
                        }
                    } else {
                        System.out.println("4ANull");
                    }
                } else {
                    System.out.println("3ANull");
                }
            }
            System.out.println("4AFinish");
            if (Inject != null) {
                System.out.println("Loading....");
                Inject.getDeclaredMethod("Load").invoke(null);
            } else {
                System.out.println("5AError");
            }
            System.out.println("----Nirvana AND Space---");
        } catch (Throwable e) {
            e.fillInStackTrace();
        }
    }
}