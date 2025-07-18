package com.example.hero_finder

import java.security.MessageDigest
import java.math.BigInteger

fun String.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    val messageDigest = md.digest(this.toByteArray())
    val number = BigInteger(1, messageDigest)
    var hashtext = number.toString(16)

    while (hashtext.length < 32){
        hashtext = "0$hashtext"
    }
    return hashtext
}