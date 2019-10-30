package org.osrs.min.canvas;


import org.osrs.min.canvas.listeners.PaintListener;
import org.osrs.min.canvas.screen.ScreenOverlay;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;


public class RSCanvas extends Canvas {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 765, HEIGHT = 503;
    protected BufferedImage clientBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    protected BufferedImage gameBuffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    protected List<PaintListener> listeners = new ArrayList<>();

    public RSCanvas() {
        super();
    }

    @Override
    public Graphics getGraphics() {
        try {
            final Graphics graphics = clientBuffer.getGraphics();
            if (graphics == null || gameBuffer == null) {
                if (super.getGraphics() != null) {
                    return super.getGraphics();
                }
            }
            graphics.drawImage(gameBuffer, 0, 0, null);
            try {
                if (getPaintListeners() != null && getPaintListeners().size() > 0) {
                    for (Iterator<PaintListener> paintListenerIterator = getPaintListeners().iterator(); paintListenerIterator.hasNext(); ) {
                        PaintListener listener = paintListenerIterator.next();
                        if (listener instanceof ScreenOverlay) {
                            final ScreenOverlay<?> overlay = (ScreenOverlay<?>) listener;
                            if (overlay.activate()) {
                                overlay.render((Graphics2D) graphics);
                            }
                        } else {
                            listener.render((Graphics2D) graphics);
                        }
                    }
                }
            } catch (ConcurrentModificationException e) {

            }
            graphics.dispose();

            final Graphics2D rend = (Graphics2D) super.getGraphics();
            if (clientBuffer != null && gameBuffer != null && rend != null) {
                rend.drawImage(clientBuffer, 0, 0, null);
            }
            if (gameBuffer != null && gameBuffer.getGraphics() != null) {
                return gameBuffer.getGraphics();
            } else {
                if (super.getGraphics() != null) {
                    return super.getGraphics();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (super.getGraphics() != null) {
            return super.getGraphics();
        } else {
            return null;
        }
    }

    public synchronized List<PaintListener> getPaintListeners() {
        return listeners;
    }

    public void setOverlays(List<ScreenOverlay> overlaysList) {
        try {
            if (overlaysList == null || overlaysList.size() <= 0) {
                System.out.println("NONE");
                return;
            }
            listeners = new ArrayList<>();
            ScreenOverlay[] overlays = overlaysList.toArray(new ScreenOverlay[overlaysList.size()]);
            synchronized (listeners) {
                Collections.addAll(listeners, overlays);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}