package crazyboyfeng.justTvLauncher.model

class ShortcutGroup(val category: String, val shortcutList: MutableList<Shortcut>) {
    var openCount = shortcutList.sumOf { it.openCount }
    fun add(element: Shortcut) {
        openCount += element.openCount
//        Log.d(TAG,"openCount[${element.id}]=${element.openCount}")
        val index = findIndex(element.openCount, 0, shortcutList.size - 1)
//        Log.d(TAG,"list[$index]=${element.openCount}")
        shortcutList.add(index, element)
    }

    private tailrec fun findIndex(oc: Int, leftIndex: Int, rightIndex: Int): Int {
        return when {
            oc > shortcutList[leftIndex].openCount -> {
//                Log.d(TAG,"$oc>list[left=$leftIndex]=${shortcutList[leftIndex].openCount}")
                leftIndex
            }
            shortcutList[rightIndex].openCount > oc -> {
//                Log.d(TAG,"list[right=$rightIndex]=${shortcutList[rightIndex].openCount}>$oc")
                rightIndex + 1
            }
            else -> {
                val middleIndex = (leftIndex + rightIndex) / 2
                when {
                    shortcutList[middleIndex].openCount > oc -> {
//                        Log.d(TAG,"list[middle=$middleIndex]>$oc")
                        findIndex(oc, leftIndex, middleIndex)
                    }
                    shortcutList[middleIndex].openCount < oc -> {
//                        Log.d(TAG,"$oc>list[middle=$middleIndex]")
                        findIndex(oc, middleIndex + 1, rightIndex)
                    }
                    else -> {
                        middleIndex
                    }
                }
            }
        }
    }
//    companion object {
//        private const val TAG = "ShortcutGroup"
//    }
}
