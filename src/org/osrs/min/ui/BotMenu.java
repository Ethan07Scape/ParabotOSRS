package org.osrs.min.ui;


import org.osrs.min.temp.canvas.screen.ScreenOverlay;

import javax.swing.*;
import java.util.List;

public class BotMenu {
    /**
     * Simple, but effective way of loading our debugs without causing
     * a clusterfuck of code.
     */
    private JMenu debugs = new JMenu("Debugs");

    public BotMenu(JMenuBar bar, List<ScreenOverlay> overlays) {
        JMenuItem debugger = new JMenuItem("DebugUI");
        if (overlays != null && overlays.size() > 0) {
            for (JMenuItem overlay : overlays) {
                debugs.add(overlay);
            }
        }
        debugs.add(debugger);
        bar.add(debugs);

        debugs.setEnabled(true);
    }

}
