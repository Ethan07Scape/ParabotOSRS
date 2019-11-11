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
        return Loader.getClient().getClient_plane();
    }

    public static int getLoginState() {
        return Loader.getClient().getGameState();
    }

    public static int getGameCycle() {
        return Loader.getClient().getCycle();
    }

    public static int getLocalPlayerIndex() {
        return Loader.getClient().getLocalPlayerIndex();
    }

    public static boolean isForcingAction() {
        return forcingAction;
    }

    public static void setForcingAction(boolean forcingAction) {
        org.osrs.min.api.data.Game.forcingAction = forcingAction;
    }

    public static boolean isSpellSelected() {
        return Loader.getClient().getIsSpellSelected();
    }

    public static int getItemSelectionState() {
        return Loader.getClient().getIsItemSelected();
    }

    public static boolean isItemSelected() {
        return getItemSelectionState() != 0;
    }

    public static boolean isInInstancedScene() {
        return Loader.getClient().getIsInInstance();
    }

    public static boolean isMembersWorld() {
        return Loader.getClient().getIsMembersWorld();
    }

    public static boolean isWorldSelectorOpen() {
        return Loader.getClient().getWorldSelectOpen();
    }

    public static boolean isMenuOpen() {
        return Loader.getClient().getIsMenuOpen();
    }

    public static int getCurrentWorld() {
        return Loader.getClient().getWorldId();
    }

    public static int getCursorState() {
        return Loader.getClient().getMouseCrossState();
    }

    public static int getSelectedRegionTileX() {
        return Loader.getClient().getScene_selectedX();
    }

    public static int getSelectedRegionTileY() {
        return Loader.getClient().getScene_selectedY();
    }

    public static long getMouseIdleTime() {
        return Loader.getClient().getMouseHandler_lastMovedVolatile();
    }
}

