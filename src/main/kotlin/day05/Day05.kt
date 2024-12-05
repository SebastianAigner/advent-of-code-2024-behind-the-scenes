package day05

import java.io.File

val input = File("input/day05.txt")

data class OrderingRule(val before: Int, val after: Int)

data class Update(val numbers: List<Int>) {
    fun isValid(rules: List<OrderingRule>): Boolean {
        for(rule in rules) {
            if(!isValid(rule)) {
                return false
            }
        }
        return true
    }

    fun isValid(rule: OrderingRule): Boolean {
        // e.g. 7 before 10
        val (before, after) = rule
        val beforeLoc = numbers.indexOf(before)
        val afterLoc = numbers.indexOf(after)
        if(beforeLoc == -1 || afterLoc == -1) {
            // the rule doesn't apply, we're ok
            return true
        }
        if(beforeLoc < afterLoc) {
            return true
        } else {
            return false
        }
    }

    fun producePartiallyBetterUpdate(rules: List<OrderingRule>): Update {
        for(rule in rules) {
            if(isValid(rule)) {
                continue
            }
            // we have found an offending rule
            println("$rule is false in $this")
            val (before, after) = rule
            val newOrder = this.numbers.toMutableList()
            val befIdx = newOrder.indexOf(before)
            val aftIdx = newOrder.indexOf(after)
            newOrder[befIdx] = after
            newOrder[aftIdx] = before
            val new = Update(newOrder)
            check(new.isValid(rule)) // we have fixed *one* rule, this is now a partially better update.
            return new
        }
        // all rules are valid, we're ok.
        return this
    }

    fun produceTotallyBetterUpdate(rules: List<OrderingRule>): Update {
        var curr = this
        do {
            curr = curr.producePartiallyBetterUpdate(rules)
        } while(!curr.isValid(rules))
        return curr
    }

    fun tryToBeSneaky(rules: List<OrderingRule>): Update {
        var mut = numbers.toMutableList()
        while(true) {
            mut.shuffle()
            if(Update(mut).isValid(rules)) {
                return Update(mut)
            }
        }
    }

    fun middleNum(): Int {
        // 5 elems -> last index is 4 -> middle elem is at index 2
        return numbers[numbers.lastIndex / 2]
    }
}

fun main() {
    val lines = input.readLines()
    val orderingRules = lines.takeWhile { string -> string.isNotBlank() }.map {
        val (before, after) = it.split("|")
        OrderingRule(before.toInt(), after.toInt())
    }
    val updates = lines.takeLastWhile { string -> string.isNotBlank() }.map { string ->
        Update(string.split(",").map { string ->
            string.toInt()
        })
    }
    var sum = 0
    for(update in updates) {
        if(update.isValid(orderingRules)) {
            sum += update.middleNum()
        }
    }

    val invalidUpdates = buildList {
        for(update in updates) {
            if(!update.isValid(orderingRules)) {
                add(update)
            }
        }
    }
    println("working on ${invalidUpdates.count()} invalid updates")

    var fastinvalidSum = 0
    for((index, update) in invalidUpdates.withIndex()) {
        println("working on $index")
        val fixed = update.produceTotallyBetterUpdate(orderingRules)
        fastinvalidSum += fixed.middleNum()
    }
    println(fastinvalidSum)
}