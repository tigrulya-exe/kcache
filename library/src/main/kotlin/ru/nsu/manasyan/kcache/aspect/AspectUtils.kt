package ru.nsu.manasyan.kcache.aspect

import java.lang.reflect.Method

inline fun <reified T> getAnnotationInstance(method: Method): T = method.annotations.filterIsInstance<T>()[0]
