package com.example.caesarzonapplication.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.caesarzonapplication.model.dao.notificationDao.AdminNotificationDao
import com.example.caesarzonapplication.model.dao.notificationDao.BanDao
import com.example.caesarzonapplication.model.dao.notificationDao.ReportDao
import com.example.caesarzonapplication.model.dao.notificationDao.SupportDao
import com.example.caesarzonapplication.model.dao.notificationDao.UserNotificationDao
import com.example.caesarzonapplication.model.dao.productDao.ProductDao
import com.example.caesarzonapplication.model.dao.productDao.ProductImageDao
import com.example.caesarzonapplication.model.dao.productDao.ProductOrderDao
import com.example.caesarzonapplication.model.dao.userDao.AddressDao
import com.example.caesarzonapplication.model.dao.userDao.CardDao
import com.example.caesarzonapplication.model.dao.userDao.CityDataDao
import com.example.caesarzonapplication.model.dao.userDao.FollowerDao
import com.example.caesarzonapplication.model.dao.userDao.ProfileImageDao
import com.example.caesarzonapplication.model.dao.userDao.UserDao
import com.example.caesarzonapplication.model.dao.wishlistDao.WishlistDao
import com.example.caesarzonapplication.model.dao.wishlistDao.WishlistProductDao
import com.example.caesarzonapplication.model.entities.notificationEntity.AdminNotification
import com.example.caesarzonapplication.model.entities.notificationEntity.Ban
import com.example.caesarzonapplication.model.entities.notificationEntity.Report
import com.example.caesarzonapplication.model.entities.notificationEntity.Support
import com.example.caesarzonapplication.model.entities.notificationEntity.UserNotification
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.Product
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.ProductImage
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.ProductOrder
import com.example.caesarzonapplication.model.entities.userEntity.Address
import com.example.caesarzonapplication.model.entities.userEntity.Card
import com.example.caesarzonapplication.model.entities.userEntity.CityData
import com.example.caesarzonapplication.model.entities.userEntity.Follower
import com.example.caesarzonapplication.model.entities.userEntity.ProfileImage
import com.example.caesarzonapplication.model.entities.userEntity.User
import com.example.caesarzonapplication.model.entities.wishListEntity.Wishlist
import com.example.caesarzonapplication.model.entities.wishListEntity.WishlistProduct
import com.example.caesarzonapplication.model.utils.BitmapConverter
import com.example.caesarzonapplication.model.utils.Converters

@Database (entities = [AdminNotification::class, Ban::class, Report::class, Support::class, UserNotification::class,ProfileImage::class,
                      Product::class, ProductOrder::class, ProductImage::class,
                      Address::class, Card::class, CityData::class, Follower::class, User::class,
                      Wishlist::class, WishlistProduct::class],
    version = 4, exportSchema = false)
@TypeConverters(Converters::class, BitmapConverter::class)
abstract class AppDatabase: RoomDatabase()  {
    abstract fun adminNotificationDao(): AdminNotificationDao
    abstract fun banDao(): BanDao
    abstract fun reportDao(): ReportDao
    abstract fun supportDao(): SupportDao
    abstract fun userNotificationDao(): UserNotificationDao
    abstract fun productDao(): ProductDao
    abstract fun productOrderDao(): ProductOrderDao
    abstract fun addressDao(): AddressDao
    abstract fun cardDao(): CardDao
    abstract fun cityDataDao(): CityDataDao
    abstract fun followerDao(): FollowerDao
    abstract fun userDao(): UserDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun wishlistProductDao(): WishlistProductDao
    abstract fun profileImageDao(): ProfileImageDao
    abstract fun productImageDao(): ProductImageDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addMigrations(MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                return instance
            }
        }
        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {

                // Drop all existing tables
                db.execSQL("DROP TABLE IF EXISTS notifiche_admin")
                db.execSQL("DROP TABLE IF EXISTS ban")
                db.execSQL("DROP TABLE IF EXISTS segnala")
                db.execSQL("DROP TABLE IF EXISTS richiesta_supporto")
                db.execSQL("DROP TABLE IF EXISTS notifiche_utente")
                db.execSQL("DROP TABLE IF EXISTS prodotto")
                db.execSQL("DROP TABLE IF EXISTS foto_prodotti")
                db.execSQL("DROP TABLE IF EXISTS ordine_prodotto")
                db.execSQL("DROP TABLE IF EXISTS indirizzo")
                db.execSQL("DROP TABLE IF EXISTS admin")
                db.execSQL("DROP TABLE IF EXISTS carte")
                db.execSQL("DROP TABLE IF EXISTS dati_comune")
                db.execSQL("DROP TABLE IF EXISTS follower")
                db.execSQL("DROP TABLE IF EXISTS foto_utente")
                db.execSQL("DROP TABLE IF EXISTS wishlist")
                db.execSQL("DROP TABLE IF EXISTS prodotto_lista_desideri")

                // Recreate the tables
                db.execSQL("""
            CREATE TABLE IF NOT EXISTS notifiche_admin (
                id_notifche_admin TEXT PRIMARY KEY NOT NULL,
                data TEXT NOT NULL,
                descrizione TEXT NOT NULL,
                username_admin TEXT NOT NULL,
                letta INTEGER NOT NULL,
                id_segnalazione INTEGER NOT NULL,
                id_richiesta_di_supporto INTEGER NOT NULL,
                FOREIGN KEY(id_segnalazione) REFERENCES segnala(id_segnalazione) ON DELETE CASCADE,
                FOREIGN KEY(id_richiesta_di_supporto) REFERENCES richiesta_supporto(id_richiesta_di_supporto) ON DELETE CASCADE
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS ban (
                id_ban TEXT PRIMARY KEY NOT NULL,
                motivo TEXT NOT NULL,
                data_inizio TEXT NOT NULL,
                data_fine TEXT NOT NULL,
                username_utente TEXT NOT NULL,
                username_admin TEXT NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS segnala (
                id_segnalazione TEXT PRIMARY KEY NOT NULL,
                data_segnalazione TEXT NOT NULL,
                motivo TEXT NOT NULL,
                descrizione TEXT NOT NULL,
                username_utente2 TEXT NOT NULL,
                id_recensione TEXT NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS richiesta_supporto (
                id_richiesta_di_supporto TEXT PRIMARY KEY NOT NULL,
                tipo TEXT NOT NULL,
                testo TEXT NOT NULL,
                oggetto TEXT NOT NULL,
                data_richiesta TEXT NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS notifiche_utente (
                id_notifiche_utente TEXT PRIMARY KEY NOT NULL,
                data TEXT NOT NULL,
                descrizione TEXT NOT NULL,
                username_utente TEXT NOT NULL,
                letta INTEGER NOT NULL,
                spiegazione TEXT NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS prodotto (
                id_prodotto TEXT PRIMARY KEY NOT NULL,
                descrizione TEXT NOT NULL,
                nome TEXT NOT NULL,
                marca TEXT NOT NULL,
                sconto INTEGER NOT NULL,
                prezzo REAL NOT NULL,
                colore_primario TEXT NOT NULL,
                colore_secondari TEXT NOT NULL,
                e_abbigliamento INTEGER NOT NULL,
                sport TEXT NOT NULL,
                ultima_aggiunta TEXT NOT NULL,
                quantita INTEGER NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS foto_prodotti (
                id_foto_prodotto TEXT PRIMARY KEY NOT NULL,
                id_prodotto TEXT NOT NULL,
                immagine BLOB NOT NULL,
                FOREIGN KEY(id_prodotto) REFERENCES prodotto(id_prodotto) ON DELETE CASCADE
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS ordine_prodotto (
                id_ordine_prodotto TEXT PRIMARY KEY NOT NULL,
                ref_prodotto TEXT NOT NULL,
                totale REAL NOT NULL,
                id_ordine TEXT NOT NULL,
                quantità INTEGER NOT NULL,
                per_dopo INTEGER NOT NULL,
                taglia TEXT NOT NULL,
                FOREIGN KEY(ref_prodotto) REFERENCES prodotto(id_prodotto) ON DELETE CASCADE
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS indirizzo (
                id_indirizzo TEXT PRIMARY KEY NOT NULL,
                nome_strada TEXT NOT NULL,
                numero_civico TEXT NOT NULL,
                tipo_strada TEXT NOT NULL,
                id_dati_comune TEXT NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS admin (
                nome TEXT PRIMARY KEY NOT NULL,
                cognome TEXT NOT NULL,
                email TEXT NOT NULL,
                password TEXT NOT NULL,
                username TEXT NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS carte (
                id_carta TEXT PRIMARY KEY NOT NULL,
                numero_carta TEXT NOT NULL,
                titolare TEXT NOT NULL,
                cvv TEXT NOT NULL,
                data_scadenza TEXT NOT NULL,
                saldo REAL NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS dati_comune (
                id_dati_comune TEXT PRIMARY KEY NOT NULL,
                nome_comune TEXT NOT NULL,
                cap TEXT NOT NULL,
                regione TEXT NOT NULL,
                provincia TEXT NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS follower (
                id TEXT PRIMARY KEY NOT NULL,
                username_utente TEXT NOT NULL,
                amico INTEGER NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS foto_utente (
                username TEXT PRIMARY KEY NOT NULL,
                foto BLOB
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS wishlist (
                id_lista_desideri TEXT PRIMARY KEY NOT NULL,
                nome TEXT NOT NULL,
                visibilità INTEGER NOT NULL
            )
        """)

                db.execSQL("""
            CREATE TABLE IF NOT EXISTS prodotto_lista_desideri (
                id_prodotto TEXT PRIMARY KEY NOT NULL,
                nome TEXT NOT NULL,
                immagine BLOB NOT NULL,
                id_lista_desideri TEXT NOT NULL,
                FOREIGN KEY(id_lista_desideri) REFERENCES wishlist(id_lista_desideri) ON DELETE CASCADE
            )
        """)

            }
        }

    }

   override fun clearAllTables() {
        adminNotificationDao().deleteAllAdminNotifications()
        banDao().deleteAllBans()
        reportDao().deleteAllReports()
        supportDao().deleteAllSupport()
        userNotificationDao().deleteAllUserNotifications()
        productDao().deleteAllProducts()
        productOrderDao().deleteAllProductOrders()
        addressDao().deleteAll()
        cardDao().deleteAllCards()
        cityDataDao().deleteAllCityData()
        followerDao().deleteAllFollowers()
        wishlistDao().deleteAllWishlist()
        wishlistProductDao().deleteAllWishlistProducts()
    }



    /*fun closeDatabase(){
        INSTANCE?.close()
        INSTANCE = null
    }da capire come gestirla*/
}