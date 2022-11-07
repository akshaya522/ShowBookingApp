package com.example.ShowBookingApplication;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Value;


public class ShellHelper {

    private Terminal terminal;

    public ShellHelper(Terminal terminal) {
        this.terminal = terminal;
    }

    public String getColored(String message, PromptColor color) {
        return (new AttributedStringBuilder()).append(message, AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle())).toAnsi();
    }

    public String getInfoMessage(String message) {
        return getColored(message, PromptColor.BLUE);
    }

    public String getErrorMessage(String message) {
        return getColored(message, PromptColor.RED);
    }

    public String getSuccessMessage(String message) {
        return getColored(message, PromptColor.GREEN);
    }

    public String getWarnMessage(String message) {
        return getColored(message, PromptColor.YELLOW);
    }

    public String getPromptMessage(String message) {
        return getColored(message, PromptColor.CYAN);
    }

    public void print(String message, PromptColor color) {
        String toPrint = message;
        if (color != null) {
            toPrint = getColored(message, color);
        }
        terminal.writer().println(toPrint);
        terminal.flush();
    }

    public enum PromptColor {
        BLACK(0),
        RED(1),
        GREEN(2),
        YELLOW(3),
        BLUE(4),
        MAGENTA(5),
        CYAN(6),
        WHITE(7),
        BRIGHT(8);
    
        private final int value;
    
        PromptColor(int value) {
            this.value = value;
        }
    
        public int toJlineAttributedStyle() {
            return this.value;
        }
    }
}