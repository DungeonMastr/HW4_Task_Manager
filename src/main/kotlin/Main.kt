import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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
enum class Status { COMPLETED, INCOMPLETE }

var taskId = Database.getMaxTaskId()
//creates a database object
val tasks: MutableList<Task> = Database.getAllTasks().toMutableList()
//Loop for the commands and menu. Uses scanner to check for the commands, It will be used for the future data
//from the command prompt.
fun main() {
    val scanner = Scanner(System.`in`)
    var command: String

    do {
        println("Enter command (add/list/edit/delete/filter/sort/quit):")
        command = scanner.next()

        when (command.lowercase()) {
            "add" -> addTask(scanner)
            "list" -> listTasks()
            "edit" -> editTask(scanner)
            "delete" -> deleteTask(scanner)
            "filter" -> filterTasks(scanner)
            "sort" -> sortTasks(scanner)
            "quit" -> println("Exiting Task Manager...")
            else -> println("Invalid command. Please try again.")
        }
    } while (command.lowercase() != "quit")
}
//Adds a new task and appends it to tasks list
fun addTask(scanner: Scanner) {
    taskId++
    scanner.nextLine() // Consume the newline character
    print("Enter task title: ")
    val title = scanner.nextLine()
    print("Enter task description: ")
    val description = scanner.nextLine()
    print("Enter task due date (dd-MM-yyyy): ")
    val dueDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    print("Enter task priority (1-5): ")
    val priority = scanner.nextInt()
    val status = Status.INCOMPLETE

    val task = Task(taskId, title, description, dueDate, priority, status)
    tasks.add(task)
    Database.addTask(task)
    println("Task added.")
}
//Lists all the tasks
fun listTasks() {
    if (tasks.isEmpty()) {
        println("No tasks found.")
    } else {
        tasks.forEach { task ->
            println(task)
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

        print("Enter new task due date (dd-MM-yyyy, leave blank to keep current): ")
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

        Database.updateTask(task)
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
        Database.deleteTask(id)
        println("Task deleted.")
    } else {
        println("Task not found.")
    }
}
//Filters the task by the status or priority and shows the filtered ones in the command prompt
fun filterTasks(scanner: Scanner) {
    print("Filter by (status/priority): ")
    val filterType = scanner.next().lowercase()
    scanner.nextLine() // Consume the newline character
    when (filterType) {
        "status" -> {
            print("Enter status to filter by (completed/incomplete): ")
            val status = scanner.next().uppercase()
            val filteredTasks = tasks.filter { it.status.name == status }
            if (filteredTasks.isEmpty()) {
                println("No tasks found with the specified status.")
            } else {
                filteredTasks.forEach { task ->
                    println(task)
                }
            }
        }
        "priority" -> {
            print("Enter priority to filter by (1-5): ")
            val priority = scanner.nextInt()
            val filteredTasks = tasks.filter { it.priority == priority }
            if (filteredTasks.isEmpty()) {
                println("No tasks found with the specified priority.")
            } else {
                filteredTasks.forEach { task ->
                    println(task)
                }
            }
        }
        else -> println("Invalid filter type. Please try again.")
    }
}
//Sorts the list of tasks by priority/title/due date
fun sortTasks(scanner: Scanner) {
    print("Sort by (title/due_date/priority): ")
    val sortBy = scanner.next().lowercase()
    scanner.nextLine() // Consume the newline character
    val sortedTasks = when (sortBy) {
        "title" -> tasks.sortedBy { it.title }
        "due_date" -> tasks.sortedBy { it.dueDate }
        "priority" -> tasks.sortedByDescending { it.priority }
        else -> {
            println("Invalid sort option. Please try again.")
            return
        }
    }

    sortedTasks.forEach { task ->
        println(task)
    }
}