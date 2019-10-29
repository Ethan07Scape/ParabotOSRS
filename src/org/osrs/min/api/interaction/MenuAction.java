package org.osrs.min.api.interaction;


public class MenuAction {
    private String action;
    private String target;
    private int opcode;
    private int tertiaryArg;
    private int secondaryArg;
    private int primaryArg;

    public MenuAction(String action, String target, int opcode, int primaryArg, int secondaryArg, int tertiaryArg) {
        this.action = action;
        this.target = target;
        this.opcode = opcode;
        this.primaryArg = primaryArg;
        this.secondaryArg = secondaryArg;
        this.tertiaryArg = tertiaryArg;
    }

    public String getTarget() {
        return this.target;
    }

    public int getPrimaryArg() {
        return this.primaryArg;
    }

    public String getAction() {
        return this.action;
    }

    public int getTertiaryArg() {
        return this.tertiaryArg;
    }

    public int getSecondaryArg() {
        return this.secondaryArg;
    }

    public int getOpcode() {
        return this.opcode;
    }
}
