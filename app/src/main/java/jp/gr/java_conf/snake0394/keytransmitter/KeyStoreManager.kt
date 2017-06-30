package jp.gr.java_conf.snake0394.keytransmitter

import android.content.Context
import android.security.KeyPairGeneratorSpec
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal

/** Android Keystore Sample for API Level 18  */
class KeyStoreManager private constructor(private var mContext: Context?) {
    private val mKeyStore: KeyStore
    private val KEY_STORE_ALIAS = "poipoi"
    private val KEY_STORE_ALGORITHM = "RSA"
    private val KEY_PROVIDER = "AndroidKeyStore"
    private val CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"

    init {
        mKeyStore = KeyStore.getInstance(KEY_PROVIDER)
        mKeyStore.load(null)
    }

    val publicKey: PublicKey
        get() {
            if (mKeyStore.containsAlias(KEY_STORE_ALIAS)) {
                return mKeyStore.getCertificate(KEY_STORE_ALIAS).publicKey
            } else {
                return createKeyPair()!!.public
            }
        }

    val privateKey: PrivateKey
        get() {
            if (mKeyStore.containsAlias(KEY_STORE_ALIAS)) {
                return mKeyStore.getKey(KEY_STORE_ALIAS, null) as PrivateKey
            } else {
                return createKeyPair()!!.private
            }
        }

    private fun createKeyPair(): KeyPair? {
        val kpg = KeyPairGenerator.getInstance(KEY_STORE_ALGORITHM, KEY_PROVIDER)
        kpg.initialize(createKeyPairGeneratorSpec())
        return kpg.generateKeyPair()
    }

    private fun createKeyPairGeneratorSpec(): KeyPairGeneratorSpec {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 100)

        val spec = KeyPairGeneratorSpec.Builder(mContext!!)
                .setAlias(KEY_STORE_ALIAS)
                .setSubject(X500Principal(String.format("CN=%s", KEY_STORE_ALIAS)))
                .setSerialNumber(BigInteger.valueOf(100000))
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build()

        return spec
    }

    fun encrypt(bytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return cipher.doFinal(bytes)
    }

    fun decrypt(bytes: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        return cipher.doFinal(bytes)
    }

    companion object {
        private var instance: KeyStoreManager? = null

        fun getInstance(context: Context): KeyStoreManager {
            if (instance == null) {
                instance = KeyStoreManager(context)
            } else {
                instance!!.mContext = context
            }
            return instance as KeyStoreManager
        }
    }

}
