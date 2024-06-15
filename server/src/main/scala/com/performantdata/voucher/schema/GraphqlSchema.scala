package com.performantdata.voucher.schema

import caliban.*
import caliban.schema.ArgBuilder.auto.*
import caliban.schema.Schema.auto.*
import com.performantdata.model.*

/** The GraphQL schema for the democracy vouchers. */
object GraphqlSchema {
  /** The GraphQL query schema for the democracy vouchers. */
  case class Queries(
    personByName: PersonalName => Person
  )
  
  /** The schema's query resolver. */
  private val queries = Queries(
    personByName = _ => Person(names = PersonalName("Joe") :: Nil, casualName = None, birthDate = None)
  )
  
  val api: GraphQL[Any] = graphQL(RootResolver(queries))
}
