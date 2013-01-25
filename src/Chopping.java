package ChopChop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
public class Chopping extends Node {

    public static int logs = 0;
    public static double gpmade = 0;
    public final static Area area = new Area(new Tile[]{
                new Tile(3090, 3297, 0), new Tile(3128, 3297, 0),
                new Tile(3119, 3327, 0), new Tile(3092, 3327, 0)
            });

    @Override
    public String toString() {
        return "Chopping wood...";
    }

    @Override
    public boolean activate() {
        if (Players.getLocal() != null && !Inventory.isFull() && atTrees() && Players.getLocal().isIdle()) {

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute() {
        final SceneObject tree = SceneEntities.getNearest(Variables.TREE_FILTER);
        final int invcount = Inventory.getCount();

        if (Walking.getEnergy() > 15 && !Walking.isRunEnabled()) {
            Walking.setRun(true);
        }

        if (tree.isOnScreen()) {
            if (isReachable(tree.getLocation())) {
                tree.interact("Chop down");
                while (!Players.getLocal().isIdle()) {
                    sleep(150);
                }
                Task.sleep(500);
                if (invcount != Inventory.getCount()) {
                    logs += 1;
                    gpmade += getPrice(1511);
                }


            } else {
                Tile walktile = Players.getLocal().getLocation().randomize(2, 2);
                Walking.walk(walktile);
                Task.sleep(500);
            }

        } else {
            Walking.walk(tree);
            Task.sleep(500);
            Camera.turnTo(tree);
            Task.sleep(500);
        }
    }

    public static boolean atTrees() {
        if (area.contains(Players.getLocal().getLocation())) {
            return true;
        } else {
            return false;
        }
    }

    public static int getPrice(int itemID) {
        try {
            final URL url = new URL("http://open.tip.it/json/ge_single_item?item=" + itemID);
            URLConnection con = url.openConnection();
            final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                line += inputLine;
                if (inputLine.contains("mark_price")) {
                    line = line.substring(line.indexOf("mark_price\":\"")
                            + "mark_price\":\"".length());
                    line = line.substring(0, line.indexOf("\""));
                    return Integer.parseInt(line.replaceAll(",", ""));
                }
            }
            in.close();
        } catch (Exception e) {
        }
        return -1;
    }

    public static double getGpMade() {
        return gpmade;
    }

    public static int getLogs() {
        return logs;
    }

    public void messageReceived1(MessageEvent e) {
        String txt = e.getMessage();
        if (txt.contains("You have completed the Task")) {
            logs++;
        }
    }

    private Tile[] createArea(Tile center, int width, int height) {
        ArrayList<Tile> treearea = new ArrayList<Tile>();

        for (int w = (-width); w <= width; w++) {
            for (int h = (-height); h <= height; h++) {
                treearea.add(new Tile(center.getX() + a, center.getY() + b, center.getPlane()));
            }
        }

        treearea.remove(new Tile(center.getX(), center.getY(), center.getPlane()));

        return treearea.toArray(new Tile[treearea.size()]);
    }

    private boolean isReachable(Tile center) {
        for (int i = 01; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (center.getLocation().derive(i, j).canReach()) {
                    return true;
                }
            }
        }
        return false;
    }
}
