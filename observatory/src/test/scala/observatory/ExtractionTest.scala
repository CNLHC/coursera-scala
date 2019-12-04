package observatory

import junit.framework.TestCase
import observatory.Extraction.{locateTemperatures, locationYearlyAverageRecords}
import org.junit.Assert._
import org.junit.Test

trait ExtractionTest extends MilestoneSuite {
  private val milestoneTest = namedMilestoneTest("data extraction", 1) _

  // Implement tests for the methods of the `Extraction` object
  @Test def testLocate(): Unit = {
    locateTemperatures(1975, "src/main/resources/stations.csv", "src/main/resources/1975.csv")
  }

  @Test def testAverage(): Unit = {
    val stream =
      locateTemperatures(1975, "src/main/resources/stations.csv", "src/main/resources/1975.csv")
    locationYearlyAverageRecords(stream)
  }


}
