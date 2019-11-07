package cz.jacfal.sf.commands
import cz.jacfal.sf.filesystem.State

class Pwd extends Command {
  override def apply(state: State): State = {
    state.setMessage(state.workingDirectory.path)
  }
}
