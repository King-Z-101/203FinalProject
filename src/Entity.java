import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private double animationPeriod;
//    private int resourceLimit;
//    private int resourceCount;
//    private double actionPeriod;
//    private double animationPeriod;
//    private int health;
//    private int healthLimit;

    public Entity(String id, Point position, List<PImage> images, double animationPeriod) {
        this.id = id;
        this.setPosition(position);
        this.images = images;
        this.imageIndex = 0;
        this.animationPeriod = animationPeriod;
//        this.resourceLimit = resourceLimit;
//        this.resourceCount = resourceCount;
////        this.actionPeriod = actionPeriod;
////        this.animationPeriod = animationPeriod;
////        this.health = health;
////        this.healthLimit = healthLimit;
    }

    //public abstract Entity createEntity(String id, Point position, List<PImage> images);
    public double getAnimationPeriod() {
//        switch (this.getClass()) {
//            case DUDE_FULL:
//            case DUDE_NOT_FULL:
//            case OBSTACLE:
//            case FAIRY:
//            case SAPLING:
//            case TREE:
        return animationPeriod;
//            default:
//                throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", this.getKind()));
//        }   //house and stump go through default == has its own implementation for getAnimationPeriod
    }





    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
    public String log(){
        return this.getId().isEmpty() ? null :
                String.format("%s %d %d %d", this.getId(), this.getPosition().x, this.getPosition().y, this.imageIndex);
    }
//    public double getAnimationPeriod() {
//        switch (this.getKind()) {
//            case DUDE_FULL:
//            case DUDE_NOT_FULL:
//            case OBSTACLE:
//            case FAIRY:
//            case SAPLING:
//            case TREE:
//                return this.animationPeriod;
//            default:
//                throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", this.getKind()));
//        }   //house and stump go through default == has its own implementation for getAnimationPeriod
//    }

    public PImage getCurrentImage() {
        if (this instanceof Entity) {
            return this.getImages().get(this.imageIndex % this.getImages().size());
        } else {
            throw new UnsupportedOperationException(String.format("getCurrentImage not supported for %s", this));
        }
    }

    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public List<PImage> getImages() {
        return images;
    }
}