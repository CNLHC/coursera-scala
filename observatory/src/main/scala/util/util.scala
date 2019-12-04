package util

import scala.util.Try

object util {
  def TryToInt(s: String): Option[Int] = Try(s.toInt).toOption

  def TryToDouble(s: String): Option[Double] = Try(s.toDouble).toOption

}
