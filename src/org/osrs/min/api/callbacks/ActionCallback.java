package org.osrs.min.api.callbacks;

public class ActionCallback {

    private static boolean debug = true;

    public static void add(int seconday, int tet, int op, int primary, int o1, int o2, String s1) {
        if (debug) {
            System.out.println("String: " + s1 + " : Secondary: " + seconday + " Tet: " + tet + " Opcode: " + op + " Primary: " + primary + " Other: " + o1 + " Other: " + o2);
        }
    }


}