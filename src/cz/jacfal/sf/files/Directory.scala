package cz.jacfal.sf.files

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
  extends DirEntry(parentPath, name) {

  def hasEntry(name: String): Boolean = ???

  def getAllFoldersInThePath: List[String] = {
    // converts dir path /root/a/b/c/ to a List("root", "a", ...)
    path.substring(1).split(Directory.SEPARATOR).toList
  }

  def findDescendant(path: List[String]): Directory = ???
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = empty("", "")

  def empty(parentPath: String, name: String): Directory = {
    new Directory(parentPath, name, List())
  }
}