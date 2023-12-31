package net.weavemc.intermediary

internal fun String.splitAround(c: Char): Pair<String,String> =
    substringBefore(c) to substringAfter(c, "")