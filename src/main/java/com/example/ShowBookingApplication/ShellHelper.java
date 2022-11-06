package com.example.ShowBookingApplication;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Value;

public class ShellHelper {

    @Value("${shell.out.info}")
    public String infoColor;

    @Value("${shell.out.success}")
    public String successColor;

    @Value("${shell.out.warning}")
    public String warningColor;

    @Value("${shell.out.error}")
    public String errorColor;

    private Terminal terminal;

    public ShellHelper(Terminal terminal) {
        this.terminal = terminal;
    }

    public String getColored(String message, PromptColor color) {
        return (new AttributedStringBuilder()).append(message, AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle())).toAnsi();
    }

    public String getInfoMessage(String message) {
        return getColored(message, PromptColor.valueOf(infoColor));
    }

    public String getErrorMessage(String message) {
        return getColored(message, PromptColor.valueOf(errorColor));
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