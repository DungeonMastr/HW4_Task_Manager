import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate
object Database {
    private const val dbName = "taskManager.db"
    private val connection: Connection by lazy {
        DriverManager.getConnection("jdbc:sqlite:$dbName").apply {
            createTaskTable()
        }
    }

    private fun Connection.createTaskTable() {
        createStatement().executeUpdate(
            """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                due_date TEXT NOT NULL,
                priority INTEGER NOT NULL,
                status TEXT NOT NULL
            )
            """
        )
    }

    fun addTask(task: Task) {
        connection.prepareStatement(
            """
            INSERT INTO tasks (id, title, description, due_date, priority, status) VALUES (?, ?, ?, ?, ?, ?)
            """
        ).apply {
            setInt(1, task.id)
            setString(2, task.title)
            setString(3, task.description)
            setObject(4, task.dueDate.toString())
            setInt(5, task.priority)
            setString(6, task.status.name)
            executeUpdate()
        }
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val resultSet = connection.createStatement().executeQuery("SELECT * FROM tasks")

        while (resultSet.next()) {
            tasks.add(
                Task(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    LocalDate.parse(resultSet.getString("due_date")),
                    resultSet.getInt("priority"),
                    Status.valueOf(resultSet.getString("status"))
                )
            )
        }

        return tasks
    }

    fun updateTask(task: Task) {
        connection.prepareStatement(
            """
        UPDATE tasks SET
            title = ?,
            description = ?,
            due_date = ?,
            priority = ?,
            status = ?
        WHERE id = ?
        """
        ).apply {
            setString(1, task.title)
            setString(2, task.description)
            setObject(3, task.dueDate.toString())
            setInt(4, task.priority)
            setString(5, task.status.name)
            setInt(6, task.id)
            executeUpdate()
        }
    }

    fun deleteTask(taskId: Int) {
        connection.prepareStatement(
            """
        DELETE FROM tasks WHERE id = ?
        """
        ).apply {
            setInt(1, taskId)
            executeUpdate()
        }
    }
    fun getMaxTaskId(): Int {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT MAX(id) FROM tasks")
        return if (resultSet.next()) {
            resultSet.getInt(1)
        } else {
            0
        }
    }
}