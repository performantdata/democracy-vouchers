package com.performantdata.voucher.database.postgresql
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends Tables {
  val profile: slick.jdbc.JdbcProfile = slick.jdbc.PostgresProfile
}

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for
  // tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Databasechangelog.schema ++ Databasechangeloglock.schema ++ Person.schema ++ PersonalName.schema ++ User.schema

  /** Entity class storing rows of table Databasechangelog
   *  @param id Database column id SqlType(varchar), Length(255,true)
   *  @param author Database column author SqlType(varchar), Length(255,true)
   *  @param filename Database column filename SqlType(varchar), Length(255,true)
   *  @param dateexecuted Database column dateexecuted SqlType(timestamp)
   *  @param orderexecuted Database column orderexecuted SqlType(int4)
   *  @param exectype Database column exectype SqlType(varchar), Length(10,true)
   *  @param md5sum Database column md5sum SqlType(varchar), Length(35,true), Default(None)
   *  @param description Database column description SqlType(varchar), Length(255,true), Default(None)
   *  @param comments Database column comments SqlType(varchar), Length(255,true), Default(None)
   *  @param tag Database column tag SqlType(varchar), Length(255,true), Default(None)
   *  @param liquibase Database column liquibase SqlType(varchar), Length(20,true), Default(None)
   *  @param contexts Database column contexts SqlType(varchar), Length(255,true), Default(None)
   *  @param labels Database column labels SqlType(varchar), Length(255,true), Default(None)
   *  @param deploymentId Database column deployment_id SqlType(varchar), Length(10,true), Default(None) */
  case class DatabasechangelogRow(id: String, author: String, filename: String, dateexecuted: java.sql.Timestamp, orderexecuted: Int, exectype: String, md5sum: Option[String] = None, description: Option[String] = None, comments: Option[String] = None, tag: Option[String] = None, liquibase: Option[String] = None, contexts: Option[String] = None, labels: Option[String] = None, deploymentId: Option[String] = None)
  /** GetResult implicit for fetching DatabasechangelogRow objects using plain SQL queries */
  implicit def GetResultDatabasechangelogRow(implicit e0: GR[String], e1: GR[java.sql.Timestamp], e2: GR[Int], e3: GR[Option[String]]): GR[DatabasechangelogRow] = GR{
    prs => import prs._
    (DatabasechangelogRow.apply).tupled((<<[String], <<[String], <<[String], <<[java.sql.Timestamp], <<[Int], <<[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table databasechangelog. Objects of this class serve as prototypes for rows in queries. */
  class Databasechangelog(_tableTag: Tag) extends profile.api.Table[DatabasechangelogRow](_tableTag, "databasechangelog") {
    def * = ((id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deploymentId)).mapTo[DatabasechangelogRow]
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(author), Rep.Some(filename), Rep.Some(dateexecuted), Rep.Some(orderexecuted), Rep.Some(exectype), md5sum, description, comments, tag, liquibase, contexts, labels, deploymentId)).shaped.<>({r=>import r._; _1.map(_=> (DatabasechangelogRow.apply).tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8, _9, _10, _11, _12, _13, _14)))}, (_:Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), Length(255,true) */
    val id: Rep[String] = column[String]("id", O.Length(255,varying=true))
    /** Database column author SqlType(varchar), Length(255,true) */
    val author: Rep[String] = column[String]("author", O.Length(255,varying=true))
    /** Database column filename SqlType(varchar), Length(255,true) */
    val filename: Rep[String] = column[String]("filename", O.Length(255,varying=true))
    /** Database column dateexecuted SqlType(timestamp) */
    val dateexecuted: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("dateexecuted")
    /** Database column orderexecuted SqlType(int4) */
    val orderexecuted: Rep[Int] = column[Int]("orderexecuted")
    /** Database column exectype SqlType(varchar), Length(10,true) */
    val exectype: Rep[String] = column[String]("exectype", O.Length(10,varying=true))
    /** Database column md5sum SqlType(varchar), Length(35,true), Default(None) */
    val md5sum: Rep[Option[String]] = column[Option[String]]("md5sum", O.Length(35,varying=true), O.Default(None))
    /** Database column description SqlType(varchar), Length(255,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(255,varying=true), O.Default(None))
    /** Database column comments SqlType(varchar), Length(255,true), Default(None) */
    val comments: Rep[Option[String]] = column[Option[String]]("comments", O.Length(255,varying=true), O.Default(None))
    /** Database column tag SqlType(varchar), Length(255,true), Default(None) */
    val tag: Rep[Option[String]] = column[Option[String]]("tag", O.Length(255,varying=true), O.Default(None))
    /** Database column liquibase SqlType(varchar), Length(20,true), Default(None) */
    val liquibase: Rep[Option[String]] = column[Option[String]]("liquibase", O.Length(20,varying=true), O.Default(None))
    /** Database column contexts SqlType(varchar), Length(255,true), Default(None) */
    val contexts: Rep[Option[String]] = column[Option[String]]("contexts", O.Length(255,varying=true), O.Default(None))
    /** Database column labels SqlType(varchar), Length(255,true), Default(None) */
    val labels: Rep[Option[String]] = column[Option[String]]("labels", O.Length(255,varying=true), O.Default(None))
    /** Database column deployment_id SqlType(varchar), Length(10,true), Default(None) */
    val deploymentId: Rep[Option[String]] = column[Option[String]]("deployment_id", O.Length(10,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Databasechangelog */
  lazy val Databasechangelog = new TableQuery(tag => new Databasechangelog(tag))

  /** Entity class storing rows of table Databasechangeloglock
   *  @param id Database column id SqlType(int4), PrimaryKey
   *  @param locked Database column locked SqlType(bool)
   *  @param lockgranted Database column lockgranted SqlType(timestamp), Default(None)
   *  @param lockedby Database column lockedby SqlType(varchar), Length(255,true), Default(None) */
  case class DatabasechangeloglockRow(id: Int, locked: Boolean, lockgranted: Option[java.sql.Timestamp] = None, lockedby: Option[String] = None)
  /** GetResult implicit for fetching DatabasechangeloglockRow objects using plain SQL queries */
  implicit def GetResultDatabasechangeloglockRow(implicit e0: GR[Int], e1: GR[Boolean], e2: GR[Option[java.sql.Timestamp]], e3: GR[Option[String]]): GR[DatabasechangeloglockRow] = GR{
    prs => import prs._
    (DatabasechangeloglockRow.apply).tupled((<<[Int], <<[Boolean], <<?[java.sql.Timestamp], <<?[String]))
  }
  /** Table description of table databasechangeloglock. Objects of this class serve as prototypes for rows in queries. */
  class Databasechangeloglock(_tableTag: Tag) extends profile.api.Table[DatabasechangeloglockRow](_tableTag, "databasechangeloglock") {
    def * = ((id, locked, lockgranted, lockedby)).mapTo[DatabasechangeloglockRow]
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(locked), lockgranted, lockedby)).shaped.<>({r=>import r._; _1.map(_=> (DatabasechangeloglockRow.apply).tupled((_1.get, _2.get, _3, _4)))}, (_:Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column locked SqlType(bool) */
    val locked: Rep[Boolean] = column[Boolean]("locked")
    /** Database column lockgranted SqlType(timestamp), Default(None) */
    val lockgranted: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("lockgranted", O.Default(None))
    /** Database column lockedby SqlType(varchar), Length(255,true), Default(None) */
    val lockedby: Rep[Option[String]] = column[Option[String]]("lockedby", O.Length(255,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Databasechangeloglock */
  lazy val Databasechangeloglock = new TableQuery(tag => new Databasechangeloglock(tag))

  /** Entity class storing rows of table Person
   *  @param id Database column id SqlType(int8), AutoInc, PrimaryKey */
  case class PersonRow(id: Long)
  /** GetResult implicit for fetching PersonRow objects using plain SQL queries */
  implicit def GetResultPersonRow(implicit e0: GR[Long]): GR[PersonRow] = GR{
    prs => import prs._
    PersonRow(<<[Long])
  }
  /** Table description of table person. Objects of this class serve as prototypes for rows in queries. */
  class Person(_tableTag: Tag) extends profile.api.Table[PersonRow](_tableTag, "person") {
    def * = (id).mapTo[PersonRow]
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id)).shaped.<>(r => r.map(_=> PersonRow(r.get)), (_:Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
  }
  /** Collection-like TableQuery object for table Person */
  lazy val Person = new TableQuery(tag => new Person(tag))

  /** Entity class storing rows of table PersonalName
   *  @param id Database column id SqlType(int8), AutoInc, PrimaryKey
   *  @param personId Database column person_id SqlType(int8)
   *  @param fullName Database column full_name SqlType(varchar), Length(200,true)
   *  @param fullNameLatinized Database column full_name_latinized SqlType(varchar), Length(200,true), Default(None) */
  case class PersonalNameRow(id: Long, personId: Long, fullName: String, fullNameLatinized: Option[String] = None)
  /** GetResult implicit for fetching PersonalNameRow objects using plain SQL queries */
  implicit def GetResultPersonalNameRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]]): GR[PersonalNameRow] = GR{
    prs => import prs._
    (PersonalNameRow.apply).tupled((<<[Long], <<[Long], <<[String], <<?[String]))
  }
  /** Table description of table personal_name. Objects of this class serve as prototypes for rows in queries. */
  class PersonalName(_tableTag: Tag) extends profile.api.Table[PersonalNameRow](_tableTag, "personal_name") {
    def * = ((id, personId, fullName, fullNameLatinized)).mapTo[PersonalNameRow]
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(personId), Rep.Some(fullName), fullNameLatinized)).shaped.<>({r=>import r._; _1.map(_=> (PersonalNameRow.apply).tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column person_id SqlType(int8) */
    val personId: Rep[Long] = column[Long]("person_id")
    /** Database column full_name SqlType(varchar), Length(200,true) */
    val fullName: Rep[String] = column[String]("full_name", O.Length(200,varying=true))
    /** Database column full_name_latinized SqlType(varchar), Length(200,true), Default(None) */
    val fullNameLatinized: Rep[Option[String]] = column[Option[String]]("full_name_latinized", O.Length(200,varying=true), O.Default(None))

    /** Foreign key referencing Person (database name fk_personal_name_person) */
    lazy val personFk = foreignKey("fk_personal_name_person", personId, Person)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table PersonalName */
  lazy val PersonalName = new TableQuery(tag => new PersonalName(tag))

  /** Entity class storing rows of table User
   *  @param id Database column id SqlType(int8), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(200,true)
   *  @param personId Database column person_id SqlType(int8) */
  case class UserRow(id: Long, name: String, personId: Long)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Long], e1: GR[String]): GR[UserRow] = GR{
    prs => import prs._
    (UserRow.apply).tupled((<<[Long], <<[String], <<[Long]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends profile.api.Table[UserRow](_tableTag, "user") {
    def * = ((id, name, personId)).mapTo[UserRow]
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(name), Rep.Some(personId))).shaped.<>({r=>import r._; _1.map(_=> (UserRow.apply).tupled((_1.get, _2.get, _3.get)))}, (_:Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int8), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column person_id SqlType(int8) */
    val personId: Rep[Long] = column[Long]("person_id")

    /** Foreign key referencing Person (database name fk_user_person) */
    lazy val personFk = foreignKey("fk_user_person", personId, Person)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (name) (database name user_name_key) */
    val index1 = index("user_name_key", name, unique=true)
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}
