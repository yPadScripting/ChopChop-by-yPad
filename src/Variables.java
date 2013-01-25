package ChopChop;

import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
public class Variables {

    public final static int[] LOG_IDS = {1511, 1521};
    public final static int[] HATCHET_IDS = {1351, 1349, 1353, 1361, 1355, 1357, 1358};
    public final static int[] TREE_IDS = {38760, 1282, 1286, 1289, 47600, 38785, 38760, 47598};
    public static Filter<Item> LOG_FILTER = new Filter<Item>() {

        @Override
        public boolean accept(Item t) {
            for (int i : LOG_IDS) {
                if (t.getId() == i) {
                    return true;
                }
            }
            return false;
        }
    };
    public static Filter<Item> HATCHET_FILTER = new Filter<Item>() {

        @Override
        public boolean accept(Item t) {
            for (int i : HATCHET_IDS) {
                if (t.getId() == i) {
                    return true;
                }
            }
            return false;
        }
    };
    public static Filter<SceneObject> TREE_FILTER = new Filter<SceneObject>() {

        @Override
        public boolean accept(SceneObject t) {
            for (int i : TREE_IDS) {
                if (t.getId() == i) {
                    return true;
                }
            }
            return false;
        }
    };
}
