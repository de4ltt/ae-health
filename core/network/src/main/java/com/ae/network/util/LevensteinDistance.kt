package com.ae.network.util

internal fun String.isFuzzyMatch(other: String): Boolean {
    val lowerA = this.lowercase()
    val lowerB = other.lowercase()

    fun levenshtein(a: String, b: String): Int {
        val m = a.length
        val n = b.length

        val dp = Array(m + 1) { IntArray(n + 1) }

        for (i in 0..m) dp[i][0] = i
        for (j in 0..n) dp[0][j] = j

        for (i in 1..m) {
            for (j in 1..n) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }

        return dp[m][n]
    }

    if (lowerA.contains(lowerB) || lowerB.contains(lowerA)) return true

    val len = maxOf(lowerA.length, lowerB.length)
    if (len == 0) return true // both strings are empty

    val distance = levenshtein(lowerA, lowerB)
    val similarity = 1.0 - distance.toDouble() / len

    return similarity >= 0.85
}