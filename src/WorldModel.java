import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel{
    public static final Random rand = new Random();
    private int numRows; //*
    private int numCols; //*
    private Background[][] background;
    private Entity[][] occupancy;
    private Set<Entity> entities; //*
    private final int PROPERTY_KEY = 0;
    private final int PROPERTY_ID = 1;
    private final int PROPERTY_COL = 2;
    private final int PROPERTY_ROW = 3;
    private final int ENTITY_NUM_PROPERTIES = 4;
    private final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right", "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));

    public WorldModel() {

    }

    public static int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    public void parseEntity(String line, ImageStore imageStore) {
        String[] properties = line.split(" ", ENTITY_NUM_PROPERTIES + 1);
        if (properties.length >= ENTITY_NUM_PROPERTIES) {
            String key = properties[PROPERTY_KEY];
            String id = properties[PROPERTY_ID];
            Point pt = new Point(Integer.parseInt(properties[PROPERTY_COL]), Integer.parseInt(properties[PROPERTY_ROW]));

            properties = properties.length == ENTITY_NUM_PROPERTIES ?
                    new String[0] : properties[ENTITY_NUM_PROPERTIES].split(" ");

            switch (key) {
                case Obstacle.OBSTACLE_KEY -> parseObstacle(properties, pt, id, imageStore);
                case Dude.DUDE_KEY -> parseDude(properties, pt, id, imageStore);
                case Fairy.FAIRY_KEY -> parseFairy(properties, pt, id, imageStore);
                case House.HOUSE_KEY -> parseHouse(properties, pt, id, imageStore);
                case Tree.TREE_KEY -> parseTree(properties, pt, id, imageStore);
                case Sapling.SAPLING_KEY -> parseSapling(properties, pt, id, imageStore);
                case Stump.STUMP_KEY -> parseStump(properties, pt, id, imageStore);
                default -> throw new IllegalArgumentException("Entity key is unknown");
            }
        }else{
            throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
        }
    }

    public Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = WorldModel.distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = WorldModel.distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public void parseSaveFile(Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        String lastHeader = "";
        int headerLine = 0;
        int lineCounter = 0;
        while(saveFile.hasNextLine()){
            lineCounter++;
            String line = saveFile.nextLine().strip();
            if(line.endsWith(":")){
                headerLine = lineCounter;
                lastHeader = line;
                switch (line){
                    case "Backgrounds:" -> this.background = new Background[this.getNumRows()][this.getNumCols()];
                    case "Entities:" -> {
                        this.occupancy = new Entity[this.getNumRows()][this.getNumCols()];
                        this.entities = new HashSet<>();
                    }
                }
            }else{
                switch (lastHeader){
                    case "Rows:" -> this.numRows = Integer.parseInt(line);
                    case "Cols:" -> this.numCols = Integer.parseInt(line);
                    case "Backgrounds:" -> parseBackgroundRow(line, lineCounter-headerLine-1, imageStore);
                    case "Entities:" -> this.parseEntity(line, imageStore);
                }
            }
        }
    }

    public void parseBackgroundRow(String line, int row, ImageStore imageStore) {
        String[] cells = line.split(" ");
        if(row < this.getNumRows()){
            int rows = Math.min(cells.length, this.getNumCols());
            for (int col = 0; col < rows; col++){
                this.background[row][col] = new Background(cells[col], imageStore.getImageList(cells[col]));
            }
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos) {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        } else {
            return Optional.empty();
        }
    }

    public void setBackgroundCell(Point pos, Background background) {
        this.background[pos.y][pos.x] = background;
    }

    public Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }

    public void load(Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        this.parseSaveFile(saveFile, imageStore, defaultBackground);
        if(this.background == null){
            this.background = new Background[this.getNumRows()][this.getNumCols()];
            for (Background[] row : this.background)
                Arrays.fill(row, defaultBackground);
        }
        if(this.occupancy == null){
            this.occupancy = new Entity[this.getNumRows()][this.getNumCols()];
            this.entities = new HashSet<>();
        }
    }

    public void removeEntity(EventScheduler scheduler, Entity entity) {
        scheduler.unscheduleAllEvents(entity);
        removeEntityAt(entity.getPosition());
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.getEntities().remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public void setOccupancyCell(Point pos, Entity entity) {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    public void moveEntity(EventScheduler scheduler, Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell(oldPos, null);
            Optional<Entity> occupant = this.getOccupant(pos);
            occupant.ifPresent(target -> this.removeEntity(scheduler, target));
            this.setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    /*
           Assumes that there is no entity currently occupying the
           intended destination cell.
        */
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            this.setOccupancyCell(entity.getPosition(), entity);
            this.getEntities().add(entity);
        }
    }

    public Optional<Entity> findNearest(Point pos, List<Class> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind : kinds) {
            for (Entity entity : this.getEntities()) {
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return this.nearestEntity(ofType, pos);
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && this.getOccupancyCell(pos) != null;
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.getNumRows() && pos.x >= 0 && pos.x < this.getNumCols();
    }

    public boolean tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.getPosition())){
            System.out.println("Position is Occupied");
            return false;
        }
        else {
            this.addEntity(entity);
            return true;
        }
    }

    public void parseStump(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Stump.STUMP_NUM_PROPERTIES) {
            Stump entity = new Stump(id, pt, imageStore.getImageList(Stump.STUMP_KEY));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Stump.STUMP_KEY, Stump.STUMP_NUM_PROPERTIES));
        }
    }

    public void parseHouse(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == House.HOUSE_NUM_PROPERTIES) {
            House entity = new House(id, pt, imageStore.getImageList(House.HOUSE_KEY));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", House.HOUSE_KEY, House.HOUSE_NUM_PROPERTIES));
        }
    }

    public void parseObstacle(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Obstacle.OBSTACLE_NUM_PROPERTIES) {
            Obstacle entity = new Obstacle(id, pt, imageStore.getImageList(Obstacle.OBSTACLE_KEY), Double.parseDouble(properties[Obstacle.OBSTACLE_ANIMATION_PERIOD]));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Obstacle.OBSTACLE_KEY, Obstacle.OBSTACLE_NUM_PROPERTIES));
        }
    }
    public void parseTree(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Tree.TREE_NUM_PROPERTIES) {
            Tree entity = new Tree(id, pt, imageStore.getImageList(Tree.TREE_KEY), Double.parseDouble(properties[Tree.TREE_ANIMATION_PERIOD]), Double.parseDouble(properties[Tree.TREE_ACTION_PERIOD]), Integer.parseInt(properties[Tree.TREE_HEALTH]));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Tree.TREE_KEY, Tree.TREE_NUM_PROPERTIES));
        }
    }

    public void parseFairy(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Fairy.FAIRY_NUM_PROPERTIES) {
            Fairy entity = new Fairy(id, pt, imageStore.getImageList(Fairy.FAIRY_KEY), Double.parseDouble(properties[Fairy.FAIRY_ANIMATION_PERIOD]), Double.parseDouble(properties[Fairy.FAIRY_ACTION_PERIOD]));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Fairy.FAIRY_KEY, Fairy.FAIRY_NUM_PROPERTIES));
        }
    }

    public void parseDude(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Dude.DUDE_NUM_PROPERTIES) {
            Dude_Not_Full entity = new Dude_Not_Full(id, pt, imageStore.getImageList(Dude.DUDE_KEY),
                    Double.parseDouble(properties[Dude.DUDE_ANIMATION_PERIOD]),
                    Double.parseDouble(properties[Dude.DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[Dude.DUDE_LIMIT]), 0);
            //System.out.println(entity.g);
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Dude.DUDE_KEY, Dude.DUDE_NUM_PROPERTIES));
        }
    }

    public void parseSapling(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Sapling.SAPLING_NUM_PROPERTIES) {
            int health = Integer.parseInt(properties[Sapling.SAPLING_HEALTH]);
            Sapling entity = new Sapling(id, pt, imageStore.getImageList(Sapling.SAPLING_KEY),  Sapling.SAPLING_ACTION_ANIMATION_PERIOD, Sapling.SAPLING_ACTION_ANIMATION_PERIOD, health,  Sapling.SAPLING_HEALTH_LIMIT);
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Sapling.SAPLING_KEY, Sapling.SAPLING_NUM_PROPERTIES));
        }
    }

    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entity entity : getEntities()) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Set<Entity> getEntities() {
        return entities;
    }
}
