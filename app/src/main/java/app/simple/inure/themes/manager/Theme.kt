package app.simple.inure.themes.manager

import android.graphics.Color
import app.simple.inure.themes.data.IconTheme
import app.simple.inure.themes.data.SwitchViewTheme
import app.simple.inure.themes.data.TextViewTheme
import app.simple.inure.themes.data.ViewGroupTheme

enum class Theme(val textViewTheme: TextViewTheme,
                 val viewGroupTheme: ViewGroupTheme,
                 val switchViewTheme: SwitchViewTheme,
                 val iconTheme: IconTheme) {

    LIGHT(
            textViewTheme = TextViewTheme(
                    headingTextColor = Color.parseColor("#121212"),
                    primaryTextColor = Color.parseColor("#2B2B2B"),
                    secondaryTextColor = Color.parseColor("#5A5A5A"),
                    tertiaryTextColor = Color.parseColor("#7A7A7A"),
                    quaternaryTextColor = Color.parseColor("#9A9A9A"),
            ),
            viewGroupTheme = ViewGroupTheme(
                    background = Color.parseColor("#ffffff")
            ),
            switchViewTheme = SwitchViewTheme(
                    switchOffColor = Color.parseColor("#F4F4F4")
            ),
            iconTheme = IconTheme(
                    regularIconColor = Color.parseColor("#2E2E2E"),
                    secondaryIconColor = Color.parseColor("#B1B1B1")
            )
    ),

    DARK(
            textViewTheme = TextViewTheme(
                    headingTextColor = Color.parseColor("#F1F1F1"),
                    primaryTextColor = Color.parseColor("#E4E4E4"),
                    secondaryTextColor = Color.parseColor("#C8C8C8"),
                    tertiaryTextColor = Color.parseColor("#AAAAAA"),
                    quaternaryTextColor = Color.parseColor("#9A9A9A"),
            ),
            viewGroupTheme = ViewGroupTheme(
                    background = Color.parseColor("#171717")
            ),
            switchViewTheme = SwitchViewTheme(
                    switchOffColor = Color.parseColor("#252525")
            ),
            iconTheme = IconTheme(
                    regularIconColor = Color.parseColor("#F8F8F8"),
                    secondaryIconColor = Color.parseColor("#E8E8E8")
            )
    )
}