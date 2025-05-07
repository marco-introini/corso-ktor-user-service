package it.marcointroini.repository

import io.ktor.util.logging.KtorSimpleLogger
import it.marcointroini.exceptions.DBOperationException
import it.marcointroini.model.AppUser
import org.postgresql.util.PSQLException
import java.sql.Connection
import java.sql.Statement.RETURN_GENERATED_KEYS
import java.util.UUID

class UserRepository {

    private val logger = KtorSimpleLogger(this::class.simpleName ?: "UserRepository")

    fun create(connection: Connection, appUser: AppUser): UUID {
        val statement = connection.prepareStatement(INSERT, RETURN_GENERATED_KEYS)
        statement.setObject(1, UUID.randomUUID())
        statement.setString(2,appUser.email)
        statement.setString(3,appUser.password)
        statement.setString(4,appUser.firstName)
        statement.setString(5,appUser.lastName)
        return try {
            statement.executeUpdate()
            val resultSet = statement.generatedKeys
            resultSet.next()

            val generatedKey = (resultSet.getObject (  "id") as UUID)
            logger.info("Successfully generated user with id: $generatedKey and email ${appUser.email}")
            generatedKey

        } catch (e: PSQLException) {
            logger.error("Error creating user: ${e.message}")
            throw DBOperationException("Error creating user: ${e.message}", e)
        }
    }

    companion object {
        private const val INSERT =
            "INSERT INTO app.app_user (id, email, password, first_name, last_name) VALUES (?, ?, ?, ?, ?)"
    }
}
