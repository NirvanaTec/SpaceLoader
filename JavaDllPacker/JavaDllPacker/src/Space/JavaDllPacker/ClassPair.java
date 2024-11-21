package Space.JavaDllPacker;

public class ClassPair implements Comparable<ClassPair> {
    public byte[] classData;
    public int priority;
    public ClassInfo classInfo;

    public ClassPair(byte[] classData) {
        this.classData = classData;
        this.priority = 0;
    }

    public int compareTo(ClassPair o) {
        return o.priority - this.priority;
    }
}
