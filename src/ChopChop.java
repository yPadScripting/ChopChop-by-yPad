package ChopChop;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.faefafeafafeafdaef
 */
/**
 *
 * @author PimGame
 */
@Manifest(authors = {"yPad Scripting"}, name = "ChopChop by yPad", description = "Chops all around the world, just for you!")
public class ChopChop extends ActiveScript implements PaintListener, MouseListener, MessageListener {

    private Tree container = null;
    private List<Node> jobs = new ArrayList<Node>();
    private final LinkedList<ChopChop.MousePathPoint> mousePath = new LinkedList<ChopChop.MousePathPoint>();
    private boolean hide = false;
    private boolean guiWait = true;
    String currentStatus = "";
    Image background = getImage("http://i46.tinypic.com/241p891.jpg");
    Image hidebuttonenabled = getImage("http://i50.tinypic.com/a08p4x.jpg");
    Image hidebuttondisabled = getImage("http://i50.tinypic.com/16i6wz4.jpg");
    Rectangle hidebutton = new Rectangle(497, 395, 20, 20);
    Point p;
    Font font1 = new Font("Verdana", 0, 20);
    Timer runTime = new Timer(0);
    private int logs;
    private double gpmade;

    @Override
    public void messageReceived(MessageEvent me) {
        String txt = me.getMessage();
        if (txt.contains("You get some logs.")) {
            logs++;
            gpmade += Chopping.getPrice(1511);
        }
    }

    @Override
    public void onStart() {
        System.out.println("Welcome to ChopChop, the chopper made by yPad");
        System.out.println("Currently supports: Draynor Manor's normal trees!");
        System.out.println("Looking to upgrade to: more locations with more trees!");
        System.out.println("Chop away!");
    }
    
    @Override
    public void onStop() {
        System.out.println("The bot has cut " + logs + " amount of logs!");
        System.out.println("This resulted in a profit of " + gpmade + ".");
        System.out.println("Thanks for using ChopChop by yPad!");
    }

    @Override
    public int loop() {

        //while (guiWait) {
        //   sleep(500);
        //  }
        if (container != null) {
            final Node job = container.state();

            if (job != null) {
                currentStatus = job.toString();
                container.set(job);
                getContainer().submit(job);
                job.join();
            }
        } else {
            jobs.add(new Banking());
            jobs.add(new WalkToBank());
            jobs.add(new WalkToTrees());
            jobs.add(new Chopping());
            container = new Tree(jobs.toArray(new Node[jobs.size()]));
        }


        return Random.nextInt(150, 250);
    }

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        p = e.getPoint();
        if (hidebutton.contains(p) && !hide) {
            hide = true;
        } else if (hidebutton.contains(p) && hide) {
            hide = false;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SuppressWarnings("serial")
    private class MousePathPoint extends Point { // All credits to Enfilade

        private long finishTime;
        private double lastingTime;

        public MousePathPoint(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;
        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }
    }

    @Override
    public void onRepaint(Graphics g1) {

        Graphics2D g = (Graphics2D) g1;

        if (Game.getClientState() == 11) {
            if (!hide) {
                double time = (double) runTime.getElapsed();
                DecimalFormat df = new DecimalFormat("#,###,###");
                g.drawImage(background, 7, 395, null);
                g.setColor(Color.BLACK);
                g.setFont(font1);
                g.drawString("Status: " + "\n" + currentStatus, 7, 365); // Status
                g.drawString(runTime.toElapsedString(), 320, 425); //Runtime
                g.drawString(df.format(gpmade), 320, 450); //GP made
                g.drawString(df.format((3600000 / time) * gpmade), 320, 477); //GP per hour
                g.drawString(Integer.toString(logs), 320, 505); //Logs cut


                g.drawImage(hidebuttondisabled, 497, 395, null);





            }
            if (hide) {
                g.drawImage(hidebuttonenabled, 497, 395, null);
            }
        }

        //Mouse cursor
        g.setColor(Color.YELLOW);
        g.drawLine(Mouse.getX() - 5, Mouse.getY() - 5, Mouse.getX() + 5, Mouse.getY() + 5);
        g.drawLine(Mouse.getX() - 5, Mouse.getY() + 5, Mouse.getX() + 5, Mouse.getY() - 5);

        //Mouse trail
        while (!mousePath.isEmpty() && mousePath.peek().isUp()) {
            mousePath.remove();
        }
        Point clientCursor = Mouse.getLocation();
        ChopChop.MousePathPoint mpp = new ChopChop.MousePathPoint(clientCursor.x, clientCursor.y,
                200); //Lasting time/MS
        if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp)) {
            mousePath.add(mpp);
        }
        ChopChop.MousePathPoint lastPoint = null;
        for (ChopChop.MousePathPoint a : mousePath) {
            if (lastPoint != null) {
                g.setColor(Color.YELLOW);//Trail color
                g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = a;
        }
    }
}
