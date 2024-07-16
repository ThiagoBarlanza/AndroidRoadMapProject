package pt.drunkdroidos.cat_alog.model

import kotlin.random.Random

class Sequence {

    val sequence = ArrayList<Int>()

    fun appendNewStep() {
        sequence.add(Random.nextInt() % 4);
    }

    fun currentSequence(): Array<Int> {
        return sequence.toArray() as Array<Int>
    }
}