package eu.alkismavridis.euljava.test_utils

import eu.alkismavridis.euljava.core.ast.EulToken
import eu.alkismavridis.euljava.core.ast.expressions.tokens.*
import eu.alkismavridis.euljava.core.ast.keywords.KeywordToken
import eu.alkismavridis.euljava.core.ast.operators.SpecialCharacterToken
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset

class EulAssert {
    companion object {
        fun assertIntegerLiteral(token: EulToken?, value: Long, size: Int, isSigned: Boolean, line: Int, column: Int) {
            assertThat(token).isNotNull().isInstanceOf(IntegerLiteral::class.java)

            val asInt = token as IntegerLiteral
            assertThat(asInt.value).isEqualTo(value)
            assertThat(asInt.getSize()).isEqualTo(size)
            assertThat(asInt.isSigned()).isEqualTo(isSigned)
            assertThat(asInt.line).isEqualTo(line)
            assertThat(asInt.column).isEqualTo(column)
        }

        fun assertFloatLiteral(token: EulToken?, value: Double, size: Int, line: Int, column: Int) {
            assertThat(token).isNotNull().isInstanceOf(FloatLiteral::class.java)

            val asInt = token as FloatLiteral
            assertThat(asInt.value).isEqualTo(value, Offset.offset(0.000001))
            assertThat(asInt.size).isEqualTo(size)
            assertThat(asInt.line).isEqualTo(line)
            assertThat(asInt.column).isEqualTo(column)
        }

        fun assertEulReference(token: EulToken?, name: String, line: Int, column: Int) {
            assertThat(token).isNotNull().isInstanceOf(EulReference::class.java)

            val asRef = token as EulReference
            assertThat(asRef.name).isEqualTo(name)
            assertThat(asRef.line).isEqualTo(line)
            assertThat(asRef.column).isEqualTo(column)
        }

        fun assertOperator(token: EulToken?, text: String, line: Int, column: Int) {
            assertThat(token).isNotNull().isInstanceOf(SpecialCharacterToken::class.java)

            val asOperator = token as SpecialCharacterToken
            assertThat(asOperator.text).isEqualTo(text)
            assertThat(asOperator.line).isEqualTo(line)
            assertThat(asOperator.column).isEqualTo(column)
        }

        fun assertCharLiteral(token: EulToken?, charValue: Long, line: Int, column: Int) {
            assertThat(token).isNotNull().isInstanceOf(CharLiteral::class.java)

            val asChar = token as CharLiteral
            assertThat(asChar.value).isEqualTo(charValue)
            assertThat(asChar.line).isEqualTo(line)
            assertThat(asChar.column).isEqualTo(column)
        }

        fun assertStringLiteral(token: EulToken?, stringContent: String, line: Int, column: Int) {
            assertThat(token).isNotNull().isInstanceOf(StringLiteral::class.java)

            val asChar = token as StringLiteral
            assertThat(asChar.value).isEqualTo(stringContent)
            assertThat(asChar.line).isEqualTo(line)
            assertThat(asChar.column).isEqualTo(column)
        }

        fun assertKeyword(token: EulToken?, text: String, line: Int, column: Int) {
            assertThat(token).isNotNull().isInstanceOf(KeywordToken::class.java)

            val asKeyword = token as KeywordToken
            assertThat(asKeyword.text).isEqualTo(text)
            assertThat(asKeyword.line).isEqualTo(line)
            assertThat(asKeyword.column).isEqualTo(column)
        }

        fun assertBooleanLiteral(token: EulToken?, value: Boolean, line: Int, column: Int) {
            assertThat(token).isNotNull().isInstanceOf(BooleanLiteral::class.java)

            val asBoolean = token as BooleanLiteral
            assertThat(asBoolean.value).isEqualTo(value)
            assertThat(asBoolean.line).isEqualTo(line)
            assertThat(asBoolean.column).isEqualTo(column)
        }

        fun assertNullLiteral(token: EulToken?, line: Int, column: Int) {
            assertThat(token).isNotNull().isInstanceOf(NullLiteral::class.java)

            val asBoolean = token as NullLiteral
            assertThat(asBoolean.line).isEqualTo(line)
            assertThat(asBoolean.column).isEqualTo(column)
        }
    }
}
