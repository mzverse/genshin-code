package mz.genshincode.data.asset;

import mz.genshincode.Side;
import mz.genshincode.data.GenshinDataAssets;

import java.util.ArrayList;
import java.util.List;

public class AssetsGenerator
{
    AssetBundle.Mode mode = AssetBundle.Mode.OVERLIMIT;
    Side side = Side.SERVER;

    List<Asset> assets = new ArrayList<>();
    List<Asset> dependencies = new ArrayList<>();

    public AssetBundle.Mode getMode()
    {
        return this.mode;
    }
    public void setMode(AssetBundle.Mode value)
    {
        this.mode = value;
    }

    public Side getSide()
    {
        return this.side;
    }
    public void setSide(Side side)
    {
        this.side = side;
    }

    long nextGuid = (1L << 30) + 1L;
    public long allocateGuid()
    {
        return this.nextGuid++;
    }

    public void addAsset(Asset value)
    {
        this.assets.add(value);
    }
    public void addDependency(Asset value)
    {
        this.dependencies.add(value);
    }

    public GenshinDataAssets toData()
    {
        return new GenshinDataAssets(AssetBundle.newBuilder()
            .addAllAsset(this.assets)
            .addAllDependency(this.dependencies)
            .setExportInfo(this.buildExportInfo())
            .setEngineVersion("6.3.0")
            .setMode(this.mode)
            .build());
    }

    String buildExportInfo()
    {
        // TODO
        return "113086933-1768985417-1073741826-\\awa.gia";
    }
}
