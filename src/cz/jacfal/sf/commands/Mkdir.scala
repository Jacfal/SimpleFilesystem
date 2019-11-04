package cz.jacfal.sf.commands
import cz.jacfal.sf.files.{DirEntry, Directory}
import cz.jacfal.sf.filesystem.State

class Mkdir(name: String) extends Command {

  override def apply(state: State): State = {
    val workingDir = state.workingDirectory

    if (workingDir.hasEntry(name)) {
      state.setMessage("Entry " + name + " already exists!")
    } else if (name.contains(Directory.SEPARATOR)) {
      state.setMessage(name + " must not contain separators")
    } else if (checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name")
    } else {
      doMkdir(state, name)
    }
  }

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  def doMkdir(state: State, name: String): State = {

    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if (path.isEmpty) {
        currentDirectory.addEntry(newEntry)
      } else {
        val oldEntry: Directory = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    // 1 . all the directories in the full path
    val allDirsInPath: List[String] = state.workingDirectory.getAllFoldersInThePath

    // 2. create new dir entry in the working dir
    val newDir: Directory = Directory.empty(state.workingDirectory.path, name)

    // 3. update the whole dir structure from the root
    val newRoot: Directory = updateStructure(state.root, allDirsInPath, newDir) // get new directory

    // 4. find new working fir instance given work dir full path in the new dir structure
    val newWorkingDir: Directory = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWorkingDir)
  }
}
