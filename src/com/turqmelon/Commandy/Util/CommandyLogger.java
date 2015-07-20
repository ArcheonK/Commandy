package com.turqmelon.Commandy.Util;

import com.turqmelon.Commandy.Commandy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class CommandyLogger {

    private Commandy plugin;

    private boolean logToConsole = true;
    private int minimumConsoleLog = 1;

    public int getMinimumConsoleLog() {
        return minimumConsoleLog;
    }

    public void setMinimumConsoleLog(int minimumConsoleLog) {
        this.minimumConsoleLog = minimumConsoleLog;
    }

    public boolean isLogToConsole() {
        return logToConsole;
    }

    public void setLogToConsole(boolean logToConsole) {
        this.logToConsole = logToConsole;
    }

    private List<LogEntry> logged = new ArrayList<>();

    public List<LogEntry> getLogged() {
        return logged;
    }

    public CommandyLogger(Commandy plugin) {
        this.plugin = plugin;
    }

    public void log(Level level, String message){
        int levelValue = getValue(level);
        if (levelValue==0)return;
        if (isLogToConsole()&&levelValue>=getMinimumConsoleLog()){
            System.out.println("[" + level.getName().toUpperCase() + "] [Commandy] " + message);
        }
        getLogged().add(new LogEntry(System.currentTimeMillis(), level, message));
    }

    private int getValue(Level level){
        if (level==Level.INFO){
            return 1;
        }
        else if (level==Level.WARNING){
            return 2;
        }
        else if (level==Level.SEVERE){
            return 3;
        }
        return 0;
    }

    public Commandy getPlugin() {
        return plugin;
    }

    public static class LogEntry {
        private long timestamp;
        private Level level;
        private String message;

        public LogEntry(long timestamp, Level level, String message) {
            this.timestamp = timestamp;
            this.level = level;
            this.message = message;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public Level getLevel() {
            return level;
        }

        public String getMessage() {
            return message;
        }
    }

}
