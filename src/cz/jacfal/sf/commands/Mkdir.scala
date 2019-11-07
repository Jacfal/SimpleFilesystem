package cz.jacfal.sf.commands
import cz.jacfal.sf.files.{DirEntry, Directory}
import cz.jacfal.sf.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
  override def doCreateSpecificEntry(state: State): DirEntry = {
    Directory.empty(state.workingDirectory.path, name)
  }
}
