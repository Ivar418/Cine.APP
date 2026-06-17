package com.ivarvisser.cineapp.di

import org.koin.dsl.module

val sharedModule = module {
    includes(dataModule, serviceModule)
}