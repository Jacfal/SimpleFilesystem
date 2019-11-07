package cz.jacfal.sf.commands
import cz.jacfal.sf.filesystem.State

class Exit extends Command {
  override def apply(state: State): State = {
    System.exit(1)
    state.setMessage("exited")
  }
}
