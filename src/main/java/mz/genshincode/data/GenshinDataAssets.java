package mz.genshincode.data;

import java.io.DataInput;
import java.io.IOException;

import mz.genshincode.data.asset.*;

/**
 * 资产数据类
 * 用于将 Graph 对象保存为 .gia 文件
 */
public class GenshinDataAssets extends GenshinData
{
    AssetBundle assets;

    public GenshinDataAssets(AssetBundle assets)
    {
        this.assets = assets;
    }

    @Override
    public Type getType()
    {
        return Type.ASSETS;
    }

    @Override
    public byte[] encode()
    {
        return this.assets.toByteArray();
    }

    public static GenshinDataAssets load(DataInput input) throws IOException
    {
        input.readInt(); // size - 4
        input.readInt(); // schema_version
        input.readInt(); // head_tag
        input.readInt();
        byte[] data = new byte[input.readInt()];
        input.readFully(data);
        input.readInt(); // tail_tag
        return new GenshinDataAssets(AssetBundle.parseFrom(data));
    }

    @Override
    public String toString()
    {
        return super.toString() + this.assets;
    }
}
