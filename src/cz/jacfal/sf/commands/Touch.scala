package cz.jacfal.sf.commands
import cz.jacfal.sf.files.{DirEntry, File}
import cz.jacfal.sf.filesystem.State

class Touch(name: String) extends CreateEntry(name) {

  override def doCreateSpecificEntry(state: State): DirEntry = {
    File.empty(state.workingDirectory.path, name)
  }
}
