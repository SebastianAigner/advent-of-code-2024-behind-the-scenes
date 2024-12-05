package day05

import java.io.File
import kotlin.time.measureTimedValue

val input = File("input/day05.txt")

data class OrderingRule(val before: Int, val after: Int)

data class Update(val numbers: List<Int>) {
    fun isValid(rules: List<OrderingRule>): Boolean {
        for(rule in rules) {
            if(!isSingleRuleValid(rule)) {
                return false
            }
        }
        return true
    }

    fun isSingleRuleValid(rule: OrderingRule): Boolean {
        val beforeLoc = numbers.indexOf(rule.before)
        val afterLoc = numbers.indexOf(rule.after)
        if(beforeLoc == -1 || afterLoc == -1) {
            // the rule doesn't apply, we're ok
            return true
        }
        return beforeLoc < afterLoc
    }

    fun producePartiallyBetterUpdate(rules: List<OrderingRule>): Update {
        for(rule in rules) {
            if(isSingleRuleValid(rule)) {
                continue
            }
            // we have found an offending rule
            val newOrder = this.numbers.toMutableList()
            val befIdx = newOrder.indexOf(rule.before)
            val aftIdx = newOrder.indexOf(rule.after)
            newOrder[befIdx] = rule.after
            newOrder[aftIdx] = rule.before
            val new = Update(newOrder)
            check(new.isSingleRuleValid(rule)) // we have fixed *one* rule, this is now a partially better update.
            check(new.isSingleRuleValid(rule))

            return new // possible optimization: apply other rules as well.
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

    val res = part1(updates, orderingRules)
    val res2 = part2(updates, orderingRules)

    println(res)
    println(res2)
}

private fun part1(
    updates: List<Update>,
    orderingRules: List<OrderingRule>,
): Int {
    var sum = 0
    for (update in updates) {
        if (update.isValid(orderingRules)) {
            sum += update.middleNum()
        }
    }
    return sum
}

private fun part2(
    updates: List<Update>,
    orderingRules: List<OrderingRule>,
): Int {
    val invalidUpdates = buildList {
        for(update in updates) {
            if(!update.isValid(orderingRules)) {
                add(update)
            }
        }
    }
    var fastinvalidSum = 0
    for (update in invalidUpdates) {
        val fixed = update.produceTotallyBetterUpdate(orderingRules)
        fastinvalidSum += fixed.middleNum()
    }
    return fastinvalidSum
}