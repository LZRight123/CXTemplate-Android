package com.fantasy.components.tools

import java.io.File

fun File.writeObject(obj: Any) = writeText(toJsonString(obj))
inline fun <reified T> File.readObject(): T? = fromJson(readText())
inline fun <reified T> File.readObjectList(): List<T>? = fromJson2List(readText())