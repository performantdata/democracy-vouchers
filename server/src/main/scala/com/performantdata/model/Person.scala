package com.performantdata.model

import java.time.LocalDate

/** A person.
  * 
  * An individual human. This does '''not''' include corporations.
  * 
  * @param names
  *   This person's name(s).
  *   The first name in the sequence is the person's "primary" name,
  *   the one which he would use in legal or government-interfacing contexts.
  * @param casualName
  *   The name that this person uses in a casual context.
  *   In some cultures—like the U.S.'s—its use may be preferred over the primary name in a business conversation.
  *   `None` if non-existent or unknown.
  * @param birthDate Day on which this person was born (in the time zone he was born). `None` if unknown.
  */
case class Person private (names: Seq[PersonalName], casualName: Option[String], birthDate: Option[LocalDate])

object Person {
  /** Create a [[Person]], cleaning his names in the process. */
  def apply(names: Seq[PersonalName], casualName: Option[String], birthDate: Option[LocalDate]): Person =
    new Person(names, casualName.map(PersonalName.cleanName), birthDate)
}

/** A name of a [[Person person]].
  * 
  * A person may have multiple names but is expected to have at least one.
  * 
  * Contrary to Western expectations, the only really consistent feature of a person's name is the "full name".
  * 
  * @param fullName
  *   The complete, unabbreviated name of the person.
  *   Parts of the name must be separated by the Unicode SPACE character, 0x0020.
  */
case class PersonalName private (fullName: String) {
  require(validateFullName(fullName))
  
  /** Return whether the given full name is valid. */
  private def validateFullName(fullName: String): Boolean = {
    true
  }
}

object PersonalName {
  /** Create a [[PersonalName]], cleaning the name in the process. */
  def apply(fullName: String): PersonalName = new PersonalName(cleanName(fullName))
  
  /** Return the given name but with extraneous characters removed. */
  def cleanName(name: String): String =
    name
}
