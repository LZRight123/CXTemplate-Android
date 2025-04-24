package com.fantasy.components.tools

import java.io.File

inline fun <reified T> File.readObject(): T? = fromJson(readText())
