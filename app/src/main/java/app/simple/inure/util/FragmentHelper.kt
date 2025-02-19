package app.simple.inure.util

import android.view.View
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.transition.Fade
import app.simple.inure.R
import app.simple.inure.decorations.transitions.DetailsTransition
import app.simple.inure.decorations.transitions.DetailsTransitionArc
import app.simple.inure.extension.fragments.ScopedFragment
import org.jetbrains.annotations.NotNull

object FragmentHelper {
    /**
     * Open fragment using arc animation for shared element
     *
     * @param fragmentManager [FragmentManager]
     * @param fragment [Fragment]
     * @param icon [View] that needs to be animated
     * @param tag back stack tag for fragment
     */
    fun openFragment(fragmentManager: FragmentManager, fragment: ScopedFragment, icon: View, tag: String) {
        fragment.setTransitions()

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(icon, icon.transitionName)
                .replace(R.id.app_container, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    /**
     * Open fragment using linear animation for shared element
     *
     * @param fragmentManager [FragmentManager]
     * @param fragment [Fragment]
     * @param icon [View] that needs to be animated
     * @param tag back stack tag for fragment
     */
    fun openFragmentLinear(fragmentManager: FragmentManager, fragment: Fragment, icon: View, tag: String) {
        fragment.exitTransition = Fade()
        fragment.sharedElementEnterTransition = DetailsTransition()
        fragment.enterTransition = Fade()
        fragment.sharedElementReturnTransition = DetailsTransition()

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(icon, icon.transitionName)
                .replace(R.id.app_container, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    /**
     * Open fragment using slide animation
     *
     * @param fragmentManager [FragmentManager]
     * @param fragment [Fragment]
     * @param tag back stack tag for fragment
     */
    fun openFragment(fragmentManager: FragmentManager, fragment: Fragment, @Nullable tag: String) {
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.app_container, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    /**
     * Open fragment using arc animation for shared element.
     * Used for fragment that don't need to be saved in back stack
     *
     * for fragment that needs to be saved in back stack @see [FragmentHelper.openFragment]
     *
     * @param fragmentManager [FragmentManager]
     * @param fragment [Fragment]
     * @param icon [View] that needs to be animated
     */
    fun openFragment(fragmentManager: FragmentManager, fragment: Fragment, @NotNull icon: ImageView) {
        fragment.exitTransition = Fade()
        fragment.sharedElementEnterTransition = DetailsTransitionArc()
        fragment.enterTransition = Fade()
        fragment.sharedElementReturnTransition = DetailsTransitionArc()

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(icon, icon.transitionName)
                .replace(R.id.app_container, fragment, null)
                .commit()
    }
}
