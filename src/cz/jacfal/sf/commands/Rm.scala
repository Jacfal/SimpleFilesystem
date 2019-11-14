package cz.jacfal.sf.commands
import cz.jacfal.sf.files.{DirEntry, Directory}
import cz.jacfal.sf.filesystem.State

class Rm(name: String) extends Command {
  override def apply(state: State): State = {
    // get abs path
    val absPath: String = {
      if (name.startsWith(Directory.SEPARATOR)) {
        name
      } else if (state.workingDirectory.isRoot) {
        state.workingDirectory.path + name
      }else {
        state.workingDirectory.path + Directory.SEPARATOR + name
      }
    }

    // do some checks
    if (Directory.ROOT_PATH.equals(absPath)) {
      state.setMessage("You can't remove root dir!")

      state // return
    }

    doRm(state, absPath)
  }

  def doRm(state: State, path: String): State = {
    // find the entry to remove
    // update structure

    def rmHelper(currentDir: Directory, path: List[String]): Directory = {
      if (path.isEmpty)
        currentDir // edge case
      else if (path.tail.isEmpty)
        currentDir.removeEntry(path.head)
      else {
        val nextDir = currentDir.findEntry(path.head)

        if (!nextDir.isDir)
          currentDir
        else {
          val newNextDir = rmHelper(nextDir.asDirectory, path.tail)

          if (nextDir == newNextDir)
            currentDir
          else
            currentDir.replaceEntry(path.head, newNextDir)
        }
      }
    }

    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList
    val newRoot: Directory = rmHelper(state.root, tokens)

    if (newRoot == state.root)
      state.setMessage(path + ": no such file or a directory")
    else
      State(newRoot, newRoot.findDescendant(state.workingDirectory.path.substring(1)))
  }
}
