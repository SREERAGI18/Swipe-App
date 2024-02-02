package com.swipeapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.swipeapp.room.SwipeDatabase
import com.swipeapp.room.entities.Words

class SyncAddGroupWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
): CoroutineWorker(context, workerParameters) {

    private val syncAddGroupDao = SwipeDatabase.getDatabase(context).wordsDao()
    override suspend fun doWork(): Result {
        val syncGroups = syncAddGroupDao.getFavoriteWords(moduleId = 1)
        val list = mutableListOf<Words>()
//
//        for (syncGroup in syncGroups) {
//            list.add(
//                SyncGroupList(
//                    name = syncGroup.name,
//                    wordIds = syncGroup.wordIds,
//                    timestamps = syncGroup.timestamp
//                )
//            )
//        }

        return if(list.isNotEmpty()) {
//            val request = SyncGroupListRequest(list)
//
//            val response = NetworkRepository(context).syncGroupList(request)
//
//            if(response is SuccessResponse) {
//
//                response.data.data?.let { groupList ->
//                    for (i in groupList.indices) {
//                        val group = groupList[i]
//                        wordGroupDao.updateGroupId(groupId = group.id, oldGroupId = syncGroups[i].id)
//                        wordGroupDao.updateFavoriteListWordIds(groupId = group.id, oldGroupId = syncGroups[i].id)
//                    }
//                }
//
//                syncAddGroupDao.deleteAllAddGroupSync()
//            }

            Result.success()
        }else {
            Result.success()
        }

    }
}