package Space.JavaDllPacker;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ClassInfo {
    public String name;
    public String name1;
    public String superClass;
    public String[] interfaces;

    public ClassInfo(byte[] classData) {
        this.parse(ByteBuffer.wrap(classData));
    }

    private void parse(ByteBuffer buf) {
        if (buf.order(ByteOrder.BIG_ENDIAN).getInt() == -889275714) {
            buf.getChar();
            buf.getChar();
            int num = buf.getChar();
            String[] cpStrings = new String[num];
            short[] cpClasses = new short[num];

            int i;
            for(i = 1; i < num; ++i) {
                byte tag = buf.get();
                switch (tag) {
                    case 1:
                        cpStrings[i] = this.decodeString(buf);
                        break;
                    case 2:
                    case 13:
                    case 14:
                    case 17:
                    default:
                        return;
                    case 3:
                        buf.getInt();
                        break;
                    case 4:
                        buf.getFloat();
                        break;
                    case 5:
                        buf.getLong();
                        ++i;
                        break;
                    case 6:
                        buf.getDouble();
                        ++i;
                        break;
                    case 7:
                        cpClasses[i] = buf.getShort();
                        break;
                    case 8:
                    case 16:
                        buf.getChar();
                        break;
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 18:
                        buf.getChar();
                        buf.getChar();
                        break;
                    case 15:
                        buf.get();
                        buf.getChar();
                        break;
                }
            }

            buf.getChar();
            this.name = cpStrings[cpClasses[buf.getChar()]];
            this.name1 = name.replace('/', '.');
            this.superClass = cpStrings[cpClasses[buf.getChar()]].replace('/', '.');
            this.interfaces = new String[buf.getChar()];

            for(i = 0; i < this.interfaces.length; ++i) {
                this.interfaces[i] = cpStrings[cpClasses[buf.getChar()]].replace('/', '.');
            }

        }
    }

    private String decodeString(ByteBuffer buf) {
        int size = buf.getChar();
        int oldLimit = buf.limit();
        buf.limit(buf.position() + size);
        StringBuilder sb = new StringBuilder(size + (size >> 1) + 16);

        while(buf.hasRemaining()) {
            byte b = buf.get();
            if (b > 0) {
                sb.append((char)b);
            } else {
                int b2 = buf.get();
                if ((b & 240) != 224) {
                    sb.append((char)((b & 31) << 6 | b2 & 63));
                } else {
                    int b3 = buf.get();
                    sb.append((char)((b & 15) << 12 | (b2 & 63) << 6 | b3 & 63));
                }
            }
        }

        buf.limit(oldLimit);
        return sb.toString();
    }
}
