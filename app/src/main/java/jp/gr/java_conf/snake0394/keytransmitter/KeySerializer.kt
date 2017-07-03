package jp.gr.java_conf.snake0394.keytransmitter

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.*

class KeySerializer : JsonSerializer<ArrayList<BaseKey>> {

    override fun serialize(keyList: ArrayList<BaseKey>?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement? {
        if (keyList == null) {
            return null
        } else {
            val jsonArray = JsonArray()
            for (baseKey in keyList) {
                val c = map[baseKey.keyType] ?: throw RuntimeException("Unknow class: " + baseKey.keyType)
                jsonArray.add(context.serialize(baseKey, c))
            }
            return jsonArray
        }
    }

    companion object {
        private val map = TreeMap<KeyType, Class<*>>()

        init {
            map.put(KeyType.EMPTY, EmptyKey::class.java)
            map.put(KeyType.KNOB, ControlKnob::class.java)
            map.put(KeyType.FLICK, FlickKey::class.java)
            map.put(KeyType.LONG_PRESS, LongPressKey::class.java)
            map.put(KeyType.RELEASED, NormalKey::class.java)
            map.put(KeyType.PRESSING, PressingKey::class.java)
        }
    }
}