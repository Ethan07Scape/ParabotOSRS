package org.osrs.min.api.interfaces;


public interface Interactable {

    boolean interact(final String action);

    String[] getActions();

}