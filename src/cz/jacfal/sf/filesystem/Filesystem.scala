package cz.jacfal.sf.filesystem

import java.util.Scanner

import cz.jacfal.sf.commands.Command
import cz.jacfal.sf.files.Directory

object Filesystem extends App {
  val root = Directory.ROOT
  var state = State(root, root)
  val scanner = new Scanner(System.in)

  while (true) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }
}
