package com.ivarvisser.cineapp.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

/**
 * Initializes the Koin dependency injection framework with an optional app-specific configuration.
 *
 * @param config An optional lambda that allows customization of the Koin application during initialization.
 *               This parameter can include additional Koin modules, logger setup, or Android context configuration.
 *               If null, Koin will be initialized with the default shared module.
 * @return A [KoinApplication] instance, which represents the initialized Koin application and allows further configuration or lookup.
 */
fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        includes(config)
        modules(
            sharedModule
        )
    }
}

