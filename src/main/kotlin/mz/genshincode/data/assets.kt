package mz.genshincode.data

import mz.genshincode.data.asset.AssetsGenerator

fun GenshinDataAssets(configuration: AssetsGenerator.() -> Unit) =
    AssetsGenerator().apply(configuration).toData()