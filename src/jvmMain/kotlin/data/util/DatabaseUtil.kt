package data.util

import kotlinx.serialization.json.*
import kotlinx.serialization.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.io.File
import kotlinx.serialization.Serializable

@Serializable
data class DbCredentials(val url: String, val username: String, val password: String)

object ConfigReader {
    fun readDbCredentials(): DbCredentials {
        val configFile = File("src/jvmMain/kotlin/data/db/config.json")
        return Json.decodeFromString(configFile.readText())
    }
}


object DatabaseUtil {
    @JvmStatic
    fun getConnection(): Connection? {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance()
            val dbCredentials = ConfigReader.readDbCredentials()
            return DriverManager.getConnection(dbCredentials.url, dbCredentials.username, dbCredentials.password)
        } catch (ex: SQLException) {
            println("Pred connect1")
            println("${ex.javaClass.simpleName} ${ex.message}")
        } catch (ex: Exception) {
            println("${ex.javaClass.simpleName} ${ex.message}")
        }
        return null
    }

    @JvmStatic
    fun testConnection() {
        val query = "SELECT first_name, last_name FROM sakila.actor;"
        val conn = getConnection() ?: return

        conn.use {
            try {
                val select = it.prepareStatement(query)
                val rs = select.executeQuery()
                while (rs.next()) {
                    val firstName = rs.getString("first_name")
                    val lastName = rs.getString("last_name")
                    println("$firstName $lastName")
                }
            } catch (ex: SQLException) {
                println(ex.message)
            }
        }
    }
}