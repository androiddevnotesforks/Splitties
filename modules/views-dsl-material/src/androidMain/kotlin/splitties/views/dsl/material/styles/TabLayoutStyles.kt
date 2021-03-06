/*
 * Copyright 2019-2020 Louis Cognault Ayeva Derman. Use of this source code is governed by the Apache 2.0 license.
 */

package splitties.views.dsl.material.styles

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.google.android.material.tabs.TabLayout
import splitties.views.dsl.core.NO_THEME
import splitties.views.dsl.core.styles.styledView
import splitties.views.dsl.material.R
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@JvmInline
value class TabLayoutStyles @PublishedApi internal constructor(
    @PublishedApi internal val ctx: Context
) {
    inline fun default(
        @IdRes id: Int = View.NO_ID,
        @StyleRes theme: Int = NO_THEME,
        initView: TabLayout.() -> Unit = {}
    ): TabLayout {
        contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
        return ctx.styledView(
            newViewRef = ::TabLayout,
            styleAttr = R.attr.Widget_MaterialComponents_TabLayout,
            id = id,
            theme = theme,
            initView = initView
        )
    }

    inline fun colored(
        @IdRes id: Int = View.NO_ID,
        @StyleRes theme: Int = NO_THEME,
        initView: TabLayout.() -> Unit = {}
    ): TabLayout {
        contract { callsInPlace(initView, InvocationKind.EXACTLY_ONCE) }
        return ctx.styledView(
            newViewRef = ::TabLayout,
            styleAttr = R.attr.Widget_MaterialComponents_TabLayout_Colored,
            id = id,
            theme = theme,
            initView = initView
        )
    }
}
