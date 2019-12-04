val a = Array("007005,,,")


import scala.util.Try
def tryToInt(s: String) = Try(s.toInt).toOption
def tryToDouble(s: String) = Try(s.toDouble).toOption



val b =
  a
    .map(_.split(",",-1))




