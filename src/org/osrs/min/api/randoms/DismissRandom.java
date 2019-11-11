package org.osrs.min.api.randoms;

import org.osrs.min.api.data.Game;
import org.osrs.min.api.interactive.Npcs;
import org.osrs.min.api.interactive.Players;
import org.osrs.min.api.wrappers.NPC;
import org.osrs.min.utils.Utils;
import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.randoms.Random;
import org.parabot.environment.randoms.RandomType;

public class DismissRandom implements Random {

    @Override
    public boolean activate() {
        return needsDismissal();
    }

    @Override
    public void execute() {
        Logger.addMessage("Dismissing random event.");

        final NPC npc = getRandomNpc();

        if (npc != null) {
            npc.interact("Dismiss");
            Time.sleep(() -> getRandomNpc() == null, 4500);
        }
    }

    @Override
    public String getName() {
        return "DismissRandom";
    }

    @Override
    public String getServer() {
        return "OSRS";
    }

    @Override
    public RandomType getRandomType() {
        return RandomType.SCRIPT;
    }

    private NPC getRandomNpc() {
        return Npcs.getClosest(n -> n != null && Players.getMyPlayer() != null
                && n.isInteracting()
                && n.getTarget().equals(Players.getMyPlayer())
                && Utils.getInstance().inArray("Dismiss", n.getActions()));
    }

    private boolean needsDismissal() {
        if (!Game.isLoggedIn())
            return false;
        final NPC npc = Npcs.getClosest(n -> n != null && Players.getMyPlayer() != null
                && n.isInteracting()
                && n.getTarget().equals(Players.getMyPlayer())
                && Utils.getInstance().inArray("Dismiss", n.getActions()));
        return npc != null;
    }

}
