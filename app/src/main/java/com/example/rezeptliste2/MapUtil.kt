package com.example.rezeptliste2

import com.example.rezeptliste2.database.dto.Ingredient

class MapUtil(private var map: Map<Ingredient, String>) {

    fun getKeys(): Set<Ingredient> {
        return map.keys
    }

    fun getValues(): Collection<String> {
        return map.values
    }

    fun getValue(key: Ingredient): String? {
        return map[key]
    }

    fun remove(key: Ingredient) {
        map = map.toMutableMap().apply { remove(key) }
    }

    fun put(key: Ingredient, value: String): MapUtil {
        map = map.toMutableMap().apply { put(key, value) }

        return this
    }

    fun replaceKey(oldKey: Ingredient, newKey: Ingredient): MapUtil {
        val oldMapUpperPart = MapUtil(emptyMap())
        val oldMapLowerPart = MapUtil(emptyMap())

        var isUpperPart: Boolean = true

        val newMap: MapUtil = MapUtil(emptyMap())

        map.keys.forEach {
            if (it == oldKey) {
                isUpperPart = false
                return@forEach
            }

            if (isUpperPart) {
                oldMapUpperPart.put(it, map[it]!!)
            } else {
                oldMapLowerPart.put(it, map[it]!!)
            }
        }

        oldMapUpperPart.getKeys().forEach {
            newMap.put(it, oldMapUpperPart.getValue(it)!!)
        }

        newMap.put(newKey, map[oldKey]!!)

        oldMapLowerPart.getKeys().forEach {
            newMap.put(it, oldMapLowerPart.getValue(it)!!)
        }

        map.toMutableMap().clear()

        newMap.getKeys().forEach {
            map.toMutableMap()[it] = newMap.getValue(it)!!
        }

        return newMap
    }

    fun setValue(ingredient: Ingredient?, it: String) {
        map = map.toMutableMap().apply { put(ingredient!!, it) }

    }

    fun getLastKey(): Ingredient {
        return map.keys.last()
    }
}