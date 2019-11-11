package org.osrs.min.threads;

import org.osrs.min.api.data.Game;
import org.osrs.min.api.packets.outgoing.PacketWriter;
import org.parabot.api.calculations.Random;
import org.parabot.environment.api.utils.Time;

/**
 * In here we constantly change the focus of the client (in the background, so you won't notice anything).
 * <p>
 * This is an attempt to throw off Jagex's bot detection system.
 * <p>
 * A packet gets sent to the OSRS server every time focus is gained/lost. (rev.185 - ClientPacket (10, 1)).
 * <p>
 * We're basically just sending the packet at random times, to simulate focus gain/loss.
 */

public class FocusChanger extends Thread {

    public void run() {
        while (true) {

            Time.sleep(Random.between(1500, 2000));
            if (!Game.isLoggedIn())
                continue;
            if (PacketWriter.getInstance() == null)
                continue;
            if (!needsRandomFocusChange())
                continue;

            PacketWriter.getInstance().sendRandomFocus();
        }
    }

    private final boolean needsRandomFocusChange() {
        if ((System.currentTimeMillis() & 1 << 18) > 0) {
            return Random.between(1, 10) == 1;
        }
        return Random.between(1, 4) == 1;
    }

}
