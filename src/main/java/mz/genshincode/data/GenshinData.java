package mz.genshincode.data;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class GenshinData
{
    public void save(File file) throws IOException
    {
        try(DataOutputStream out = new DataOutputStream(Files.newOutputStream(file.toPath())))
        {
            this.save(out);
        }
    }
    public void save(DataOutputStream out) throws IOException
    {
        byte[] data = this.encode();
        final int total = data.length + 24;
        out.writeInt(total - 4);
        out.writeInt(1); // schema_version
        out.writeInt(0x0326); // head_tag
        out.writeInt(this.getType().getCode());
        out.writeInt(data.length);
        out.write(data);
        out.writeInt(0x0679); // tail_tag
    }

    public abstract byte[] encode();

    public abstract Type getType();

    @Override
    public String toString()
    {
        return String.format("GenshinData(type=%s)", this.getType());
    }

    public enum Type
    {
        PROJECT(1),
        LEVEL(2),
        ASSETS(3),
        RUNTIME(4)
        ;

        final int code;

        public int getCode()
        {
            return this.code;
        }

        Type(int code)
        {
            this.code = code;
        }
    }
}
