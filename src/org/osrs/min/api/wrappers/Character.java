package org.osrs.min.api.wrappers;

import org.osrs.min.api.accessors.Npc;
import org.osrs.min.api.accessors.PathingEntity;
import org.osrs.min.api.data.Calculations;
import org.osrs.min.api.data.Game;
import org.osrs.min.api.interaction.InteractionHandler;
import org.osrs.min.api.interactive.Players;
import org.osrs.min.api.interfaces.Interactable;
import org.osrs.min.api.interfaces.Locatable;
import org.osrs.min.loading.Loader;

import java.awt.*;

public class Character implements Locatable, Interactable {
    private final PathingEntity accessor;
    private final int index;
    private final InteractionHandler interactionHandler;

    public Character(int index, PathingEntity accessor) {
        this.index = index;
        this.accessor = accessor;
        this.interactionHandler = new InteractionHandler(this);
    }

    public final int getX() {
        return ((getLocalX() >> 7) + Game.getBaseX());
    }

    public final int getY() {
        return ((getLocalY() >> 7) + Game.getBaseY());
    }

    public final int getLocalX() {
        return accessor.getFineX();
    }

    public final int getLocalY() {
        return accessor.getFineY();
    }

    public final int getAnimation() {
        return accessor.getAnimation();
    }

    public final int getOrientation() {
        return accessor.getOrientation();
    }

    public final int getPathSize() {
        return accessor.getPathQueueSize();
    }

    public final int getTargetIndex() {
        return accessor.getTargetIndex();
    }

    public final String getOverheadText() {
        return accessor.getOverheadText();
    }

    public final boolean isMoving() {
        return getPathSize() > 0;
    }

    public final boolean isAnimating() {
        return getAnimation() != -1;
    }

    public int[] getHitsplatCycles() {
        return this.accessor.getHitsplatCycles();
    }

    public int[] getHitsplats() {
        return this.accessor.getHitsplats();
    }

    public boolean isInCombat() {
        if (accessor == null)
            return false;
        int loopCycleStatus = Loader.getClient().getEngineCycle() - 130;
        int[] hitCycles = this.getHitsplatCycles();
        for (final int loopCycle : hitCycles) {
            if (loopCycle > loopCycleStatus) {
                return true;
            }
        }
        return false;
    }

    public final org.osrs.min.api.wrappers.Character getTarget() {
        final int index = getTargetIndex();
        if (index == -1) {
            return null;
        }
        if (index < 32768) {
            Npc[] localNpcs = Loader.getClient().getNpcs();
            return index >= 0 && index < localNpcs.length ? new NPC(index, localNpcs[index]) : null;
        } else {
            final int pos = index - 32768;
            final int localIndex = Loader.getClient().getPlayerIndex();
            if (pos == localIndex) {
                return Players.getMyPlayer();
            }

            final org.osrs.min.api.accessors.Player[] players = Loader.getClient().getPlayers();
            return pos >= 0 && pos < players.length ? new Player(index, players[index]) : null;
        }
    }

    public boolean isInteracting() {
        return this.getTarget() != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        org.osrs.min.api.wrappers.Character other = (org.osrs.min.api.wrappers.Character) obj;
        if (accessor == null) {
            if (other.accessor != null) {
                return false;
            }
        } else if (!accessor.equals(other.accessor)) {
            return false;
        }

        return index == other.index;
    }

    @Override
    public int distanceTo() {
        return Calculations.distanceTo(getLocation());
    }

    @Override
    public int distanceTo(Locatable locatable) {
        return Calculations.distanceBetween(getLocation(), locatable.getLocation());
    }

    @Override
    public int distanceTo(Tile tile) {
        return Calculations.distanceBetween(getLocation(), tile);
    }

    @Override
    public Tile getLocation() {
        return new Tile(getX(), getY());
    }

    @Override
    public boolean canReach() {
        return false;
    }

    @Override
    public boolean interact(String action) {
        return interactionHandler.interact(action);
    }

    public boolean interact(int opcode) {
        return interactionHandler.interact(opcode);
    }

    @Override
    public String[] getActions() {
        return new String[0];
    }

    public void drawTile(Graphics2D g, Color color) {
        getLocation().draw(g, color);
    }

    protected PathingEntity getAccessor() {
        return accessor;
    }
}
