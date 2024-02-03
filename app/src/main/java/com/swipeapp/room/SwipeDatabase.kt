package com.swipeapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.swipeapp.room.dao.ProductsDao
import com.swipeapp.room.dao.WordsDao
import com.swipeapp.room.entities.Products
import com.swipeapp.room.entities.Words
import com.swipeapp.room.relations.CategoryChooseTranslationCrossRef
import com.swipeapp.utils.Logger


@Database(entities = [
    Words::class,
    CategoryChooseTranslationCrossRef::class, Products::class], exportSchema = false, version = 1
)
abstract class SwipeDatabase: RoomDatabase() {

    companion object{
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: SwipeDatabase? = null

        fun getDatabase(context: Context): SwipeDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            return INSTANCE ?: synchronized(this) {
                val instanceBuilder = Room.databaseBuilder(
                    context.applicationContext,
                    SwipeDatabase::class.java,
                    "swipe.db"
                )
                Logger.levelError("Instance Builder", instanceBuilder.toString())
                val instance = instanceBuilder.build()
                Logger.levelError("Instance Builder", instance.toString())
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }

    abstract fun wordsDao():WordsDao
    abstract fun productsDao():ProductsDao
}