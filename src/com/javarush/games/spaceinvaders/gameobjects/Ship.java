package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends GameObject{

    private List<int[][]> frames;
    private int frameIndex;
    private boolean loopAnimation = false;

    public boolean isAlive = true;

    public Ship(double x, double y) {
        super(x, y);
    }

    public void setStaticView(int[][] viewFrame){
        setMatrix(viewFrame);
        frames = new ArrayList<>();
        frames.add(viewFrame);
        frameIndex = 0;
    }

    public void setAnimatedView(boolean isLoopAnimation,  int[][]... viewFrames) {
        loopAnimation = isLoopAnimation;
        frames = Arrays.asList(viewFrames);
        frameIndex = 0;
        setMatrix(viewFrames[0]);
    }

    public Bullet fire() {
        return null;
    }

    public void kill() {
        isAlive = false;
    }

    public void nextFrame() {
        frameIndex++;

        if (frameIndex < frames.size() && loopAnimation) {
            matrix = frames.get(frameIndex);
        }else if (frameIndex >= frames.size() && loopAnimation){
            frameIndex = 0;
        }
    }

    public boolean isVisible() {
        if (!isAlive && frameIndex >= frames.size()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        nextFrame();
    }
}
