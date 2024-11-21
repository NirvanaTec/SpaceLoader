package Space.JavaDllPacker;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JavaDllPacker {

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Not enough args");
                return;
            }

            ZipInputStream zipFile = new ZipInputStream(Files.newInputStream(Paths.get(args[0])));
            List<ClassPair> classes = new ArrayList<>();
            HashMap<String, ClassPair> classMap = new HashMap<>();

            while(true) {
                ZipEntry entry;
                do {
                    if ((entry = zipFile.getNextEntry()) == null) {
                        classes.forEach((_item) -> classes.forEach((cPair) -> {
                            ClassInfo cInfo = cPair.classInfo;
                            if (classMap.containsKey(cInfo.superClass)) {
                                classMap.get(cInfo.superClass).priority = Math.max(classMap.get(cInfo.superClass).priority, cPair.priority + 1);
                            }

                            String[] var3 = cInfo.interfaces;

                            for (String iFace : var3) {
                                if (classMap.containsKey(iFace)) {
                                    classMap.get(iFace).priority = Math.max(classMap.get(iFace).priority, cPair.priority + 1);
                                }
                            }

                        }));

                        Collections.sort(classes);
                        classes.forEach((cls) -> System.out.print(cls.classInfo.name + "[#@@#]"));
                        System.exit(0);
                        return;
                    }
                } while(!entry.getName().endsWith(".class"));

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] data = new byte[16384];

                int nRead;
                while((nRead = zipFile.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                byte[] classData = buffer.toByteArray();
                classes.add(new ClassPair(classData));
                ClassInfo cInfo = new ClassInfo(classes.get(classes.size() - 1).classData);
                classes.get(classes.size() - 1).classInfo = cInfo;
                classMap.put(cInfo.name1, classes.get(classes.size() - 1));
            }

        } catch (Exception e) {
            e.fillInStackTrace();
            System.exit(1);
        }
    }
}