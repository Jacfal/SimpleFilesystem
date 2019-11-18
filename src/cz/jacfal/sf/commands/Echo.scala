package cz.jacfal.sf.commands
import cz.jacfal.sf.files.{DirEntry, Directory, File}
import cz.jacfal.sf.filesystem.State

import scala.annotation.tailrec

class Echo(args: Array[String]) extends Command {
  override def apply(state: State): State = {
    if (args.isEmpty)
      state
    else if (args.length == 1)
      state.setMessage(args(0))
    else {
      val operator: String = args(args.length - 2) // check for the > or >>
      val filename: String = args(args.length - 1)
      val contents: String = createContent(args, args.length - 2) // create content from the rest args

      if (">>".equals(operator))
        doEcho(state, contents, filename, true)
      else if (">".equals(operator))
        doEcho(state, contents, filename, false)
      else
        state.setMessage(createContent(args, args.length))
    }
  }

  def createContent(args: Array[String], topIndex: Int): String = {
    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String = {
      if (currentIndex >= topIndex)
        accumulator
      else
        createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))
    }

    createContentHelper(0, "")
  }

  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean): Directory = {
    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)

      if (dirEntry == null) {
        currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      } else if (dirEntry.isDir) {
        currentDirectory
      } else {
        if (append)
          currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
        else
          currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContents(contents))
      }
    } else {
      val nextEntry: Directory = currentDirectory.findEntry(path.head).asDirectory
      val newNextEntry: Directory = getRootAfterEcho(nextEntry, path.tail, contents, append)

      if (newNextEntry == nextEntry)
        currentDirectory
      else
        currentDirectory.replaceEntry(path.head, newNextEntry)
    }
  }

  def doEcho(state: State, content: String, filename: String, appendMode: Boolean): State = {
    if (filename.contains(Directory.SEPARATOR)) {
      state.setMessage("Echo: filename must not contains separators")
    } else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.workingDirectory.getAllFoldersInThePath :+ filename, content, appendMode)r
      if (newRoot == state.root) {
        state.setMessage(filename + ": no such file")
      } else {
        State(newRoot, newRoot.findDescendant(state.workingDirectory.getAllFoldersInThePath))
      }
    }
  }
}
