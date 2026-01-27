package mz.genshincode

open class IdBased(var id: Long)
class Guid(id: Long) : IdBased(id)
class Config(id: Long) : IdBased(id)
class Prefab(id: Long) : IdBased(id)
class Faction(id: Long) : IdBased(id)