package com.blipblipcode.distribuidoraayl.core.local.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MigrationsFactory {

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 1. Crear nueva tabla temporal con uid nullable
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS sale_data_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                uid TEXT,
                number INTEGER NOT NULL,
                clientRut TEXT NOT NULL,
                date TEXT NOT NULL,
                token TEXT,
                resolution_number INTEGER,
                resolution_date TEXT,
                timbre TEXT,
                isSynchronized INTEGER NOT NULL
            )
        """.trimIndent()
            )

            // 2. Copiar los datos de la tabla antigua a la nueva
            database.execSQL(
                """
            INSERT INTO sale_data_new (id, uid, number, clientRut, date, token,  resolution_number, resolution_date, timbre, isSynchronized)
            SELECT id, uid, number, clientRut, date, token, resolution_number, resolution_date, timbre, isSynchronized
            FROM sale_data
        """.trimIndent()
            )

            // 3. Eliminar la tabla antigua
            database.execSQL("DROP TABLE sale_data")

            // 4. Renombrar la tabla nueva
            database.execSQL("ALTER TABLE sale_data_new RENAME TO sale_data")

            // 5. Volver a crear el índice único
            database.execSQL("CREATE UNIQUE INDEX index_sale_data_uid ON sale_data(uid)")
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 1. Agregar la columna dteType con valor por defecto 0
            database.execSQL(
                """
                ALTER TABLE sale_data ADD COLUMN dteType INTEGER NOT NULL DEFAULT 0
                """.trimIndent()
            )

            // 2. Actualizar dteType a 1 donde token no es null
            database.execSQL(
                """
                UPDATE sale_data 
                SET dteType = 1 
                WHERE token IS NOT NULL
                """.trimIndent()
            )
        }
    }

}