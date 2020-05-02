/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.ast.impl.antlr4;

import java.util.Arrays;
import java.util.stream.Stream;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Stores the XPath name of antlr terminals. I found no simple way to
 * give names to punctuation (we could add a lexer rule, but it may
 * conflict with other tokens). So their names are hardcoded here.
 *
 * <p>Terminal names start with {@code "T-"} in XPath to avoid conflicts
 * with other stuff.
 */
public class AntlrNameDictionary {

    private final String[] terminalXPathNames;
    private final String[] nonTermXpathNames;
    private final Vocabulary vocabulary;

    public AntlrNameDictionary(Vocabulary vocab, String[] ruleNames) {
        this.vocabulary = vocab;

        nonTermXpathNames = new String[ruleNames.length];
        for (int i = 0; i < nonTermXpathNames.length; i++) {
            nonTermXpathNames[i] = StringUtils.capitalize(ruleNames[i]);
        }

        // terminal names
        terminalXPathNames = new String[vocab.getMaxTokenType()];
        for (int i = 0; i < terminalXPathNames.length; i++) {
            String name = vocab.getSymbolicName(i);


            if (name == null) {
                name = vocab.getLiteralName(i);

                if (name != null) {
                    // cleanup literal name, Antlr surrounds the image with single quotes
                    name = name.substring(1, name.length() - 1);

                    if (!StringUtils.isAlphanumeric(name)) {
                        name = maybePunctName(name);
                    } // otherwise something like "final"
                }
            }


            assert name != null : "Token of kind " + i + " has no XPath name (literal " + vocab.getLiteralName(i) + ")";

            String finalName = "T-" + name;

            assert finalName.matches("[a-zA-Z][\\w_-]+"); // must be a valid xpath name

            terminalXPathNames[i] = finalName;
        }


        assert Stream.of(terminalXPathNames).distinct().count() == terminalXPathNames.length
            : "Duplicate names in " + Arrays.toString(terminalXPathNames);
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    protected @Nullable String maybePunctName(String s) {
        // these are hardcoded, but it's overridable
        // here we try to avoid semantic overtones, because
        // a-priori the same terminal may mean several things
        // in different contexts.
        switch (s) {
        case "!": return "bang";
        case "!!": return "double-bang";

        case "?": return "question";
        case "??": return "double-question";
        case "?:": return "elvis";
        case "?.": return "question-dot";

        case ":": return "colon";
        case ";": return "semi";
        case ",": return "comma";

        case "(": return "lparen";
        case ")": return "rparen";
        case "[": return "lbracket";
        case "]": return "rbracket";
        case "{": return "lbrace";
        case "}": return "rbrace";

        case "_": return "underscore";

        case ".": return "dot";
        case "..": return "double-dot";
        case "...": return "ellipsis";

        case "@": return "at-symbol";
        case "$": return "dollar";
        case "&": return "amp";

        case "\\": return "backslash";
        case "/": return "slash";
        case "//": return "double-slash";
        case "`": return "backtick";
        case "'": return "squote";
        case "\"": return "dquote";

        case ">": return "gt";
        case ">=": return "ge";
        case "<": return "lt";
        case "<=": return "le";

        case ">>": return "double-gt";
        case "<<": return "double-lt";
        case ">>>": return "triple-gt";
        case "<<<": return "triple-lt";

        case "=": return "eq";
        case "==": return "double-eq";
        case "===": return "triple-eq";
        case "!=": return "not-eq";

        case "*": return "star";
        case "**": return "double-star";

        case "+": return "plus";
        case "++": return "double-plus";
        case "-": return "minus";
        case "--": return "double-minus";

        case "->": return "rarrow";
        case "<-": return "larrow";

        default:
            return null;
        }
    }

    /**
     * Gets the xpath name of a terminal node with a given {@link Token#getType()}.
     *
     * @throws IllegalArgumentException If the index is invalid
     */
    public @NonNull String getXPathNameOfToken(int tokenType) {
        if (tokenType >= 0 && tokenType < terminalXPathNames.length) {
            return terminalXPathNames[tokenType];
        }

        if (tokenType == Token.EOF) {
            return "EOF";
        }

        throw new IllegalArgumentException("I don't know token type " + tokenType);
    }

    /**
     * Gets the xpath name of an inner node with a given {@link ParserRuleContext#getRuleIndex()}.
     *
     * @throws IndexOutOfBoundsException If the index is invalid
     */
    public @NonNull String getXPathNameOfRule(int idx) {
        return nonTermXpathNames[idx];
    }

    public int getMaxRuleIndex() {
        return nonTermXpathNames.length;
    }

    public int getMaxTokenType() {
        return vocabulary.getMaxTokenType();
    }
}
