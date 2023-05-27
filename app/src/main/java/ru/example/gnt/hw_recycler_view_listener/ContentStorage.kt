package ru.example.gnt.hw_recycler_view_listener

object ContentStorage {
    var horizontalItems = listOf<ContentItem>()
        private set
    var verticalItems = listOf<ContentItem>()
        private set

    init {
        generateRandomItems()
    }

    private fun generateRandomItems() {
        val listFirst = mutableListOf<ContentItem>()
        val listSecond = mutableListOf<ContentItem>()

        for (i in (0..(10..30).random())) {
            listFirst.add(ContentItem(i))
            listSecond.add(ContentItem(i))
        }
        horizontalItems = listFirst
        verticalItems = listSecond
    }

}
