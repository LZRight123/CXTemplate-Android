package com.fantasy.cctemplate.model

import androidx.annotation.Keep

@Keep
data class APPConfig(
    val home_page_title : String = "", // home_page_title,
    val oss_endpoint : String = "", // endpoint,
    val oss_accessKeyId : String = "", // token['Credentials']['AccessKeyId'],
    val oss_accessKeySecret : String = "", // token['Credentials']['AccessKeySecret'],
    val oss_securityToken : String = "", // token['Credentials']['SecurityToken'],
    val oss_expiration : String = "", // token['Credentials']['Expiration']
)