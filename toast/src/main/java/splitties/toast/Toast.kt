/*
 * Copyright (c) 2018. Louis Cognault Ayeva Derman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("NOTHING_TO_INLINE")

package splitties.toast

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.content.ContextWrapper
import android.os.Build.VERSION.SDK_INT
import android.support.annotation.StringRes
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import splitties.init.appCtx
import splitties.resources.txt
import splitties.systemservices.layoutInflater
import splitties.systemservices.windowManager
import kotlin.LazyThreadSafetyMode.NONE
import android.support.v4.app.Fragment as SupportFragment

@PublishedApi internal fun Context.createToast(text: CharSequence, duration: Int): Toast {
    val ctx = if (SDK_INT == 25) SafeToastCtx(this) else this
    return Toast.makeText(ctx, text, duration)
}

@PublishedApi internal fun Context.createToast(@StringRes resId: Int, duration: Int): Toast {
    return createToast(txt(resId), duration)
}

inline fun Context.toast(@StringRes msgResId: Int) = createToast(msgResId, Toast.LENGTH_SHORT)
inline fun SupportFragment.toast(@StringRes msgResId: Int) = ctx.createToast(msgResId, Toast.LENGTH_SHORT)
inline fun Fragment.toast(@StringRes msgResId: Int) = ctx.createToast(msgResId, Toast.LENGTH_SHORT)
inline fun View.toast(@StringRes msgResId: Int) = context.createToast(msgResId, Toast.LENGTH_SHORT)
inline fun toast(@StringRes msgResId: Int) = appCtx.createToast(msgResId, Toast.LENGTH_SHORT)

inline fun Context.longToast(@StringRes msgResId: Int) = createToast(msgResId, Toast.LENGTH_LONG)
inline fun SupportFragment.longToast(@StringRes msgResId: Int) = ctx.createToast(msgResId, Toast.LENGTH_LONG)
inline fun Fragment.longToast(@StringRes msgResId: Int) = ctx.createToast(msgResId, Toast.LENGTH_LONG)
inline fun View.longToast(@StringRes msgResId: Int) = context.createToast(msgResId, Toast.LENGTH_LONG)
inline fun longToast(@StringRes msgResId: Int) = appCtx.createToast(msgResId, Toast.LENGTH_LONG)

@PublishedApi internal inline val SupportFragment.ctx: Context get() = context ?: appCtx
@PublishedApi internal inline val Fragment.ctx: Context get() = activity ?: appCtx

/**
 * Avoids [WindowManager.BadTokenException] on API 25.
 */
private class SafeToastCtx(ctx: Context) : ContextWrapper(ctx) {

    private val toastWindowManager by lazy(NONE) { ToastWindowManager(baseContext.windowManager) }
    private val toastLayoutInflater by lazy(NONE) {
        baseContext.layoutInflater.cloneInContext(this)
    }

    override fun getApplicationContext(): Context = SafeToastCtx(baseContext.applicationContext)
    override fun getSystemService(name: String?): Any = when (name) {
        Context.LAYOUT_INFLATER_SERVICE -> toastLayoutInflater
        Context.WINDOW_SERVICE -> toastWindowManager
        else -> super.getSystemService(name)
    }

    private class ToastWindowManager(private val base: WindowManager) : WindowManager by base {

        @SuppressLint("LogNotTimber") // Timber is not a dependency here, but lint passes through.
        override fun addView(view: View?, params: ViewGroup.LayoutParams?) {
            try {
                base.addView(view, params)
            } catch (e: WindowManager.BadTokenException) {
                Log.e("SafeToast", "Couldn't add Toast to WindowManager", e)
            }
        }
    }
}
