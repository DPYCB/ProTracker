package com.dpycb.utils.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import javax.inject.Scope
import kotlin.reflect.KClass

@kotlin.annotation.Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Scope
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
annotation class ActivityScope

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)