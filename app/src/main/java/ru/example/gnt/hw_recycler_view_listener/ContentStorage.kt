package ru.example.gnt.hw_recycler_view_listener

object ContentStorage {
    /*
    2 списка с одинаковыми элементами, чтобы в списке были разные экземпляры.
    можно было в view.tag задавать копию. Пример:  view.tag = view.tag.copy()
    но это почему-то не создавало новый экземпляр.
    В итоге получалось так, что при обновлении процентов в вертикальной RecyclerView
    обновлялся список и в горизонтальной.
     */
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
