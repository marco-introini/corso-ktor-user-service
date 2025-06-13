package it.marcointroini.repository

import io.ktor.util.logging.KtorSimpleLogger
import it.marcointroini.exceptions.DBOperationException
import it.marcointroini.model.AppUser
import it.marcointroini.model.AppUserEntry
import it.marcointroini.model.UserType
import org.postgresql.util.PSQLException
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement.RETURN_GENERATED_KEYS
import java.util.UUID

class UserRepository {

    private val logger = KtorSimpleLogger(this::class.simpleName ?: "UserRepository")

    fun create(connection: Connection, appUser: AppUser): UUID {
        val statement = connection.prepareStatement(INSERT, RETURN_GENERATED_KEYS)
            .apply {
                setObject(1, UUID.randomUUID())
                setString(2, appUser.email)
                setString(3, appUser.password)
                setString(4, appUser.firstName)
                setString(5, appUser.lastName)
            }
        return try {
            statement.executeUpdate()
            val resultSet = statement.generatedKeys
            resultSet.next()

            (resultSet.getObject("id") as UUID)
                .also {
                    logger.info("Successfully generated user with id: $it and email ${appUser.email}")
                }
        } catch (e: PSQLException) {
            logger.error("Error creating user: ${e.message}")
            throw DBOperationException("Error creating user: ${e.message}", e)
        }
    }

    fun findById(connection: Connection, id: UUID): AppUserEntry? {
        val statement = connection.prepareStatement(SELECT_BY_ID)
            .apply { setObject(1, id) }
        return try {
            executeQuery(statement)
        } catch (ex: PSQLException) {
            logger.error("Unable to find by id: $id")
            throw DBOperationException("Unable to find by id: $id", ex)
        }
    }

    private fun executeQuery(statement: PreparedStatement): AppUserEntry? =
        statement.executeQuery()
            .takeIf { it.next() }
            ?.let { resultSet ->
                AppUserEntry(
                    uuid = resultSet.getObject("id") as UUID,
                    email = resultSet.getString("email"),
                    firstName = resultSet.getString("first_name"),
                    lastName = resultSet.getString("last_name"),
                    type = UserType.valueOf(resultSet.getString("type")),
                )
            }

    companion object {
        private const val INSERT =
            "INSERT INTO app.app_user (id, email, password, first_name, last_name) VALUES (?, ?, ?, ?, ?)"
        private const val SELECT_BY_ID =
            "SELECT id, email, type, first_name, last_name FROM app.app_user WHERE id = ?"
    }
}

