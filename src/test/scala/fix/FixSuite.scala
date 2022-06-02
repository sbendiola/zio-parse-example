package fix

import zio.test.Assertion.{equalTo, isRight}
import zio.test.{ ZIOSpecDefault, assert, suite }
import zio.test.*
import zio.Chunk
import fix.Fix.*

object FixSuite extends ZIOSpecDefault:

  def FixString(fields: String*): String = 
    fields.map(_.trim)
      .mkString(String.valueOf(seperator))

  def spec = suite("fix parsing")(
    test("simpleFix") {
      val message = FixString("8=FIX.5.0", "35=8", "58=Blah blah = #!@$%", "212=<xml>12=3</xml>")
      assert(fix.message.parseString(message))(isRight(equalTo(
        Chunk(Chunk(
          Field(Tag(8), Value("FIX.5.0")),
          Field(Tag(35), Value("8")),
          Field(Tag(58), Value("Blah blah = #!@$%")),
          Field(Tag(212), Value("<xml>12=3</xml>")),
          )))))
      },
      
    test("index of seperator") {
        val message = FixString("8=FIX.5.0", "35=8", "58=Blah blah = #!@$%")
        assert(message.indexOf(seperator))(Assertion.not(equalTo(-1)))
    })