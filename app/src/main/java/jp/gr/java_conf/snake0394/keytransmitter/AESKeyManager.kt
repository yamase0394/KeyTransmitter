package jp.gr.java_conf.snake0394.keytransmitter

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESKeyManager {

    lateinit var key: ByteArray
        private set
        get

    init {
        initKey()
    }

    fun encrypt(plain: String, key: ByteArray): String {
        return encrypt(plain.toByteArray(Charsets.UTF_8), key)
    }

    fun encrypt(plain: String): String {
        return encrypt(plain, key)
    }

    fun encrypt(plain: ByteArray, key: ByteArray): String {
        val keySpec = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encrypted = cipher.doFinal(plain)
        return Base64.encodeToString(cipher.iv, Base64.NO_WRAP) + "?" + Base64.encodeToString(encrypted, Base64.NO_WRAP)
    }

    fun decrypt(iv: ByteArray, encrypted: ByteArray): ByteArray {
        return decrypt(iv, encrypted, key)
    }

    fun decrypt(iv: ByteArray, encrypted: ByteArray, key: ByteArray): ByteArray {
        val keySpec = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
        return cipher.doFinal(encrypted)
    }

    fun initKey() {
        val keyGen = KeyGenerator.getInstance("AES")
        val random = SecureRandom.getInstance("SHA1PRNG")
        keyGen.init(256, random)
        val key = keyGen.generateKey()
        this.key = key.encoded
    }
}