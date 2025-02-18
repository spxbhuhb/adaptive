package `fun`.adaptive.code.g4

import `fun`.adaptive.code.parser.Parser
import `fun`.adaptive.code.parser.ParserEntryDef

object G4Parser : Parser() {

    override fun entryPoint(): ParserEntryDef =
        rules

    val rules by entry {
        many {
            + ruleSpec
        }
    }

    val ruleSpec by entry {
        + lexerRuleSpec
    }

    val optionsSpec by entry {

    }

    val lexerRuleSpec by entry {
        optional { + G4Lexer.FRAGMENT }
        + G4Lexer.ID
        optional { + optionsSpec }
        + G4Lexer.COLON
        + lexerRuleBlock
        + G4Lexer.SEMI
    }

    val lexerRuleBlock by entry {
        + lexerAltList
    }

    val lexerAltList : ParserEntryDef by entry {
        + lexerAlt
        many {
            + G4Lexer.OR
            + lexerAlt
        }
    }

    val lexerAlt by entry {
        + lexerElements
        optional { + lexerCommands }
    }

    val lexerElements by entry {
        many(min = 1) { + lexerElement }
    }

    val lexerElement by entry {
        one {
            all {
                + lexerAtom
                optional { + ebnfSuffix }
            }
            all {
                + lexerBlock
                optional { + ebnfSuffix }
            }
        }
    }

    val lexerBlock by entry {
        + G4Lexer.LPAREN
        + lexerAltList
        + G4Lexer.RPAREN
    }

    val lexerCommands by entry {
        + G4Lexer.RARROW
        + lexerCommand
        many {
            + G4Lexer.COMMA
            + lexerCommand
        }
    }

    val lexerCommand by entry {
        one {
            all {
                + lexerCommandName
                + G4Lexer.LPAREN
                + lexerCommandExpr
                + G4Lexer.RPAREN
            }
            + lexerCommandName
        }
    }

    val lexerCommandName by entry {
        one {
            + G4Lexer.ID
            + G4Lexer.MODE
        }
    }

    val lexerCommandExpr by entry {
        one {
            + G4Lexer.ID
            + G4Lexer.INT
        }
    }

    val ebnfSuffix by entry {
        one {
            + G4Lexer.QUESTION
            + G4Lexer.PLUS
            + G4Lexer.STAR
        }
    }

    val lexerAtom by entry {
        one {
            + characterRange
            //+ terminalDef
            //+ notSet
            + G4Lexer.LEXER_CHAR_SET
            all {
                + G4Lexer.DOT
                //optional { + elementOptions }
            }
        }
    }

    val characterRange by entry {
        + G4Lexer.STRING_LITERAL
        + G4Lexer.RANGE
        + G4Lexer.STRING_LITERAL
    }

    val identifier by entry {

    }
}