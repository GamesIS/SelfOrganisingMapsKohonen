package KohonenMap.gui;


import KohonenMap.Network.Data;
import KohonenMap.Network.Grid;
import KohonenMap.Network.Trainer;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


import java.awt.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public abstract class HexDrawer {
    private static final double OPACITY = 1;

    private static Pane g2;

    private static boolean border = true;

    private static Polygon[][] polygons;
    private static HashMap<Integer, Hexagon> hexagons;
    private static HashSet<StackPane> stackPane;

    private static int s;    // length of one side
    private static int t;    // short side of 30o triangle outside of each hex
    private static int r;    // radius of inscribed circle (centre to middle of each side). r= h/2
    private static int h;    // height. Distance between centres of two adjacent hexes. Distance between two opposite sides in a hex.


    public static void setProperty(Pane container, int gridWidth, int gridHeight, int height, boolean border){
        HexDrawer.g2 = container;
        polygons = new Polygon[gridWidth][gridHeight];
        hexagons = new HashMap<>();
        stackPane = new HashSet<>();

        h = height;
        //h = 26;
        r = h / 2;                       // r = radius of inscribed circle
        s = (int) (h / Math.sqrt(3));    // s = (h/2)/cos(30)= (h/2) / (sqrt(3)/2) = h / sqrt(3)
        t = (int) (r / Math.sqrt(3));    // t = (h/2) tan30 = (h/2) 1/sqrt(3) = h / (2 sqrt(3)) = r / sqrt(3)

        HexDrawer.border = border;
        if (border) {
            g2.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    public static void paint(Grid grid, int index) {
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                double weight = grid.getNode(i, j).getWeights()[index];
                drawHex(weight, i, j);
            }
        }
    }

    public static void repaint(Grid grid, int index) {
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                double weight = grid.getNode(i, j).getWeights()[index];
                polygons[i][j].setFill(getColor(weight));
            }
        }
    }

    private static Polygon hex(int x0, int y0) {

        if (s == 0  || h == 0) {
            System.out.println("ERROR: size of hex has not been set");
            return new Polygon();
        }

        int y = y0/* + borders*/;
        int x = x0/* + borders*/;

        double[] cx = new double[]{x + t, x + s + t, x + s + t + t, x + s + t, x + t, x};
        double[] cy = new double[]{y, y, y + r, y + r + r, y + r + r, y + r};

        List<Double> all = new ArrayList<>();

        all.add(cx[0]);
        all.add(cy[0]);
        all.add(cx[1]);
        all.add(cy[1]);
        all.add(cx[2]);
        all.add(cy[2]);
        all.add(cx[3]);
        all.add(cy[3]);
        all.add(cx[4]);
        all.add(cy[4]);
        all.add(cx[5]);
        all.add(cy[5]);

        Polygon hex = new Polygon();
        hex.getPoints().addAll(all);

        return hex;
    }

    private static DecimalFormat decimalFormatter;
    static {
        decimalFormatter = new DecimalFormat("####.###");
        decimalFormatter.setMinimumFractionDigits(3);
    }

    public static void increasedHex(int x0, int y0, Trainer trainer, int index) {
        Point point = pixelToHex(x0, y0);
        Hexagon hexagon = hexagons.get(Hexagon.getHash(point.x, point.y));

        if(hexagon == null) return;

        System.out.println(hexagon.i + " " + hexagon.j);

        String selectedItem = (String) ListImagesController.getController().property.getSelectionModel().getSelectedItem();
        //System.out.println(pixelToHex(x0, y0));
        double weight = 0;
        try{
            weight = trainer.getGrid().getNode(point.x, point.y).getWeights()[index];
        }
        catch (ArrayIndexOutOfBoundsException ex){

        }
        ListImagesController.getController().info.setText(selectedItem + " " + String.valueOf(Data.denormalise(weight, index)) + " i=" + point.x + " j=" + point.y);
    }

    private static void drawHex(double w, int i, int j) {

        int x = i * (s + t);
        int y = j * h + (i % 2) * h / 2;

        Polygon poly = hex(x, y);
        polygons[i][j] = poly;
        Hexagon hexagon = new Hexagon(i, j, poly);
        hexagons.put(hexagon.hashCode(), hexagon);

        //Text text = new Text ("" + i + ":" + j);
        Text text = new Text ("");
        //System.out.println("X = " + x + " Y = " + y + " i = " + i +" j = " + j + " w =" + w);
        text.setFont(new Font(7 ));
        //text.setFont(new Font(9 ));

        StackPane stack = new StackPane();
        stack.getChildren().addAll(poly, text);
        stackPane.add(stack);
        stack.setLayoutX(x);
        stack.setLayoutY(y);


        g2.getChildren().addAll(stack);

        poly.setFill(getColor(w));
    }

    static Point pixelToHex(int mx, int my) {

        Point p = new Point(-1,-1);

        //mx -= borders;
        //my -= borders;

        int x = mx / (s + t); //this gives a quick value for x. It works only on odd cols and doesn't handle the triangle sections. It assumes that the hexagon is a rectangle with width s+t (=1.5*s).
        int y = (my - (x % 2) * r) / h; //this gives the row easily. It needs to be offset by h/2 (=r)if it is in an even column

        //dx,dy are the number of pixels from the hex boundary. (ie. relative to the hex clicked in)
        int dx = mx - x * (s + t);
        int dy = my - y * h;

        if (my - (x % 2) * r < 0) return p; // prevent clicking in the open halfhexes at the top of the screen}

            if (x % 2 == 0) {    //even columns
                if (dy > r) {    //bottom half of hexes
                    if (dx * r / t < dy - r) {
                        x--;
                    }
                }
                if (dy < r) {    //top half of hexes
                    if ((t - dx) * r / t > dy) {
                        x--;
                        y--;
                    }
                }
            } else {             // odd columns
                if (dy > h) {    //bottom half of hexes
                    if (dx * r / t < dy - h) {
                        x--;
                        y++;
                    }
                }
                if (dy < h) {    //top half of hexes
                    if ((t - dx) * r / t > dy - r) {
                        x--;
                    }
                }
            }

        p.x = x;
        p.y = y;

        return p;
    }

    private static Color getColor(double d) {
        double min = 0, max = 1;

        d -= min;
        max -= min;
        min = 0;

        double maxTemp = 1.0;
        double rg, gb, r, g, b;

        double middle = min + (max - min) / 2;
        Color res = Color.WHITE;

        if (d == max)
        {
            res = new Color(maxTemp, 0, 0, OPACITY);
        }
        else if (d == middle)
        {
            res = new Color(0, maxTemp, 0, OPACITY);
        }
        else if (d == min)
        {
            res = new Color(0, 0, maxTemp, OPACITY);
        }
        else if (d < middle)
        {
            gb = d / middle;
            if (gb <= maxTemp)
            {
                r = 0;
                g = gb;
                b = maxTemp;
                res = new Color(r, g, b, OPACITY);
            }
            else
            {
                //System.out.println("test");//TODO Не доходит
                r = 0;
                g = maxTemp;
                b = maxTemp - (gb - maxTemp);
                res = new Color(r, g, b, OPACITY);
            }
        }
        else if (d > middle)
        {
            //rg = (d - middle) / middle;
            rg = d  / middle;
            if (rg <= maxTemp)
            {
                r = rg;
                g = maxTemp;
                b = 0;
                res = new Color(r, g, b, OPACITY);
            }
            else
            {
                r = maxTemp;
                g = maxTemp - (rg - maxTemp);
                b = 0;
                res = new Color(r, g, b, OPACITY);
            }
        }
        return res;
    }
}