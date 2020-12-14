package net.mamoe.mirai.rainey
import java.sql.*
import java.util.Properties
/**
 * Program to list databases in MySQL using Kotlin
 */
object DBConn {
    private var conn: Connection? = null
    private var username = "root" // provide the username
    private var password = "" // provide the corresponding password
    @JvmStatic fun main(args: Array<String>) {
    }

    fun query(query: String): ResultSet? {
        var stmt: Statement? = null
        var resultset: ResultSet? = null
        try {
            stmt = conn!!.createStatement()
            if (stmt.execute(query)) {
                resultset = stmt.resultSet
            }
            return resultset
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } finally {
        }
        return null
    }
    /**
     * This method makes a connection to MySQL Server
     * In this example, MySQL Server is running in the local host (so 127.0.0.1)
     * at the standard port 3306
     */
    fun getConnection() {
        val connectionProps = Properties()
        connectionProps.put("user", username)
        connectionProps.put("password", password)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance()
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/raineydb?serverTimezone=GMT%2B8&useSSL=false",
                    connectionProps)
        } catch (ex: SQLException) {
            // handle any errors
            ex.printStackTrace()
        } catch (ex: Exception) {
            // handle any errors
            ex.printStackTrace()
        }
    }
}
