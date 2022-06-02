package fix

import zio.Chunk
import zio.parser.{Syntax, *}
import Syntax.{char, charIn, charNotIn, digit}

val seperator: Char = 0x01

opaque type Value =  String
opaque type Tag = Int
case class Field(tag: Tag, value: Value)

object Tag:
  def apply(i: Int): Tag = i
  
object Value:
  def apply(value: String): Value = value

extension (value: Value)
  def text: String = value

extension (tag: Tag)
  def value: Int = tag   

object Fix: 
  export Tag.*
  export Value.*

lazy val sep = char(seperator)

lazy val notSep = charNotIn(seperator).*.string

lazy val tag = digit.repeat.string
        .transform[Tag](text => Tag(text.toInt), tag => tag.value.toString)

lazy val eq = char('=') 

lazy val value = notSep
        .transform[Value](value => Value(value), value => value.text)

lazy val field: Syntax[String, Char, Char, Chunk[Field]] = (tag ~ eq ~ value)
    .repeatWithSep(sep)
    .transform[Chunk[Field]](
        to => to.map((tag, value) => Field(tag, value)), 
        fields => fields.map(field => (field.tag, field.value)))

lazy val message: Syntax[String, Char, Char, Chunk[Chunk[Field]]] = field.repeat