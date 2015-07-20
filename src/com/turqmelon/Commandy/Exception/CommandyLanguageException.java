package com.turqmelon.Commandy.Exception;


import com.turqmelon.Commandy.Commandy;

public class CommandyLanguageException extends CommandyException {

    public CommandyLanguageException(Commandy plugin, int errorCode, String message) {
        super(plugin, errorCode+100, message);
    }
}
