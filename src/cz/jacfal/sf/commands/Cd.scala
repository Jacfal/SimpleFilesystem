package cz.jacfal.sf.commands
import cz.jacfal.sf.files.{DirEntry, Directory}
import cz.jacfal.sf.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {
  override def apply(state: State): State = {

    // 1. find root
    val root: Directory = state.root

    // 2. find the abs path
    val absPath: String = {
      if (dir.startsWith(Directory.SEPARATOR)) // abs path
        dir
      else { // non-abs path
        if (state.workingDirectory.isRoot)
          state.workingDirectory + dir
        else
          state.workingDirectory + Directory.SEPARATOR + dir
      }
    }

    // 3. find the cs path dir
    val destDir: DirEntry = findEntry(root, absPath)

    // 4. change the state
    if (destDir == null || !destDir.isDir)
      state.setMessage(dir + ": no such directory")
    else
      State(root, destDir.asDirectory)
  }

  def findEntry(root: Directory, path: String): DirEntry = {

    @tailrec
    def findEntryHelper(currentDir: Directory, path: List[String]): DirEntry = {
      if (path.isEmpty || path.head.isEmpty) currentDir
      else if (path.tail.isEmpty) currentDir.findEntry(path.head)
      else {
        val nextDir: DirEntry = currentDir.findEntry(path.head)

        if (nextDir == null || !nextDir.isDir) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList
    findEntryHelper(root, tokens)
  }
}
