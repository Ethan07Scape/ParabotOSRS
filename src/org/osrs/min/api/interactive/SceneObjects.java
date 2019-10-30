package org.osrs.min.api.interactive;

import org.osrs.min.api.accessors.EntityMarker;
import org.osrs.min.api.accessors.Tile;
import org.osrs.min.api.data.Game;
import org.osrs.min.api.wrappers.SceneObject;
import org.osrs.min.loading.Loader;
import org.parabot.environment.api.utils.Filter;

import java.util.*;

public class SceneObjects {

    private static final Comparator<SceneObject> NEAREST_SORTER = Comparator.comparingInt(SceneObject::distanceTo);

    private static final Filter<SceneObject> ALL_FILTER = object -> true;


    public static final SceneObject[] getSceneObjects() {
        return getSceneObjects(ALL_FILTER);
    }

    public static final SceneObject[] getNearest(Filter<SceneObject> filter) {
        final SceneObject[] objects = getSceneObjects(filter);
        Arrays.sort(objects, NEAREST_SORTER);

        return objects;
    }

    public static final SceneObject[] getNearest() {
        return getNearest(ALL_FILTER);
    }

    public static final SceneObject[] getNearest(final int... ids) {
        return getNearest(object -> {
            for (final int id : ids) {
                if (id == object.getId()) {
                    return true;
                }
            }

            return false;
        });
    }

    public static final SceneObject[] getNearest(final String... names) {
        return getNearest(object -> {
            for (final String name : names) {
                if (name.toLowerCase().equals(object.getName().toLowerCase())) {
                    return true;
                }
            }

            return false;
        });
    }

    public static final SceneObject getClosest(final int... ids) {
        SceneObject[] nearestObjects = getNearest(object -> {
            for (final int id : ids) {
                if (id == object.getId()) {
                    return true;
                }
            }

            return false;
        });
        if (nearestObjects == null || nearestObjects.length == 0) {
            return null;
        }

        return nearestObjects[0];
    }

    public static final SceneObject getClosest(final String... names) {
        SceneObject[] nearestObjects = getNearest(object -> {
            for (final String name : names) {
                if (name.toLowerCase().equals(object.getName().toLowerCase())) {
                    return true;
                }
            }

            return false;
        });
        if (nearestObjects == null || nearestObjects.length == 0) {
            return null;
        }

        return nearestObjects[0];
    }

    private static SceneObject getSceneObjectAtTile(int x, int y) {
        final Tile sceneTile = Loader.getClient().getSceneGraph().getTiles()[Game.getPlane()][x][y];
        if (sceneTile == null) {
            return null;
        }

        EntityMarker[] interactiveObjects = sceneTile.getMarkers();
        if (interactiveObjects != null) {
            for (final EntityMarker interactiveObject : interactiveObjects) {
                if (interactiveObject != null) {
                    return new SceneObject(interactiveObject, x, y, Game.getPlane());
                }
            }
        }

        return null;
    }

    public static final SceneObject[] getSceneObjects(Filter<SceneObject> filter) {
        List<SceneObject> sceneObjects = new ArrayList<>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                final SceneObject sceneObjectAtTile = getSceneObjectAtTile(x, y);
                if (sceneObjectAtTile != null && filter.accept(sceneObjectAtTile)) {
                    sceneObjects.add(sceneObjectAtTile);
                }

            }
        }

        return sceneObjects.toArray(new SceneObject[sceneObjects.size()]);
    }

    public static final Collection<SceneObject> getSceneObjectsAtTile(int x, int y) {
        final Tile sceneTile = Loader.getClient().getSceneGraph().getTiles()[Game.getPlane()][x][y];
        List<SceneObject> sceneObjects = null;
        if (sceneTile != null) {
            EntityMarker[] interactiveObjects = sceneTile.getMarkers();
            if (interactiveObjects != null) {
                for (final EntityMarker interactiveObject : interactiveObjects) {
                    if (interactiveObject != null) {
                        if (sceneObjects == null) {
                            sceneObjects = new ArrayList<>();
                        }
                        sceneObjects.add(new SceneObject(interactiveObject, x, y, Game.getPlane()));
                    }
                }
            }
        }
        return sceneObjects;
    }
}
