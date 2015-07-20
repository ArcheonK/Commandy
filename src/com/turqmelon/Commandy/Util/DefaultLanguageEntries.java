package com.turqmelon.Commandy.Util;


public enum DefaultLanguageEntries {

    COMMAND_MUST_BE_PLAYER("&cYou must be a player to use that!"),
    COMMAND_INSUFFICIENT_ARGS("&cIncorrect Usage"),
    COMMAND_USAGE_EXAMPLE("&cTry this: &f{0}"),

    INSUFFICIENT_PERMISSION("&cI asked a fairy if you could do that, the fairy got mad."),

    LANGUAGE_ENTRIES_LOADED("Successfully loaded {0} language entries!"),
    LANGUAGE_ENTRIES_UPDATED("Successfully updated {0} language entries!");

    private String message;

    DefaultLanguageEntries(String message) {
        this.message = message;
    }

    public DefaultLanguageEntries getEntry(String key){
        for(DefaultLanguageEntries entry : DefaultLanguageEntries.values()){
            if (entry.name().equalsIgnoreCase(key)){
                return entry;
            }
        }
        return null;
    }

    public String getMessage() {
        return message;
    }
}
