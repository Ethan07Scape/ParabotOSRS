package org.osrs.min.ui;


import org.osrs.min.canvas.screen.ScreenOverlay;
import org.osrs.min.ui.debugger.DebuggerUI;

import javax.swing.*;
import java.util.List;

public class BotMenu {
    /**
     * Simple, but effective way of loading our debugs without causing
     * a clusterfuck of code.
     */
    private JMenu debugs = new JMenu("Debugs");
    private DebuggerUI debuggerUI;

    public BotMenu(JMenuBar bar, List<ScreenOverlay> overlays) {
        JMenuItem widgetViewer = new JMenuItem("Widgets");
        JMenuItem debugger = new JMenuItem("DebugUI");
        debugger.addActionListener(e -> loadDebugger());
        widgetViewer.addActionListener(e -> new WidgetViewer());
        if (overlays != null && overlays.size() > 0) {
            for (JMenuItem overlay : overlays) {
                debugs.add(overlay);
            }
        }
        debugs.add(widgetViewer);

        debugs.add(debugger);
        bar.add(debugs);
        debugs.setEnabled(true);
    }

    private void loadDebugger() {
        if (debuggerUI == null) {
            debuggerUI = new DebuggerUI();
        } else {
            debuggerUI.setVisible();
        }
    }
}
