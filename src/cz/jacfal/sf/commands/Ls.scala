package cz.jacfal.sf.commands
import cz.jacfal.sf.files.DirEntry
import cz.jacfal.sf.filesystem.State

class Ls extends Command {
  override def apply(state: State): State = {
    state.setMessage(createOutput(state.workingDirectory.contents))
  }

  def createOutput(contents: List[DirEntry]): String = {
    if (contents.isEmpty) ""
    else {
      val entry = contents.head
      entry.name + "[" + entry.getType + "]" + "\n" + createOutput(contents.tail)
    }
  }
}
