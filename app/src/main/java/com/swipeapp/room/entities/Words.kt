package com.swipeapp.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Words(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false) val id: Int?,
    @ColumnInfo(name = "word", index = true) var word: String,
    @ColumnInfo(name = "language_module_id") val languageModuleId: Int?,
    @ColumnInfo(name = "pronunciation") val pronunciation: String? = null,
    @ColumnInfo(name = "file_path") val filePath : String = "",
    @ColumnInfo(name = "is_favourite") var isFavourite:Boolean?,
    @ColumnInfo(name = "is_known") var isKnown:Boolean?,
    @ColumnInfo(name = "created_by") val createdBy:String? = "admin",
    @ColumnInfo(name = "word_translation") val translateWord:String?,
    @ColumnInfo(name = "is_infinitive") var isInfinitive:Boolean = false,
    @ColumnInfo(name = "is_menu_shown") var isMenuShown:Boolean = false,
    @ColumnInfo(name = "is_open_view_shown") var isOpenViewShown:Boolean = false,
    @ColumnInfo(name = "is_full_open_view_shown") var isFullOpenViewShown:Boolean = false,
    @ColumnInfo(name = "is_word_selected") var isWordSelected:Boolean? = false,
    @ColumnInfo(name = "created_at") var createAt:String?,
    @ColumnInfo(name = "updated_at") var updatedAt:String?,
    @ColumnInfo(name = "updated_at_favorite") var updatedAtFavorite:String?,
    @ColumnInfo(name = "updated_at_know_it") var updatedAtKnowIt:String?,
)