package io.ztc.tools.cache

import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @param context Context
 * @param name 存储命名
 * @param default 存储默认值
 */
@Suppress("UNCHECKED_CAST")
class Cache<T>(private val context: Context, private val name: String?, private val default: T) : ReadWriteProperty<Any?, T> {

    private var time: Int? = null

    constructor(context: Context, name: String?, default: T, time: Int) : this(context, name, default) {
        this.time = time
    }

    private val cache: CACHE by lazy {
        CACHE.get(context)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = with(cache) {
        val res: Any = when (default) {
            is String -> getString(name)
            is Int -> getInt(name, default)
            else -> throw IllegalArgumentException("获取过程异常...")
        }
        res as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = with(cache) {

        when (value) {
            is String -> if (time != null) put(name, value, time!!) else put(name, value)
            is Int -> if (time != null) put(name, value, time!!) else put(name, value)
            else -> throw IllegalArgumentException("缓存过程异常...")
        }
    }
}