package eu.alkismavridis.euljava.parser

import eu.alkismavridis.euljava.core.CompileOptions
import eu.alkismavridis.euljava.core.EulLogger
import eu.alkismavridis.euljava.core.ast.EulToken
import eu.alkismavridis.euljava.core.ast.expressions.tokens.EulReference
import eu.alkismavridis.euljava.core.ast.keywords.KeywordType
import eu.alkismavridis.euljava.core.ast.operators.SpecialCharType
import eu.alkismavridis.euljava.core.ast.statements.*
import eu.alkismavridis.euljava.parser.expressions.NewLinePolicy
import eu.alkismavridis.euljava.parser.expressions.ExpressionParser
import eu.alkismavridis.euljava.parser.token.EulTokenizer
import java.io.Reader
import java.util.*

class DefaultEulParser(reader: Reader, private val logger: EulLogger, private val options: CompileOptions) : TokenSource, EulParser {
    private val tokenizer = EulTokenizer(reader, logger, options)
    private var rolledBackTokens = ArrayDeque<EulToken>()

    private val expressionParser = ExpressionParser(this)
    private val typeParser = TypeParser(this)
    private val varDeclarationParser = VariableDeclarationStatementParser(this, this.expressionParser, this.typeParser)

    override fun getNextStatement(): EulStatement? {
        val firstToken = this.getNextStatementStart() ?: return null

        when (firstToken.getKeywordType()) {
            KeywordType.RETURN -> return this.parseReturnStatement(firstToken)
            KeywordType.CONTINUE -> return ContinueStatement(firstToken.line, firstToken.column)
            KeywordType.BREAK -> return BreakStatement(firstToken.line, firstToken.column)

            KeywordType.CONST,
            KeywordType.LET -> return this.varDeclarationParser.parse(firstToken)
        }

        throw ParserException.of(firstToken, "Unexpected token for statement start")
    }

    private fun getNextStatementStart() : EulToken? {
        while(true) {
            val next = this.getNextToken(true) ?: return null
            if (next.getSpecialCharType() != SpecialCharType.SEMICOLON) {
                return next
            }
        }
    }

    private fun parseReturnStatement(returnToken: EulToken): ReturnStatement {
        val expression = this.expressionParser.readExpression(NewLinePolicy.RESPECT)
        return ReturnStatement(expression, returnToken.line, returnToken.column)
    }


    /// TOKEN READING
    override fun getNextToken(skipNewLines: Boolean): EulToken? {
        return if (this.rolledBackTokens.isEmpty()) this.tokenizer.getNextToken(skipNewLines)
        else this.rolledBackTokens.pop()
    }

    override fun requireNextToken(skipNewLines: Boolean, eofMessage: String): EulToken {
        if (this.rolledBackTokens.isEmpty()) {
            return this.tokenizer.getNextToken(skipNewLines) ?: throw ParserException.eof(eofMessage)
        }

        return this.rolledBackTokens.pop()
    }

    override fun rollBackToken(token: EulToken) {
        this.rolledBackTokens.push(token)
    }


    /// UTILS
    override fun requireReference(wrongTokenMessage: String, eofErrorMessage: String) : EulReference {
        val nextToken = this.getNextToken(true)
                ?: throw ParserException.eof("Expected type but end of file was found")

        if (nextToken !is EulReference) {
            throw ParserException.of(nextToken, "Expected type")
        }

        return nextToken
    }

}
