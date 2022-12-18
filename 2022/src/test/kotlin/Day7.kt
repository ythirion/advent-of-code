import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day7 : Day(7, "No Space Left On Device") {
    private fun Directory.`size of directories with a total size of at most 100000`(): Long =
        directorySizes()
            .filter { it < 100_000 }
            .sum()

    private fun Directory.directorySizes() = allDirectories().map { it.size() }

    private fun Directory.`size of the smallest directory to delete`(): Long =
        (30_000_000 - (70_000_000 - size())).let { spaceToFind ->
            directorySizes()
                .filter { it >= spaceToFind }
                .minOf { it }
        }

    @Test
    fun part1() =
        assertEquals(1084134,
            computeResult {
                it.toFileSystem()
                    .`size of directories with a total size of at most 100000`()
            }
        )

    @Test
    fun part2() =
        assertEquals(6183184,
            computeResult {
                it.toFileSystem()
                    .`size of the smallest directory to delete`()
            }
        )

    companion object Day7 {
        data class File(val name: String, val size: Long)
        data class Directory(val name: String, val parent: Directory?) {
            private val directories: MutableList<Directory> = mutableListOf()
            private val files: MutableList<File> = mutableListOf()
            fun add(directory: Directory): Boolean = directories.add(directory)
            fun add(file: File): Boolean = files.add(file)
            fun findDirectory(directory: String): Directory = directories.first { it.name == directory }
            fun size(): Long = files.sumOf { it.size } + directories.sumOf { it.size() }
            fun allDirectories(): List<Directory> = directories + directories.flatMap { it.allDirectories() }
        }

        data class Terminal(val fileSystem: Directory) {
            private var currentDirectory: Directory = fileSystem

            fun changeDirectory(directory: String) {
                currentDirectory = when (directory) {
                    ".." -> currentDirectory.parent!!
                    "/" -> fileSystem
                    else -> currentDirectory.findDirectory(directory)
                }
            }

            fun addFile(file: String): Boolean =
                file.splitWords().let {
                    currentDirectory.add(File(it[1], it[0].toLong()))
                }

            fun addDirectory(directory: String) =
                currentDirectory.add(
                    Directory(directory.splitWords()[1], currentDirectory),
                )

            fun applyCommand(command: String): Unit =
                command.splitWords().let {
                    if (it.count() == 3 && it[1] == "cd")
                        this.changeDirectory(it[2])
                }
        }

        private fun String.isFile(): Boolean = splitWords()[0].toLongOrNull() != null

        private fun String.parse(terminal: Terminal) {
            when {
                startsWith("$") -> terminal.applyCommand(this)
                isFile() -> terminal.addFile(this)
                else -> terminal.addDirectory(this)
            }
        }

        private fun List<String>.toFileSystem(): Directory =
            Directory("/", null).let { filesystem ->
                Terminal(filesystem).let { terminal ->
                    this.forEach { line ->
                        line.parse(terminal)
                    }
                    return filesystem
                }
            }
    }
}