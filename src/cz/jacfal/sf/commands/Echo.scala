package cz.jacfal.sf.commands
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

  def doEcho(state: State, content: String, filename: String, appendMode: Boolean): State = {
    ???
  }
}
