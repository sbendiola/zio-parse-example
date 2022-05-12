package fix

import zio.test.Assertion.{equalTo, isRight}
import zio.test.*
import zio.test.junit.JUnitRunnableSpec
import zio.Chunk
import fix.FixValue
import FixValue.*

object FixSuite extends JUnitRunnableSpec:
  def spec = suite("fix parsing")(
    test("simpleFix") {
      val message = List("8=FIX.5.0", "35=8", "58=Blah blah = #!@$%", "212=<xml>12=3</xml>").mkString(String.valueOf(seperator))
      assert(fix.message.parseString(message))(isRight(equalTo(
        Chunk(Chunk(
          Field(Tag(8), Value("FIX.5.0")),
          Field(Tag(35), Value("8")),
          Field(Tag(58), Value("Blah blah = #!@$%")),
          Field(Tag(212), Value("<xml>12=3</xml>")),
          )))))
      },
    test("index of seperator") {
        val message = List("8=FIX.5.0", "35=8", "58=Blah blah = #!@$%").mkString(String.valueOf(seperator))
        assert(message.indexOf(seperator))(Assertion.not(equalTo(-1)))
    })