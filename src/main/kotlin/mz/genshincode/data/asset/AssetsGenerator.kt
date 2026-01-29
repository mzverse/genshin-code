package mz.genshincode.data.asset

import mz.genshincode.Side
import mz.genshincode.data.GenshinDataAssets
import mz.genshincode.GenshinDsl

@GenshinDsl
class AssetsGenerator {
    var mode: AssetBundle.Mode? = AssetBundle.Mode.OVERLIMIT
    @JvmField
    var side: Side? = Side.SERVER

    var assets: MutableList<Asset?> = ArrayList<Asset?>()
    var dependencies: MutableList<Asset?> = ArrayList<Asset?>()

    var nextGuid: Long = (1L shl 30) + 1L
    fun allocateGuid(): Long {
        return this.nextGuid++
    }

    fun addAsset(value: Asset?) {
        this.assets.add(value)
    }

    fun addDependency(value: Asset?) {
        this.dependencies.add(value)
    }

    fun toData(): GenshinDataAssets {
        return GenshinDataAssets(
            AssetBundle.newBuilder()
                .addAllAsset(this.assets)
                .addAllDependency(this.dependencies)
                .setExportInfo(this.buildExportInfo())
                .setEngineVersion("6.3.0")
                .setMode(this.mode)
                .build()
        )
    }

    fun buildExportInfo(): String {
        // TODO
        return "113086933-1768985417-1073741826-\\awa.gia"
    }
}
