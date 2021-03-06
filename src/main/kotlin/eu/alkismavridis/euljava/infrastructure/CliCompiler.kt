package eu.alkismavridis.euljava.infrastructure

import eu.alkismavridis.euljava.analyse.FileAnalyser
import eu.alkismavridis.euljava.core.CompileOptions
import eu.alkismavridis.euljava.core.ast.statements.EulStatement
import eu.alkismavridis.euljava.emitter.DoNothingEmitter
import eu.alkismavridis.euljava.parser.DefaultEulParser
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.nio.file.AccessDeniedException
import java.nio.file.Files
import java.nio.file.Paths


class EulFileNotReadableException(message: String) : AccessDeniedException(message)

class CliCompiler {
    private val logger = SimpleEulLogger()

    fun compile(params: Array<String>) {
        try {
            val options = this.createOptions(params)
            this.compileFile(options)
        }
        catch (e: Exception) { this.logger.error(e.message ?: "") }
    }

    private fun createOptions(params: Array<String>): CompileOptions {
        if(params.size != 2) {
            throw IllegalArgumentException("Please provider input file and output file")
        }

        return CompileOptions(params[0])
    }

    private fun compileFile(options: CompileOptions) {
        val statements = mutableListOf<EulStatement>()
        while(true) {
            val parser = createParser(options)
            val nextStatement = parser.getNextStatement() ?: break
            statements.add(nextStatement)
        }

        FileAnalyser(this.logger, statements).analyse()
        DoNothingEmitter(this.logger).emitStatements(statements)
    }


    private fun createParser(options: CompileOptions) : DefaultEulParser {
        val path = Paths.get(options.entryPoint)
        if (!Files.exists(path)) {
            throw IllegalArgumentException("${options.entryPoint}: File not found.")
        }


        if (!Files.isReadable(path)) {
            throw EulFileNotReadableException("File ${options.entryPoint} is not readable")
        }

        val reader = Files.newBufferedReader(path)
        return DefaultEulParser(reader, this.logger, options)
    }
}
