package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.MatchItem
import com.example.data.model.SubscriptionRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MatchItem::class, SubscriptionRequest::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun matchDao(): MatchDao
    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "football_prono_db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.matchDao())
                }
            }
        }

        private suspend fun populateInitialData(matchDao: MatchDao) {
            val initialMatches = listOf(
                MatchItem(
                    homeTeam = "Real Madrid",
                    awayTeam = "Barcelona",
                    league = "La Liga Espayòl",
                    matchTime = "Jodi a • 15:00",
                    pickAnalysis = "Fòm dènye match yo:\nReal Madrid nan yon fòm ekselan lakay yo nan Santiago Bernabéu ak 4 viktwa konsekitif. Barcelona gen kèk difikilte nan defans lè yo jwe deyò.\n\nAnaliz atak ak defans:\nVinícius Jr ak Mbappé trè rapid sou kontr-atak. Defans Barcelona ap soufri sou boul pwofondè.\n\nPronostic mwen:\nViktwa Real Madrid oswa plis pase 2.5 gòl nan match la.\n\nNòt pèsonèl:\nReal Madrid gen tout avantaj la pou l pran 3 pwen yo.",
                    prediction = "1 — Viktwa ekip lakay",
                    confidence = "Wo",
                    visibility = "both",
                    updatedAt = "Jodi a 10:30 AM",
                    status = "Ap vini",
                    odds = 2.10
                ),
                MatchItem(
                    homeTeam = "Arsenal",
                    awayTeam = "Manchester City",
                    league = "Premier League Angletè",
                    matchTime = "Demen • 12:30",
                    pickAnalysis = "Fòm dènye match yo:\nDe pi gwo ekip Angletè yo fas a fas. Arsenal make nan chak match lakay yo sezon sa a. Man City ak Haaland gen pi gwo atak lig la.\n\nEstatistik kle:\nNan 5 dènye konfwontasyon ant de ekip sa yo, te toujou gen gòl nan tou de kan yo.\n\nPronostic mwen:\nChak ekip ap make omwen yon gòl (BTTS / Chak ekip make).",
                    prediction = "Chak ekip make",
                    confidence = "Wo",
                    visibility = "premium",
                    updatedAt = "Jodi a 09:15 AM",
                    status = "Ap vini",
                    odds = 1.75
                ),
                MatchItem(
                    homeTeam = "Paris Saint-Germain",
                    awayTeam = "Marseille",
                    league = "Ligue 1 Frans",
                    matchTime = "Dimanch • 14:45",
                    pickAnalysis = "Fòm dènye match yo:\nPSG gen yon atak ki trè pwodiktif nan Parc des Princes, ak yon mwayèn 2.6 gòl pa match. Marseille ap make anpil tou anba nouvo sistèm yo a.\n\nPronostic mwen:\nMatch la ap ouvè anpil ak anpil okazyon. Nou chwazi plis pase 2.5 gòl.",
                    prediction = "Plis pase 2.5 gòl",
                    confidence = "Mwayen",
                    visibility = "public",
                    updatedAt = "Jodi a 08:00 AM",
                    status = "Ap vini",
                    odds = 1.68
                ),
                MatchItem(
                    homeTeam = "Violette AC",
                    awayTeam = "Baltimore SC",
                    league = "Chanpyona Nasyonal Ayiti",
                    matchTime = "Dimanch • 16:00",
                    pickAnalysis = "Fòm dènye match yo:\nGwo chòk foutbòl ayisyen an! Violette AC trè difisil pou bat lakay yo. Defans yo trè solid e sipòtè yo ap prezan an foul.\n\nPronostic mwen:\nViolette AC pap pèdi match sa a: Double Chans 1X (Viktwa Violette oswa Match Nil).",
                    prediction = "Double chans 1X",
                    confidence = "Wo",
                    visibility = "both",
                    updatedAt = "Jodi a 11:00 AM",
                    status = "Ap vini",
                    odds = 1.55
                ),
                MatchItem(
                    homeTeam = "Inter Milan",
                    awayTeam = "Juventus",
                    league = "Serie A Itali",
                    matchTime = "Samdi • 14:45",
                    pickAnalysis = "Fòm dènye match yo:\nDerby d'Italia se toujou yon match taktik ak anpil tansyon. Juventus gen youn nan pi bon defans an Ewòp sezon sa a.\n\nPronostic mwen:\nMwens pase 2.5 gòl paske okenn nan de ekip yo pap pran twòp risk nan kòmansman an.",
                    prediction = "Mwens pase 2.5 gòl",
                    confidence = "Mwayen",
                    visibility = "premium",
                    updatedAt = "Jodi a 07:45 AM",
                    status = "Ap vini",
                    odds = 1.80
                )
            )
            matchDao.insertAll(initialMatches)
        }
    }
}
