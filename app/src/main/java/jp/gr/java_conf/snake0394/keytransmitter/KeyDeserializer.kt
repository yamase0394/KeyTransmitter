package jp.gr.java_conf.snake0394.keytransmitter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.*

class KeyDeserializer : JsonDeserializer<List<BaseKey>> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<BaseKey> {
        val list = ArrayList<BaseKey>()
        val jsonArray = json.asJsonArray

        for (jsonElement in jsonArray) {
            val keyType = jsonElement.asJsonObject.get("keyType").asString
            Logger.d("KeyDeserializer", "keyType = " + keyType)
            val type = map[keyType] ?: throw RuntimeException("Unknow KeyType: " + keyType)
            list.add(context.deserialize<BaseKey>(jsonElement, type))
        }

        return list
    }

    companion object {
        private val map = TreeMap<String, Class<*>>()

        init {
            map.put(KeyType.EMPTY.toString(), EmptyKey::class.java)
            map.put(KeyType.KNOB.toString(), ControlKnob::class.java)
            map.put(KeyType.FLICK.toString(), FlickKey::class.java)
            map.put(KeyType.LONG_PRESS.toString(), LongPressKey::class.java)
            map.put(KeyType.RELEASED.toString(), NormalKey::class.java)
            map.put(KeyType.PRESSING.toString(), PressingKey::class.java)
        }
    }

}