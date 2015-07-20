package com.turqmelon.Commandy.Exception;

import com.turqmelon.Commandy.Commandy;

import java.util.logging.Level;


public abstract class CommandyException extends Exception {

    private Commandy plugin;
    private int errorCode;
    private String message;

    public CommandyException(Commandy plugin, int errorCode, String message) {
        this.plugin = plugin;
        this.errorCode = errorCode;
        this.message = message;
    }

    public void sendToLogger(){
        getPlugin().getCommandyLogger().log(Level.SEVERE, getClass().getName() + " - " + getMessage() + " (Error Code: " + getErrorCode() + ")");
    }

    public Commandy getPlugin() {
        return plugin;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
