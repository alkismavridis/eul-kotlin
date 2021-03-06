package eu.alkismavridis.euljava.core.ast.expressions.tokens

import eu.alkismavridis.euljava.core.ast.expressions.EulExpression

class BooleanLiteral(val value: Boolean, line: Int, column: Int) : EulExpression(line, column)
