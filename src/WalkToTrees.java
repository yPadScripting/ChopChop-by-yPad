
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Equipment;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.bot.Context;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
public class WalkToTrees extends Node {

    public final Tile[] path = {
        new Tile(3092, 3244, 0), new Tile(3098, 3250, 0),
        new Tile(3104, 3249, 0), new Tile(3104, 3257, 0),
        new Tile(3103, 3267, 0), new Tile(3104, 3274, 0),
        new Tile(3105, 3281, 0), new Tile(3108, 3283, 0),
        new Tile(3108, 3293, 0), new Tile(3110, 3300, 0),
        new Tile(3112, 3306, 0), new Tile(3104, 3308, 0),
        new Tile(3096, 3311, 0), new Tile(3094, 3316, 0),
        new Tile(3099, 3320, 0), new Tile(3110, 3319, 0),
        new Tile(3118, 3318, 0), new Tile(3122, 3314, 0),
        new Tile(3123, 3307, 0), new Tile(3112, 3306, 0),
        new Tile(3112, 3314, 0)
    };

    @Override
    public String toString() {
        return "Walking to the trees...";
    }

    @Override
    public boolean activate() {
        if (Inventory.contains(Variables.HATCHET_IDS) || Equipment.containsOneOf(Variables.HATCHET_IDS)) {
            if (Players.getLocal() != null && !Chopping.atTrees() && !Inventory.isFull()) {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Hatchet not found; start script with an axe in inventory or equipped!");
            Context.get().getScriptHandler().stop();
            return false;
        }
    }

    @Override
    public void execute() {
        Widgets.get(548).getChild(109).click(true);
        Task.sleep(500);
        Widgets.get(548).getChild(111).click(true);
        Task.sleep(500);
        while (!Chopping.atTrees()) {
            Walking.newTilePath(path).traverse();
            if (Walking.getEnergy() > 15 && !Walking.isRunEnabled()) {
                Walking.setRun(true);
            }
            Task.sleep(Random.nextInt(600, 900));
        }
    }
}
