package cz.jacfal.sf.files

abstract class DirEntry(val parentPath: String, val name: String) {

  def path: String = {
    val separatorIfNeeded: String = {
      if (Directory.ROOT_PATH.equals(parentPath))
        ""
      else
        Directory.SEPARATOR
    }

    parentPath + separatorIfNeeded + name
  }

  def asDirectory: Directory
  def asFile: File

  def isDir: Boolean
  def isFile: Boolean

  def getType: String

}
