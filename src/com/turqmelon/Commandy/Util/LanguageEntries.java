package com.turqmelon.Commandy.Util;


public enum LanguageEntries {

    COMMAND_MUST_BE_PLAYER("&cYou must be a player to use that!"),
    COMMAND_INSUFFICIENT_ARGS("&cIncorrect Usage"),
    COMMAND_USAGE_EXAMPLE("&cTry this: &f{0}"),

    INSUFFICIENT_PERMISSION("&cI asked a fairy if you could do that, the fairy got mad."),
    TARGET_NOT_FOUND("&cTarget either doesn't exist or isn't online!"),
    MATERIAL_NOT_FOUND("&cItem not found!"),
    EMPTY_HAND("&cNothing in hand!"),
    INVALID_INTEGER("&cThe number you provided is not valid!"),
    INVALID_STACK_SIZE("&cThe stack size must be at least 1, but no larger than {0}."),

    GIVE_COMMAND_GIVEN("&aYou were given &f{0}&a x&f{1}&a!"),
    GIVE_COMMAND_GIVEN_FROM("&aYou were given &f{0}&a x&f{1}&a by &f{2}&a!"),
    GIVE_COMMAND_GIVEN_TO("&aYou gave &f{0}&a x&f{1}&a to &f{2}&a!"),

    WHO_COMMAND_HEADER("&eThere are &f{0}&e out of &f{1}&e players online."),
    WHO_COMMAND_LIST("&ePlayers:&f {0}"),

    MORE_COMMAND_ALL("&aYour inventory has been maxed out!"),
    MORE_COMMAND_HAND("&aThere you go!"),

    LANGUAGE_ENTRIES_LOADED("Successfully loaded {0} language entries!"),
    LANGUAGE_ENTRIES_UPDATED("Successfully updated {0} language entries!");

    private String message;

    LanguageEntries(String message) {
        this.message = message;
    }

    public LanguageEntries getEntry(String key){
        for(LanguageEntries entry : LanguageEntries.values()){
            if (entry.name().equalsIgnoreCase(key)){
                return entry;
            }
        }
        return null;
    }

    public String getKey(){
        return name().toLowerCase();
    }

    public String getMessage() {
        return message;
    }
}
