package com.swipeapp.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swipeapp.room.entities.Words
import java.time.OffsetDateTime

@Dao
interface WordsDao {

    @Query("UPDATE words SET is_word_selected=:isSelected WHERE id=:wordId")
    suspend fun selectWord(wordId:Int, isSelected:Boolean=true):Int

    @Query("UPDATE words SET is_word_selected=:isSelected WHERE id=:wordId")
    suspend fun unSelectWord(wordId:Int, isSelected:Boolean=false): Int

    @Query("SELECT COUNT(id) FROM words WHERE is_favourite = :isFav AND language_module_id=:moduleId")
    suspend fun getFavWordsCount(isFav: Boolean = true, moduleId: Int):Int

    @Query("SELECT COUNT(id) FROM words WHERE is_known = :isKnown AND language_module_id=:moduleId")
    suspend fun getKnownWordsCount(isKnown: Boolean = true, moduleId: Int):Int

    @Query("SELECT COUNT(id) FROM words")
    suspend fun getAllWordsCount():Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Words)

    @Query("UPDATE words SET file_path = :filePath WHERE id=:wordId")
    suspend fun updateFilePath(filePath:String, wordId: Int?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWords(wordList:List<Words>):List<Long>

    @Query("UPDATE words SET is_favourite = :isFav,updated_at_favorite = :updateTimeFav  WHERE word = :word")
    suspend fun addWordToFav(word: String,updateTimeFav:String, isFav: Boolean = true):Int

    @Query("UPDATE words SET is_favourite = :isFav,updated_at_favorite = :updateTimeFav WHERE word = :word")
    suspend fun removeWordFromFav(word: String,updateTimeFav:String, isFav: Boolean = false):Int

    @Query("UPDATE words SET is_known = :isKnown,updated_at_know_it = :updateTimeKnowIt WHERE word = :word")
    suspend fun addWordToKnown(word: String,updateTimeKnowIt:String, isKnown: Boolean = true):Int

    @Query("UPDATE words SET is_known = :isKnown,updated_at_know_it = :updateTimeKnowIt WHERE word = :word")
    suspend fun removeWordFromKnown(word: String,updateTimeKnowIt:String, isKnown: Boolean = false):Int

    // Fav Words
    @Query("SELECT * FROM words WHERE is_favourite = :isFav AND language_module_id=:moduleId ORDER BY datetime(updated_at_favorite) DESC")
    suspend fun getFavWordsByLastCreated(isFav:Boolean = true, moduleId:Int): List<Words>

    @Query("SELECT * FROM words WHERE is_favourite = :isFav AND language_module_id=:moduleId")
    suspend fun getFavoriteWords(isFav:Boolean = true, moduleId: Int): List<Words>

    @Query("SELECT * FROM words WHERE is_favourite = :isFav AND language_module_id=:moduleId ORDER BY word ASC")
    suspend fun getFavWordsInAscOrder(isFav:Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_favourite = :isFav AND language_module_id=:moduleId ORDER BY word DESC")
    suspend fun getFavWordsInDescOrder(isFav:Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_favourite = :isFav AND language_module_id=:moduleId ORDER BY LENGTH(word) ASC")
    suspend fun getFavWordsByMinCharFirst(isFav:Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_favourite = :isFav AND language_module_id=:moduleId ORDER BY LENGTH(word) DESC")
    suspend fun getFavWordsByMaxCharFirst(isFav:Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_favourite = :isFav AND language_module_id=:moduleId AND word LIKE :searchQuery ORDER BY word ASC")
    fun searchFavWords(searchQuery:String, isFav:Boolean = true, moduleId: Int):LiveData<List<Words>>

    // Known Words
    @Query("SELECT * FROM words WHERE is_known = :isKnown AND language_module_id=:moduleId ORDER BY datetime(updated_at_favorite) DESC")
    suspend fun getKnownWordsByFav(isKnown: Boolean=true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND language_module_id=:moduleId ORDER BY datetime(updated_at_know_it) DESC")
    suspend fun getKnownWordsByLastCreated(isKnown: Boolean = true, moduleId: Int): List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND language_module_id=:moduleId ORDER BY word ASC")
    suspend fun getKnownWordsInAscOrder(isKnown:Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND language_module_id=:moduleId ORDER BY word DESC")
    suspend fun getKnownWordsInDescOrder(isKnown:Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND language_module_id=:moduleId ORDER BY LENGTH(word) ASC")
    suspend fun getKnownWordsByMinCharFirst(isKnown:Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND language_module_id=:moduleId ORDER BY LENGTH(word) DESC")
    suspend fun getKnownWordsByMaxCharFirst(isKnown:Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND language_module_id=:moduleId AND word LIKE :searchQuery ORDER BY word ASC")
    fun searchKnownWords(searchQuery:String, isKnown:Boolean = true, moduleId: Int):LiveData<List<Words>>

    // Fav Learning Words
    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId ORDER BY datetime(updated_at_favorite) DESC")
    suspend fun getFavLearningWordsByLastCreated(isFav: Boolean = true, isKnown: Boolean = false, moduleId: Int): List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId ORDER BY word ASC")
    suspend fun getFavLearningWordsInAscOrder(isKnown:Boolean = false, isFav: Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId ORDER BY word DESC")
    suspend fun getFavLearningWordsInDescOrder(isKnown:Boolean = false, isFav: Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId ORDER BY LENGTH(word) ASC")
    suspend fun getFavLearningWordsByMinCharFirst(isKnown:Boolean = false, isFav: Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId ORDER BY LENGTH(word) DESC")
    suspend fun getFavLearningWordsByMaxCharFirst(isKnown:Boolean = false, isFav: Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId AND word LIKE :searchQuery ORDER BY word ASC")
    fun searchFavLearningWords(searchQuery:String, isKnown:Boolean = false, isFav: Boolean = true, moduleId: Int):LiveData<List<Words>>

    // Fav Known Words
    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId ORDER BY datetime(updated_at_know_it) DESC")
    suspend fun getFavKnownWordsByLastCreated(isFav: Boolean = true, isKnown: Boolean = true, moduleId: Int): List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId ORDER BY word ASC")
    suspend fun getFavKnownWordsInAscOrder(isKnown:Boolean = true, isFav: Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId ORDER BY word DESC")
    suspend fun getFavKnownWordsInDescOrder(isKnown:Boolean = true, isFav: Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId ORDER BY LENGTH(word) ASC")
    suspend fun getFavKnownWordsByMinCharFirst(isKnown:Boolean = true, isFav: Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId ORDER BY LENGTH(word) DESC")
    suspend fun getFavKnownWordsByMaxCharFirst(isKnown:Boolean = true, isFav: Boolean = true, moduleId: Int):List<Words>

    @Query("SELECT * FROM words WHERE is_known = :isKnown AND is_favourite = :isFav AND language_module_id=:moduleId AND word LIKE :searchQuery ORDER BY word ASC")
    fun searchFavKnownWords(searchQuery:String, isKnown:Boolean = true, isFav: Boolean = true, moduleId: Int):LiveData<List<Words>>

    @Query("DELETE FROM words")
    suspend fun deleteAllWords()
}