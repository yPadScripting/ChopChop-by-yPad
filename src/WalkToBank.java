package ChopChop;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Tile;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
public class WalkToBank extends Node {

 /*   public final Tile[] path = {
        new Tile(3111, 3320, 0), new Tile(3112, 3314, 0),
        new Tile(3111, 3307, 0), new Tile(3109, 3300, 0),
        new Tile(3107, 3294, 0), new Tile(3108, 3287, 0),
        new Tile(3106, 3281, 0), new Tile(3104, 3275, 0),
        new Tile(3104, 3268, 0), new Tile(3104, 3261, 0),
        new Tile(3105, 3255, 0), new Tile(3103, 3248, 0),
        new Tile(3098, 3250, 0), new Tile(Random.nextInt(3093,3096), 3243, 0)
    };*/
    
    public final Tile[] path = {
		new Tile(3099, 3322, 0), new Tile(3095, 3313, 0),
		new Tile(3105, 3309, 0), new Tile(3116, 3310, 0),
		new Tile(3127, 3312, 0), new Tile(3129, 3322, 0),
		new Tile(3115, 3322, 0), new Tile(3110, 3317, 0),
		new Tile(3110, 3303, 0), new Tile(3111, 3293, 0),
		new Tile(3109, 3284, 0), new Tile(3104, 3275, 0),
		new Tile(3105, 3267, 0), new Tile(3104, 3260, 0),
		new Tile(3104, 3250, 0), new Tile(3097, 3249, 0),
		new Tile(3094, 3244, 0) 
};

        @Override
    public String toString() {
        return "Walking to the bank...";
    }
    
    @Override
    public boolean activate() {
        if (Players.getLocal() != null && Inventory.isFull() && !Banking.inBank()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute() {


        while (!Banking.inBank()) {
            Walking.newTilePath(path).traverse();
            Task.sleep(1000);
            if (Walking.getEnergy() > 15 && !Walking.isRunEnabled()) {
                Walking.setRun(true);
            }
            
        }
        Widgets.get(548).getChild(109).click(true);
        Task.sleep(500);
        Widgets.get(548).getChild(111).click(true);
        Task.sleep(500);

    }
}
