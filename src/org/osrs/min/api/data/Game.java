package org.osrs.min.api.data;

import org.osrs.min.loading.Loader;

public class Game {

    public static final int STATE_LOGGED_IN = 30;
    public static final int STATE_LOG_IN_SCREEN = 10;
    public static final int CONNECTING = 20;

    private static boolean forcingAction = false;

    public static boolean isLoggedIn() {
        return getLoginState() == 30;
    }

    public static int getBaseX() {
        return Loader.getClient().getBaseX();
    }

    public static int getBaseY() {
        return Loader.getClient().getBaseY();
    }

    public static int getPlane() {
        return Loader.getClient().getFloorLevel();
    }

    public static int getLoginState() {
        return Loader.getClient().getGameState();
    }

    public static int getGameCycle() {
        return Loader.getClient().getEngineCycle();
    }

    public static int getLocalPlayerIndex() {
        return Loader.getClient().getPlayerIndex();
    }

    public static boolean isForcingAction() {
        return forcingAction;
    }

    public static void setForcingAction(boolean forcingAction) {
        Game.forcingAction = forcingAction;
    }

    public static boolean isSpellSelected() {
        return Loader.getClient().getSpellSelected();
    }

    public static int getItemSelectionState() {
        return Loader.getClient().getItemSelectionState();
    }

    public static boolean isItemSelected() {
        return getItemSelectionState() != 0;
    }

    public static boolean isInInstancedScene() {
        return Loader.getClient().getInInstancedScene();
    }

    public static boolean isMembersWorld() {
        return Loader.getClient().getMembersWorld();
    }

    public static boolean isWorldSelectorOpen() {
        return Loader.getClient().getLoginWorldSelectorOpen();
    }

    public static boolean isMenuOpen() {
        return Loader.getClient().getMenuOpen();
    }

    public static int getCurrentWorld() {
        return Loader.getClient().getCurrentWorld();
    }

    public static int getCursorState() {
        return Loader.getClient().getCursorState();
    }

    public static int getSelectedRegionTileX() {
        return Loader.getClient().getSelectedRegionTileX();
    }

    public static int getSelectedRegionTileY() {
        return Loader.getClient().getSelectedRegionTileY();
    }

    public static long getMouseIdleTime() {
        return Loader.getClient().getMouseMoveTime();
    }
}

