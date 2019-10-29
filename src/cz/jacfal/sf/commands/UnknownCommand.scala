package cz.jacfal.sf.commands
import cz.jacfal.sf.filesystem.State

class UnknownCommand extends Command {
  override def apply(state: State): State =
    state.setMessage("Command not found!")
}
