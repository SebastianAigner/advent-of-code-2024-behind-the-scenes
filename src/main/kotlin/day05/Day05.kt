package day05

import java.io.File
import kotlin.time.measureTimedValue

val input = File("input/day05.txt")

data class OrderingRule(val before: Int, val after: Int)

data class Update(val numbers: List<Int>) {
    fun isValid(rules: List<OrderingRule>): Boolean {
        return rules.all { rule ->
            isSingleRuleValid(rule)
        }
    }

    fun isSingleRuleValid(rule: OrderingRule): Boolean {
        val beforeLoc = numbers.indexOf(rule.before)
        val afterLoc = numbers.indexOf(rule.after)
        if (beforeLoc == -1 || afterLoc == -1) {
            // the rule doesn't apply, we're ok
            return true
        }
        return beforeLoc < afterLoc
    }

    fun producePartiallyBetterUpdate(rules: List<OrderingRule>): Update {
        for (rule in rules) {
            if (isSingleRuleValid(rule)) {
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

            return new // possible optimization: apply other rules as well.
        }
        // all rules are valid, we're ok.
        return this
    }

    fun produceTotallyBetterUpdate(rules: List<OrderingRule>): Update {
        var curr = this
        do {
            curr = curr.producePartiallyBetterUpdate(rules)
        } while (!curr.isValid(rules))
        return curr
    }

    fun tryToBeSneaky(rules: List<OrderingRule>): Update {
        var mut = numbers.toMutableList()
        while (true) {
            mut.shuffle()
            if (Update(mut).isValid(rules)) {
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
    val orderingRules = lines
        .takeWhile { line -> line.isNotBlank() }
        .map {
            val (before, after) = it.split("|")
            OrderingRule(before.toInt(), after.toInt())
        }
    val updates = lines
        .takeLastWhile { line -> line.isNotBlank() }
        .map { line ->
            Update(line.split(",").map { it.toInt() })
        }

    val res = measureTimedValue { part1(updates, orderingRules) }
    val res2 = measureTimedValue { part2(updates, orderingRules) }

    println(res)
    println(res2)
}

private fun part1(
    updates: List<Update>,
    orderingRules: List<OrderingRule>,
): Int {
    return updates.sumOf {
        if (it.isValid(orderingRules)) {
            it.middleNum()
        } else {
            0
        }
    }
}

private fun part2(
    updates: List<Update>,
    orderingRules: List<OrderingRule>,
): Int {
    val invalidUpdates = updates.filterNot { update ->
        update.isValid(orderingRules)
    }
    return invalidUpdates.sumOf {
        it
            .produceTotallyBetterUpdate(orderingRules)
            .middleNum()
    }
}