import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Scanner
//Task class for the task manager. Will be used as the main unit for task saving
data class Task(
    var id: Int,
    var title: String,
    var description: String,
    var dueDate: LocalDate,
    var priority: Int,
    var status: Status
)
//Status of the tasks made
enum class Status {
    COMPLETED, INCOMPLETE
}

val tasks = mutableListOf<Task>()
var taskId = 0
//Loop for the commands and menu. Uses scanner to check for the commands, It will be used for the future data
//from the command prompt.
fun main() {
    val scanner = Scanner(System.`in`)
    var command: String

    do {
        printMenu()
        command = scanner.next().lowercase()

        when (command) {
            "add" -> addTask(scanner)
            "list" -> listTasks()
            "edit" -> editTask(scanner)
            "delete" -> deleteTask(scanner)
            "sort" -> sortTasks()
            "filter" -> filterTasks(scanner)
            "exit" -> println("No tasks for ya, loser!")
            else -> println("Commands are written, u stoopid?")
        }
    } while (command != "exit")
}
//Prints menu
fun printMenu() {
    println(
        """
        |Task Manager Menu:
        |add - Add a new task
        |list - List all tasks
        |edit - Edit an existing task
        |delete - Delete a task
        |sort - Sort tasks by priority
        |filter - Filter tasks by status (completed/incomplete)
        |exit - Exit the application
        |Enter a command:
    """.trimMargin()
    )
}
//Adds a new task and appends it to tasks list
fun addTask(scanner: Scanner) {
    taskId++
    scanner.nextLine()
    print("Enter task title: ")
    val title = scanner.nextLine()
    print("Enter task description: ")
    val description = scanner.nextLine()
    print("Enter task due date (dd-mm-yyyy): ")
    val dueDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    print("Enter task priority (1-5): ")
    val priority = scanner.nextInt()
    val status = Status.INCOMPLETE

    val task = Task(taskId, title, description, dueDate, priority, status)
    tasks.add(task)
    println("Task added.")
}
//Lists all the tasks
fun listTasks() {
    if (tasks.isEmpty()) {
        println("No tasks found.")
    } else {
        tasks.forEach {
            println(it)
        }
    }
}
//Edits the tasks, as well as allows to change the status from Incomplete to Completed
fun editTask(scanner: Scanner) {
    print("Enter task ID to edit: ")
    val id = scanner.nextInt()
    scanner.nextLine() // Consume the newline character
    val task = tasks.find { it.id == id }

    if (task != null) {
        print("Enter new task title (leave blank to keep current): ")
        val title = scanner.nextLine()
        if (title.isNotBlank()) {
            task.title = title
        }

        print("Enter new task description (leave blank to keep current): ")
        val description = scanner.nextLine()
        if (description.isNotBlank()) {
            task.description = description
        }

        print("Enter new task due date (dd-mm-yyyy, leave blank to keep current): ")
        val dueDate = scanner.nextLine()
        if (dueDate.isNotBlank()) {
            task.dueDate = LocalDate.parse(dueDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }

        print("Enter new task priority (1-5, leave blank to keep current): ")
        val priority = scanner.nextLine()
        if (priority.isNotBlank()) {
            task.priority = priority.toInt()
        }

        print("Enter new task status (completed/incomplete, leave blank to keep current): ")
        val status = scanner.nextLine()
        if (status.isNotBlank()) {
            task.status = Status.valueOf(status.uppercase())
        }

        println("Task updated.")
    } else {
        println("Task not found.")
    }
}
//Deletes the task based on the ID given automatically
fun deleteTask(scanner: Scanner) {
    print("Enter task ID to delete: ")
    val id = scanner.nextInt()
    val task = tasks.find { it.id == id }
    if (task != null) {
        tasks.remove(task)
        println("Task deleted.")
    } else {
        println("Task not found.")
    }
}
//Sorts the list of tasks by priority
fun sortTasks() {
    tasks.sortByDescending { it.priority }
    println("Tasks sorted by priority.")
}
//Filters the task by the status and shows the filtered ones in the command prompt
fun filterTasks(scanner: Scanner) {
    print("Enter status to filter tasks (completed/incomplete): ")
    val status = scanner.next().uppercase()
    if (status == "COMPLETED" || status == "INCOMPLETE") {
        val filteredTasks = tasks.filter { it.status == Status.valueOf(status) }
        if (filteredTasks.isEmpty()) {
            println("No tasks found with the specified status.")
        } else {
            filteredTasks.forEach {
                println(it)
            }
        }
    } else {
        println("Invalid status. Please try again.")
    }
}