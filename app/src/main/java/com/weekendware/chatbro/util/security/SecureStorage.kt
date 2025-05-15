package com.weekendware.chatbro.util.security

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import androidx.core.content.edit

object SecureStorage {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "ChatBroAPIKeyAlias"
    private const val PREF_NAME = "secure_prefs"
    private const val PREF_ENCRYPTED_KEY = "encrypted_api_key"
    private const val PREF_IV = "encryption_iv"

    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        return keyStore.getKey(KEY_ALIAS, null) as? SecretKey
            ?: generateKey()
    }

    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES", ANDROID_KEYSTORE)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    fun encryptAndSave(context: Context, plainText: String) {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(PREF_ENCRYPTED_KEY, Base64.encodeToString(encrypted, Base64.DEFAULT))
                .putString(PREF_IV, Base64.encodeToString(iv, Base64.DEFAULT))
        }
    }

    fun getDecryptedApiKey(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val encryptedBase64 = prefs.getString(PREF_ENCRYPTED_KEY, null)
        val ivBase64 = prefs.getString(PREF_IV, null)

        if (encryptedBase64 == null || ivBase64 == null) return null

        val encrypted = Base64.decode(encryptedBase64, Base64.DEFAULT)
        val iv = Base64.decode(ivBase64, Base64.DEFAULT)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)

        val decryptedBytes = cipher.doFinal(encrypted)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}
