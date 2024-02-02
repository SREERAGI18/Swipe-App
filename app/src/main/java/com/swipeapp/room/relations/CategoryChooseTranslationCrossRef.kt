package com.swipeapp.room.relations

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "category_choose_translation_cross_refs", primaryKeys = ["category_id", "choose_translation_word_id"])
data class CategoryChooseTranslationCrossRef(
    @ColumnInfo(name = "category_id") val category_id:Int,
    @ColumnInfo(name = "choose_translation_word_id", index = true) val exercise_choose_translation_word_id:Int
)
