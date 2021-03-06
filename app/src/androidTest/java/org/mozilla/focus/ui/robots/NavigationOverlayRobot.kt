/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.focus.ui.robots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.recyclerview.widget.RecyclerView
import org.mozilla.focus.R
import org.mozilla.focus.helpers.hasItemCount
import org.mozilla.focus.helpers.isDisplayed

/**
 * Implementation of Robot Pattern for the navigation overlay.
 */
class NavigationOverlayRobot private constructor() {

    fun assertHomeTileCount(count: Int) {
        homeTiles().check(matches(hasItemCount(count)))
    }

    private fun assertIsDisplayed(isDisplayed: Boolean = true) {
        homeTiles().check(matches(isDisplayed(isDisplayed)))
    }

    inner class Transition {

        /**
         * Long presses the tile at the given index and returns the same screen: the Google search tile cannot be
         * removed and long presses on that tile can be represented by this call.
         */
        fun longPressTileIsNoOp(index: Int, interact: NavigationOverlayRobot.() -> Unit): NavigationOverlayRobot.Transition {
            longPressTile(index)
            assertIsDisplayed()
            return NavigationOverlayRobot.interactAndTransition(interact)
        }

        fun longPressTileToUnpinOverlay(index: Int, interact: UnpinOverlayRobot.() -> Unit): UnpinOverlayRobot.Transition {
            longPressTile(index)
            assertIsDisplayed()
            return UnpinOverlayRobot.interactAndTransition(interact)
        }

        private fun longPressTile(index: Int) {
            homeTiles().perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(index, ViewActions.longClick()))
        }
    }

    companion object {

        fun interactAndTransition(interact: NavigationOverlayRobot.() -> Unit): NavigationOverlayRobot.Transition {
            NavigationOverlayRobot().run {
                assertIsDisplayed()
                interact()
            }
            return NavigationOverlayRobot().Transition()
        }
    }
}

private fun homeTiles() = onView(withId(R.id.homeTiles))

/**
 * Applies [interact] to a new [NavigationOverlayRobot]
 *
 * @sample org.mozilla.focus.ui.UnpinTilesTest.unpinTilesTest
 */
fun navigationOverlay(interact: NavigationOverlayRobot.() -> Unit): NavigationOverlayRobot.Transition {
    return NavigationOverlayRobot.interactAndTransition(interact)
}
