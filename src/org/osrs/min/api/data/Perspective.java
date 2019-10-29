package org.osrs.min.api.data;

import org.osrs.min.api.accessors.Client;
import org.osrs.min.api.wrappers.Tile;
import org.osrs.min.loading.Loader;

import java.awt.*;

public class Perspective {

    /*
     * Thanks RuneLite
     */

    public static final int LOCAL_COORD_BITS = 7;
    public static final int LOCAL_TILE_SIZE = 1 << LOCAL_COORD_BITS; // 128 - size of a tile in local coordinates
    public static final int LOCAL_HALF_TILE_SIZE = LOCAL_TILE_SIZE / 2;
    public static final int SCENE_SIZE = 104; // in tiles
    public static final int[] SINE = new int[2048]; // sine angles for each of the 2048 units, * 65536 and stored as an int
    public static final int[] COSINE = new int[2048]; // cosine
    private static final double UNIT = Math.PI / 1024d; // How much of the circle each unit of SINE/COSINE is

    static {
        for (int i = 0; i < 2048; ++i) {
            SINE[i] = (int) (65536.0D * Math.sin((double) i * UNIT));
            COSINE[i] = (int) (65536.0D * Math.cos((double) i * UNIT));
        }
    }


    public static Point tileToScreen(Tile tile, double dX, double dY, int height) {
        return worldToCanvas((int) ((tile.getX() - Game.getBaseX() + dX) * 128), (int) ((tile.getY() - Game.getBaseY() + dY) * 128), height);
    }

    public static Point tileToScreen(Tile tile) {
        return worldToCanvas((int) ((tile.getX() - Game.getBaseX() + 0.5) * 128), (int) ((tile.getY() - Game.getBaseY() + 0.5) * 128), 0);
    }

    public static boolean isOffscreen(Point point) {
        Client client = Loader.getClient();
        return (point.getX() < 0 || point.getX() >= client.getViewportWidth())
                && (point.getY() < 0 || point.getY() >= client.getViewportHeight());
    }

    public static Point worldToCanvas(int x, int y, int var2) {
        final Client client = Loader.getClient();
        if (x >= 128 && y >= 128 && x <= 13056 && y <= 13056) {
            int z = getTileHeight(x, y, client.getFloorLevel()) - var2;
            x -= client.getCameraX();
            z -= client.getCameraZ();
            y -= client.getCameraY();

            int cameraPitch = client.getCameraPitch();
            int cameraYaw = client.getCameraYaw();

            int pitchSin = SINE[cameraPitch];
            int pitchCos = COSINE[cameraPitch];
            int yawSin = SINE[cameraYaw];
            int yawCos = COSINE[cameraYaw];

            int var8 = yawCos * x + y * yawSin >> 16;
            y = yawCos * y - yawSin * x >> 16;
            x = var8;
            var8 = pitchCos * z - y * pitchSin >> 16;
            y = z * pitchSin + y * pitchCos >> 16;

            if (y >= 50) {
                int pointX = client.getViewportWidth() / 2 + x * client.getViewportScale() / y;
                int pointY = var8 * client.getViewportScale() / y + client.getViewportHeight() / 2;
                return new Point(pointX, pointY);
            }
        }

        return null;

    }

    public static int getTileHeight(int x, int y, int plane) {
        final Client client = Loader.getClient();
        int var3 = x >> 7;
        int var4 = y >> 7;
        if (var3 >= 0 && var4 >= 0 && var3 <= 103 && var4 <= 103) {
            int[][][] tileHeights = client.getTileHeights();

            int var5 = plane;

            int var6 = x & 127;
            int var7 = y & 127;
            int var8 = var6 * tileHeights[var5][var3 + 1][var4] + (128 - var6) * tileHeights[var5][var3][var4] >> 7;
            int var9 = tileHeights[var5][var3][var4 + 1] * (128 - var6) + var6 * tileHeights[var5][var3 + 1][var4 + 1] >> 7;
            return (128 - var7) * var8 + var7 * var9 >> 7;
        }

        return 0;
    }


    private static int getHeight(Client client, int localX, int localY, int plane) {
        int sceneX = localX >> LOCAL_COORD_BITS;
        int sceneY = localY >> LOCAL_COORD_BITS;
        if (sceneX >= 0 && sceneY >= 0 && sceneX < SCENE_SIZE && sceneY < SCENE_SIZE) {

            int[][][] tileHeights = client.getTileHeights();

            int x = localX & (LOCAL_TILE_SIZE - 1);
            int y = localY & (LOCAL_TILE_SIZE - 1);
            int var8 = x * tileHeights[plane][sceneX + 1][sceneY] + (LOCAL_TILE_SIZE - x) * tileHeights[plane][sceneX][sceneY] >> LOCAL_COORD_BITS;
            int var9 = tileHeights[plane][sceneX][sceneY + 1] * (LOCAL_TILE_SIZE - x) + x * tileHeights[plane][sceneX + 1][sceneY + 1] >> LOCAL_COORD_BITS;
            return (LOCAL_TILE_SIZE - y) * var8 + y * var9 >> LOCAL_COORD_BITS;
        }

        return 0;
    }

}
