package com.github.crazyboyfeng.justTvLauncher.model

class ShortcutGroup(val category: String?, val shortcutList: MutableList<Shortcut>) {
    var openCount = 0
    fun add(element: Shortcut) {
        val oc = element.openCount
        val index = findIndex(oc, 0, shortcutList.size - 1)
        shortcutList.add(index, element)
    }

    private fun findIndex(oc: Int, leftIndex: Int, rightIndex: Int): Int {
        return when {
            shortcutList[leftIndex].openCount < oc -> {
                leftIndex
            }
            shortcutList[rightIndex].openCount > oc -> {
                rightIndex
            }
            else -> {
                val middleIndex = (leftIndex + rightIndex) / 2
                when {
                    shortcutList[middleIndex].openCount > oc -> {
                        findIndex(oc, leftIndex, middleIndex)
                    }
                    shortcutList[middleIndex].openCount < oc -> {
                        findIndex(oc, middleIndex + 1, rightIndex)
                    }
                    else -> {
                        middleIndex
                    }
                }
            }
        }
    }
}
