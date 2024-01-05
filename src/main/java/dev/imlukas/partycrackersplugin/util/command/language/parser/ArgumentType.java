package dev.imlukas.partycrackersplugin.util.command.language.parser;

public enum ArgumentType {

    PARAMETER,
    TAG,
    LIST,
    STRING;

    public boolean isLiteral() {
        return this == TAG || this == STRING;
    }
}
