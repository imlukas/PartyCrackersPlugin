package dev.imlukas.partycrackersplugin.util.command.language.type.impl;

import dev.imlukas.partycrackersplugin.util.command.language.type.ParameterType;


public class StringParameterType implements ParameterType<String> {

    @Override
    public boolean isType(String input) {
        return true;
    }

    @Override
    public String parse(String input) {
        return input;
    }

    @Override
    public String getDefaultValue() {
        return "";
    }
}
