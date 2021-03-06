package org.neo4j.graphql

import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.procedure.Context
import org.neo4j.procedure.Name
import org.neo4j.procedure.Procedure
import java.util.stream.Stream

/**
 * @author mh
 * @since 29.10.16
 */
class GraphQLProcedure {

    @Context
    @JvmField var db: GraphDatabaseService? = null

    class GraphQLResult(@JvmField val result: Map<String, Any>)

    @Procedure("graphql.execute")
    fun execute(@Name("query") query : String , @Name("variables") variables : Map<String,Any>) : Stream<GraphQLResult> {
        val result = GraphSchema.getGraphQL(db!!).execute(query,db, variables)

        if (result.errors.isEmpty()) {
            return Stream.of(GraphQLResult(result.data as Map<String, Any>))
        }

        val errors = result.errors.joinToString("\n")
        throw RuntimeException("Error executing GraphQL Query:\n $errors")
    }
}
