import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import processing.core.*;

public final class VirtualWorld extends PApplet {
    private static String[] ARGS;

    private final int VIEW_WIDTH = 640;
    private final int VIEW_HEIGHT = 480;
    private final int TILE_WIDTH = 32;
    private final int TILE_HEIGHT = 32;

    private final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;

    private final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private final int DEFAULT_IMAGE_COLOR = 0x808080;

    private final String FAST_FLAG = "-fast";
    private final String FASTER_FLAG = "-faster";
    private final String FASTEST_FLAG = "-fastest";
    private final double FAST_SCALE = 0.5;
    private final double FASTER_SCALE = 0.25;
    private final double FASTEST_SCALE = 0.10;

    private String loadFile = "world.sav";
    private long startTimeMillis = 0;
    private double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        parseCommandLine(ARGS);
        loadImages(IMAGE_LIST_FILE_NAME);
        loadWorld(loadFile, this.imageStore);

        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler();
        this.startTimeMillis = System.currentTimeMillis();
        this.scheduleActions(world, scheduler, imageStore);
    }

    public void draw() {
        double appTime = (System.currentTimeMillis() - startTimeMillis) * 0.001;
        double frameTime = (appTime - scheduler.getCurrentTime())/timeScale;
        this.update(frameTime);
        view.drawViewport();
    }

    public void update(double frameTime){
        scheduler.updateOnTime(frameTime);
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint();
        System.out.println("CLICK! " + pressed.x + ", " + pressed.y);
        //adding coin and changing the background tiles surrounding it
        Coin coin = new Coin(Coin.COIN_KEY, pressed, imageStore.getImageList(Coin.COIN_KEY), Double.parseDouble(String.valueOf(Coin.COIN_ANIMATION_PERIOD)));
        //world.tryAddEntity(coin);

        if (world.tryAddEntity(coin)){
            coin.scheduleActions(scheduler, world, imageStore);
            Rock rock1 = new Rock(Rock.ROCK_KEY, new Point(pressed.x + 1, pressed.y + 1), imageStore.getImageList(Rock.ROCK_KEY));
            world.tryAddEntity(rock1);
//            Rock rock2 = new Rock(Rock.ROCK_KEY, new Point(pressed.x + 1, pressed.y), imageStore.getImageList(Rock.ROCK_KEY));
//            world.tryAddEntity(rock2);
            Rock rock3 = new Rock(Rock.ROCK_KEY, new Point(pressed.x + 1, pressed.y - 1), imageStore.getImageList(Rock.ROCK_KEY));
            world.tryAddEntity(rock3);
//            Rock rock4 = new Rock(Rock.ROCK_KEY, new Point(pressed.x, pressed.y + 1), imageStore.getImageList(Rock.ROCK_KEY));
//            world.tryAddEntity(rock4);
//            Rock rock5 = new Rock(Rock.ROCK_KEY, new Point(pressed.x, pressed.y - 1), imageStore.getImageList(Rock.ROCK_KEY));
//            world.tryAddEntity(rock5);
            Rock rock6 = new Rock(Rock.ROCK_KEY, new Point(pressed.x - 1, pressed.y + 1), imageStore.getImageList(Rock.ROCK_KEY));
            world.tryAddEntity(rock6);
            Rock rock7 = new Rock(Rock.ROCK_KEY, new Point(pressed.x - 1, pressed.y - 1), imageStore.getImageList(Rock.ROCK_KEY));
            world.tryAddEntity(rock7);
//            Rock rock8 = new Rock(Rock.ROCK_KEY, new Point(pressed.x - 1, pressed.y), imageStore.getImageList(Rock.ROCK_KEY));
//            world.tryAddEntity(rock8);
        }

//        Rock rock4 = new Rock(Rock.ROCK_KEY, new Point(pressed.x + 1, pressed.y - 1), imageStore.getImageList(Rock.ROCK_KEY));
//        world.tryAddEntity(rock4);

        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            if (entity instanceof Sapling){
                Sapling entity1 = (Sapling) entity;
                System.out.println(entity1.getId() + ": " + entity1.getClass() + " : " + entity1.getHealth());
            }
            else if (entity instanceof Tree){
                Tree entity2 = (Tree) entity;
                System.out.println(entity2.getId() + ": " + entity2.getClass() + " : " + entity2.health);
            }
            else {
                System.out.println(entity.getId() + ": " + entity.getClass() + " : ");
            }
        }

    }

    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof House || entity instanceof Stump){
                continue;
            }
            else if (entity instanceof Obstacle){
                Obstacle entity1 = (Obstacle) entity;
                entity1.scheduleActions(scheduler, world, imageStore);
                continue;
            }
            else if (entity instanceof Coin){
                Coin entity2 = (Coin) entity;
                entity2.scheduleActions(scheduler, world, imageStore);
                continue;
            }
            EntityAction entity1 = (EntityAction) entity;
            entity1.scheduleActions(scheduler, world, imageStore);
        }
    }

    private Point mouseToPoint() {
        return view.getViewport().viewportToWorld(mouseX / TILE_WIDTH, mouseY / TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP -> dy -= 1;
                case DOWN -> dy += 1;
                case LEFT -> dx -= 1;
                case RIGHT -> dx += 1;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    private PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        Arrays.fill(img.pixels, color);
        img.updatePixels();
        return img;
    }

    public void loadImages(String filename) {
        this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, this);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadWorld(String file, ImageStore imageStore) {
        this.world = new WorldModel();
        try {
            Scanner in = new Scanner(new File(file));
            world.load(in, imageStore, createDefaultBackground(imageStore));
        } catch (FileNotFoundException e) {
            Scanner in = new Scanner(file);
            world.load(in, imageStore, createDefaultBackground(imageStore));
        }
    }

    public void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG -> timeScale = Math.min(FAST_SCALE, timeScale);
                case FASTER_FLAG -> timeScale = Math.min(FASTER_SCALE, timeScale);
                case FASTEST_FLAG -> timeScale = Math.min(FASTEST_SCALE, timeScale);
                default -> loadFile = arg;
            }
        }
    }

    public static void main(String[] args) {
        VirtualWorld.ARGS = args;
        PApplet.main(VirtualWorld.class);
    }

    public static List<String> headlessMain(String[] args, double lifetime){
        VirtualWorld.ARGS = args;

        VirtualWorld virtualWorld = new VirtualWorld();
        virtualWorld.setup();
        virtualWorld.update(lifetime);

        return virtualWorld.world.log();
    }
}