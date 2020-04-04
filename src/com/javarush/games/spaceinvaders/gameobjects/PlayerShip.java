package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.List;

public class PlayerShip extends Ship {

    private Direction direction = Direction.UP;

    public void setDirection(Direction newDirection) {
        this.direction = newDirection == Direction.DOWN ? this.direction : newDirection;
    }

    public Direction getDirection() {
        return direction;
    }

    public PlayerShip() {
        super(SpaceInvadersGame.WIDTH / 2.0, SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length - 1);
        setStaticView(ShapeMatrix.PLAYER);
    }

    public void verifyHit(List<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            if (bullet.isAlive && isAlive && isCollision(bullet)) {
                bullet.kill();
                kill();
            }
        }
    }

    @Override
    public void kill() {
        if (isAlive) {
            setAnimatedView(false, ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST,
                    ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND, ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD, ShapeMatrix.DEAD_PLAYER);
            isAlive = false;
        }
    }

    public void move() {
        if (isAlive) {
            if (direction.equals(Direction.RIGHT)) {
                x++;
            } else if (direction.equals(Direction.LEFT)) {
                x--;
            }

            x = x < 0 ? 0 : x;

            x = (x + width) > SpaceInvadersGame.WIDTH ? SpaceInvadersGame.WIDTH - width : x;
        }
    }

    @Override
    public Bullet fire() {
        if (isAlive) {
            return new Bullet(x + 2, y - ShapeMatrix.BULLET.length, Direction.UP);
        } else {
            return null;
        }
    }

    public void win() {
        setStaticView(ShapeMatrix.WIN_PLAYER);
    }
}
