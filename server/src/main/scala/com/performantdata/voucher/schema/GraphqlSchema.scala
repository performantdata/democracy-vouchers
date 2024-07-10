package com.performantdata.voucher.schema

import caliban.*
import caliban.schema.Annotations.GQLDescription
import caliban.schema.ArgBuilder.auto.*
import caliban.schema.Schema.auto.*

/** The GraphQL schema for the democracy vouchers. */
object GraphqlSchema {
  case class PersonFilterArgs(name: PersonalName)
  
  /** The GraphQL query schema for the democracy vouchers.
    * 
    * @param personBySimilarName
    *   Operation that returns any persons that have a personal name similar to the given one,
    *   in order of decreasing similarity.
    */
  case class Queries(
    @GQLDescription("Return any persons that have a personal name similar to the given one, in order of decreasing similarity.")
    personBySimilarName: PersonFilterArgs => Seq[Person]
  )
  
  /** The schema's query resolver. */
  private val queries = Queries(
    personBySimilarName = args => Person(names = args.name :: Nil, casualName = None, birthDate = None) :: Nil
  )
  
  val api: GraphQL[Any] = graphQL(RootResolver(queries))
}
