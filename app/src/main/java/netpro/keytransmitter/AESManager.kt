package netpro.keytransmitter

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by snake0394 on 2017/06/20.
 */

class AESManager {

    val rawKey: ByteArray = initKey()

    /**
     * Encrypt plain string and encode to Base64

     * @param plain
     * *
     * @return IV?Encrypted
     */
    fun encrypt(plain: String): String {
        val keySpec = SecretKeySpec(rawKey, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encrypted = cipher.doFinal(plain.toByteArray())
        return Base64.encodeToString(cipher.iv, Base64.DEFAULT) + "?" + Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    /**
     * Decrypt Base64 encoded encrypted string
     *
     * @param encrypted
     * *
     * @return
     */
    fun decrypt(iv:String, encrypted: String): String {
        val enc = Base64.decode(encrypted.toByteArray(), Base64.DEFAULT)
        val iv = Base64.decode(iv.toByteArray(), Base64.DEFAULT)
        val keySpec = SecretKeySpec(rawKey, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
        val decrypted = cipher.doFinal(enc)
        return String(decrypted)
    }

    private fun initKey(): ByteArray {
        val keygen = KeyGenerator.getInstance("AES")
        val random = SecureRandom.getInstance("SHA1PRNG")
        keygen.init(256, random)
        val key = keygen.generateKey()
        val raw = key.encoded
        return raw
    }
}