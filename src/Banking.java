package ChopChop;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *feafa fafafeafa
 * @author PimGame
 */
public class Banking extends Node {

    public static final Area area = new Area(new Tile[]{
                new Tile(3085, 3248, 0), new Tile(3084, 3238, 0),
                new Tile(3098, 3238, 0), new Tile(3098, 3248, 0)
            });
    public static final int[] BANK_IDS = {2012, 2015, 2019};

    @Override
    public String toString() {
        return "Banking...";
    }

    @Override
    public boolean activate() {
        if (Players.getLocal() != null && inBank() && !Bank.isOpen() && !(Inventory.getCount() <= 1)) {
            System.out.println("Banking");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute() {
        final SceneObject bank = SceneEntities.getNearest(BANK_IDS);
        final Item log = Inventory.getItem(Variables.LOG_FILTER);
        if (bank.isOnScreen() && !Widgets.get(762).validate()) {
            if (Inventory.getCount(log.getId()) != 0) {
                bank.interact("Bank", "Counter");
                while (!Bank.isOpen()) {
                    sleep(500);
                }
                if (Bank.isOpen()) {
                    while (Inventory.getCount() > 1) {
                        Bank.deposit(log.getId(), 0);
                    }
                }

                Bank.close();
                Task.sleep(1000);
            }
        } else {
            Camera.turnTo(bank);
            Task.sleep(500);
        }

    }

    public static boolean inBank() {
        if (area.contains(Players.getLocal().getLocation())) {
            return true;
        } else {
            return false;
        }
    }

    public static String getWCLevel() {
        return Widgets.get(320).getChild(124).getText();
    }
}
