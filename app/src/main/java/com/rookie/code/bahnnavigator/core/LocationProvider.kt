package com.rookie.code.bahnnavigator.core

interface LocationProvider {
    suspend fun current(): Pair<Double, Double>?
}

class FakeLocationProvider : LocationProvider {
    override suspend fun current() = 52.5 to 13.4   // Berlin
}