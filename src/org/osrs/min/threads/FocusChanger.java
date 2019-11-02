package org.osrs.min.threads;

import org.parabot.api.calculations.Random;
import org.parabot.core.Context;
import org.parabot.environment.api.utils.Time;

import java.lang.reflect.Field;

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

    private String focusClass;

    private String focusField;

    private boolean lastSet;

    public void run() {
        while (true) {

            Time.sleep(Random.between(2000, 3000));

            if (!needsRandomFocusChange())
                continue;

            if (focusClass == null || focusField == null)
                continue;

            try {

                final Class<?> c = Context.getInstance().getASMClassLoader().loadClass(focusClass);
                if (c != null) {
                    final Field f = c.getDeclaredField(focusField);
                    if (f != null) {
                        lastSet = !lastSet;
                        System.out.println("Setting focus: " + lastSet);
                        f.setAccessible(true);
                        f.set(null, lastSet);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final boolean needsRandomFocusChange() {
        if ((System.currentTimeMillis() & 1 << 18) > 0) {
            return Random.between(1, 10) == 1;
        }
        return Random.between(1, 3) == 1;
    }

    public void setFocusClass(String focusClass) {
        this.focusClass = focusClass;
    }

    public void setFocusField(String focusField) {
        this.focusField = focusField;
    }
}
//ItemDef error...
//Error: hz.a() bx.al() bx.n() | | java.lang.NullPointerException | 278 509 277 50