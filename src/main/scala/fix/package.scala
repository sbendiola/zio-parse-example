package fix

import zio.Chunk
import zio.parser.{Syntax, *}
import Syntax.{char, charIn, charNotIn, digit}

val seperator: Char = 0x01

enum FixValue:
    case Sep
    case Value(text: String)
    case Tag(value: Int)
    case Field(tag: Tag, value: Value)

import FixValue.*

lazy val sep = char(seperator)

lazy val notSep = charNotIn(seperator).*.string

lazy val tag = digit.repeat.string
        .transform[Tag](text => Tag(text.toInt), value => value.value.toString)

lazy val eq = char('=')

lazy val value = notSep
        .transform[Value](value => Value(value), value => value.text)

lazy val field = (tag ~ eq ~ value)
    .repeatWithSep(sep)
    .transform[Chunk[Field]](
        to => to.map((tag, value) => Field(tag, value)), 
        fields => fields.map(field => (field.tag, field.value)))

lazy val message = field.repeat