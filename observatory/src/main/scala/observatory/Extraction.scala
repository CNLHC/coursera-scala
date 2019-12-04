package observatory

import java.time.LocalDate
import scala.io.Source
import util.util.{TryToDouble, TryToInt}


/**
  * 1st milestone: data extraction
  */
object Extraction extends ExtractionInterface {

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Year, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Temperature)] = {
    val station = Source.fromInputStream(getClass.getResourceAsStream(stationsFile), "utf-8")
    val temperature = Source.fromInputStream(getClass.getResourceAsStream(temperaturesFile), "utf-8")
    val IndexByTuple: Map[(Int, Int), Location] = station
      .getLines()
      .map(e => e.split(",", -1))
      .map { case Array(t1, t2, t3, t4) => (
        (
          TryToInt(t1).getOrElse(-1),
          TryToInt(t2).getOrElse(-1)
        ),
        (
          TryToDouble(t3),
          TryToDouble(t4)
        ))
      }
      .filter {
        case (_, (Some(_), Some(_))) => true
        case _ => false
      }
      .map { case (tup, (Some(t3), Some(t4))) => (tup, Location(t3, t4)) }

      .toMap
    temperature
      .getLines()
      .map(e => e.split(",", -1))
      .map { case Array(t1, t2, t3, t4, t5) => (
        IndexByTuple.get((
          TryToInt(t1).getOrElse(-1),
          TryToInt(t2).getOrElse(-1)
        )),
        TryToInt(t3), TryToInt(t4), TryToDouble(t5).getOrElse(9999.9))
      }
      .filter {
        case (Some(geo), _, _, _) => true
        case _ => false
      }
      .filter(_._4 < 9999.9)
      .map {
        case (Some(geo), Some(month), Some(day), temp) => (LocalDate.of(year, month, day), geo, ((temp - 32.0) * 5.0 / 9.0))
      }
      .toIterable

  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Temperature)]): Iterable[(Location, Temperature)] = {
    records
      .seq
      .groupBy(_._2)
      .map { case (l, t) => (l, t.foldLeft((0.0, 0))((a, c) => (a._1 + c._3, a._2 + 1))) }
      .map { case (l, (sum, len)) => (l, sum / len) }
  }

}
