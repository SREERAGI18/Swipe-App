package com.swipeapp.room.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

//data class CategoryWithChooseTranslation(
//    @Embedded val exerciseCategories: ExerciseCategories,
//    @Relation(
//        parentColumn = "category_id",
//        entityColumn = "choose_translation_word_id",
//        associateBy = Junction(CategoryChooseTranslationCrossRef::class)
//    )
//    val exerciseChooseTranslations: List<ExerciseChooseTranslationWords>
//)
