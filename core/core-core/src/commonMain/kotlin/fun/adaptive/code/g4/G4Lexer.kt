package `fun`.adaptive.code.g4

import `fun`.adaptive.code.lexer.Lexer

object G4Lexer : Lexer() {

    val NLCHARS = fragment { charSet { "\r\n" } }
    val WSCHARS = fragment { charSet { " \t\u000C" } }
    val WSNLCHARS = fragment { charSet { " \t\n\r\u000C" } }

    val NameStartChar = fragment {
        one {
            charRange { 'A' .. 'Z' }
            charRange { 'a' .. 'z' }
            charRange { '\u00C0' .. '\u00D6' }
            charRange { '\u00D8' .. '\u00F6' }
            charRange { '\u00F8' .. '\u02FF' }
            charRange { '\u0370' .. '\u037D' }
            charRange { '\u037F' .. '\u1FFF' }
            charRange { '\u200C' .. '\u200D' }
            charRange { '\u2070' .. '\u218F' }
            charRange { '\u2C00' .. '\u2FEF' }
            charRange { '\u3001' .. '\uD7FF' }
            charRange { '\uF900' .. '\uFDCF' }
            charRange { '\uFDF0' .. '\uFEFE' }
            charRange { '\uFF00' .. '\uFFFD' }
        }
    }

    val NameChar = fragment {
        one {
            + NameStartChar
            charRange { '0' .. '9' }
            char { '_' }
            char { '\u00B7' }
            charRange { '\u0300' .. '\u036F' }
            charRange { '\u203F' .. '\u2040' }
        }
    }

    val COMMENT by token(channel = hidden) {
        all {
            char { '/' }
            one {
                all {
                    char { '/' }
                    allBefore { charSet { "\r\n" } }
                }
                all {
                    char { '*' }
                    allBefore { string { "*/" } }
                    string { "*/" }
                }
            }
        }
    }

    val LEXER_CHAR_SET by token {
        all {
            char { '[' }
            many {
                one {
                    all {
                        char { '\\' }
                        allBefore { charSet { "\r\n]" } }
                    }
                    allBefore { charSet { "\r\n\\]" } }
                }
            }
            char { ']' }
        }
    }

    val OPTIONS by token {
        all {
            string { "options" }
            + WSNLCHARS
            char { '{' }
        }
    }

    val TOKENS_SPEC by token {
        all {
            string { "tokens" }
            + WSNLCHARS
            char { '{' }
        }
    }

    val CHANNELS by token {
        all {
            string { "options" }
            + WSNLCHARS
            char { '{' }
        }
    }

    val IMPORT by token { string { "import" } }
    val FRAGMENT by token { string { "fragment" } }
    val LEXER by token { string { "lexer" } }
    val PARSER by token { string { "parser" } }
    val GRAMMAR by token { string { "grammar" } }
    val RETURNS by token { string { "returns" } }
    val LOCALS by token { string { "locals" } }
    val THROWS by token { string { "throws" } }
    val CATCH by token { string { "catch" } }
    val FINALLY by token { string { "finally" } }
    val MODE by token { string { "mode" } }

    val COLON by token { char { ':' } }
    val COLONCOLON by token { string { "::" } }
    val COMMA by token { char { ',' } }
    val SEMI by token { char { ';' } }
    val LPAREN by token { char { '(' } }
    val RPAREN by token { char { ')' } }
    val RARROW by token { string { "->" } }
    val LT by token { char { '<' } }
    val GT by token { char { '>' } }
    val ASSIGN by token { char { '=' } }
    val QUESTION by token { char { '?' } }
    val STAR by token { char { '*' } }
    val PLUS by token { char { '+' } }
    val PLUS_ASSIGN by token { string { "+=" } }
    val OR by token { char { '|' } }
    val DOLLAR by token { char { '$' } }
    val DOT by token { char { '.' } }
    val RANGE by token { string { ".." } }
    val AT by token { char { '@' } }
    val POUND by token { char { '#' } }
    val NOT by token { char { '~' } }
    val RBRACE by token { char { '}' } }

    val ID by token {
        all {
            + NameStartChar
            many { + NameChar }
        }
    }

    val INT by token {
        many { charRange { '0' .. '9' } }
    }

    val HEX_DIGIT = fragment { charSet { "0123456789abcdefABCDEF" } }

    val UNICODE_ESC = fragment {
        all {
            char { 'u' }
            many { + HEX_DIGIT }
        }
    }

    val UNICODE_EXTENDED_ESC = fragment {
        all {
            string { "u{" }
            many(min = 1, max = 6) { + HEX_DIGIT }
            char { '}' }
        }
    }

    val ESC_SEQ = fragment {
        all {
            char { '\\' }
            one {
                charSet { "btnr\"\'\\" }
                + UNICODE_ESC
                + UNICODE_EXTENDED_ESC
            }
        }
    }

    val STRING_LITERAL by token {
        all {
            char { '\'' }
            many {
                one {
                    + ESC_SEQ
                    allBefore { charSet { "\'\\" } }
                }
            }
            char { '\'' }
        }
    }

    val WS by token(channel = hidden) {
        many(min = 1) {
            + WSNLCHARS
        }
    }

    val UnicodeBOM by token(skip = true) {
        char { '\uFEFF' }
    }

}