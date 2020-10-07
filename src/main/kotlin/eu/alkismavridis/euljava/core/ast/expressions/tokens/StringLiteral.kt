package eu.alkismavridis.euljava.core.ast.expressions.tokens

import eu.alkismavridis.euljava.core.ast.expressions.EulExpression
import eu.alkismavridis.euljava.core.types.NativeEulType

class StringLiteral(val value: String,line: Int, column: Int) : EulExpression(line, column) {
    companion object {
        val TYPE = NativeEulType("String")
    }
}
