package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.javarush.games.spaceinvaders.SpaceInvadersGame.WIDTH;

public class EnemyFleet {

    public EnemyFleet() {
        createShips();
    }

    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;

    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    public void draw(Game game) {
        for (EnemyShip enemyShip : ships) {
            enemyShip.draw(game);
        }
    }

    public void move() {
        if (ships.isEmpty()) {

        } else {
            Direction changedDirection = direction;
            if (direction == Direction.LEFT && getLeftBorder() < 0) {
                direction = Direction.RIGHT;
                ships.forEach(s -> s.move(Direction.DOWN, getSpeed()));
            } else if (direction == Direction.RIGHT && getRightBorder() > WIDTH) {
                direction = Direction.LEFT;
                ships.forEach(s -> s.move(Direction.DOWN, getSpeed()));
            }

            if (changedDirection == direction) {
                ships.forEach(s -> s.move(direction, getSpeed()));
            }
        }
    }

    public Bullet fire(Game game) {
        if (ships.isEmpty() || game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY) > 0) {
            return null;
        } else {
            return ships.get(game.getRandomNumber(ships.size())).fire();
        }
    }

    public int verifyHit(List<Bullet> bullets) {
        if (bullets.size() == 0) {
            return 0;
        } else {
            int allScore = 0;
            for (EnemyShip ship : ships) {
                for (Bullet bullet : bullets) {
                    if (bullet.isAlive && ship.isAlive && ship.isCollision(bullet) && bullet.isCollision(ship)) {
                        bullet.kill();
                        ship.kill();
                        allScore = allScore + ship.score;
                    }
                }
            }
            return allScore;
        }
    }

    public void deleteHiddenShips() {
        ships.removeIf(ship -> !ship.isVisible());
    }

    public double getBottomBorder() {
        double max = 0;
        for (Ship ship:
             ships) {
            if (ship.y + ship.height > max) {
                max = ship.y + ship.height;
            }
        }
        return max;
    }

    public int getShipsCount() {
        return ships.size();
    }

    private void createShips() {
        ships = new ArrayList<>();

        for (int x = 0; x < COLUMNS_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                ships.add(new EnemyShip(x * STEP, y * STEP + 12));
            }
        }

        ships.add(new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5));
    }

    private double getLeftBorder() {
        Ship ship = ships.stream().min(Comparator.comparingDouble(s -> s.x)).get();
        return ship.x;
    }

    private double getRightBorder() {
        Ship ship = ships.stream().max(Comparator.comparingDouble(s -> s.x)).get();
        return ship.x + ship.width;
    }

    private double getSpeed() {
        return Math.min(2.0, 3.0 / ships.size());
    }
}
