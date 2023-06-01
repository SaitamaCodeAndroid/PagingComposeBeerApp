package com.example.pagingcomposewine.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Beer::class],
    version = 1
)
abstract class BeerDatabase: RoomDatabase() {
    abstract val beerDao: BeerDao
}
