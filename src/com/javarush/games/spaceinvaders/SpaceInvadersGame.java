package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int COMPLEXITY = 5;

    private static final int PLAYER_BULLETS_MAX = 1;

    private List<Star> stars;
    private EnemyFleet enemyFleet;
    private List<Bullet> enemyBullets;
    private List<Bullet> playerBullets;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void drawField(){
        for (int height = 0; height < HEIGHT; height++){
            for (int width = 0; width < WIDTH; width++){
                setCellValueEx(height, width ,Color.CORAL, "");
            }
        }

        for (Star star: stars) {
            star.draw(this);
        }
    }

    private void drawScene(){
        drawField();
        playerShip.draw(this);
        enemyFleet.draw(this);
        enemyBullets.forEach(bullet -> bullet.draw(this));
        playerBullets.forEach(bullet -> bullet.draw(this));
    }

    private void createGame() {
        createStars();
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<>();
        playerBullets = new ArrayList<>();
        playerShip = new PlayerShip();
        isGameStopped = false;
        animationsCount = 0;
        drawScene();
        setTurnTimer(40);
        score = 0;
    }

    private void createStars() {
        stars = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            stars.add(new Star(new Random().nextInt(WIDTH), new Random().nextInt(HEIGHT)));
        }
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        enemyBullets.forEach(Bullet::move);
        playerBullets.forEach(Bullet::move);
        playerShip.move();
    }

    private void removeDeadBullets() {
        enemyBullets.removeIf(bullet -> bullet.y >= HEIGHT - 1 || !bullet.isAlive);
        playerBullets.removeIf(bullet -> (bullet.y + bullet.height) < 0 || !bullet.isAlive);
    }

    private void check() {
        playerShip.verifyHit(enemyBullets);
        score = score + enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        if (enemyFleet.getBottomBorder() >= playerShip.y) {
            playerShip.kill();
        }
        if (enemyFleet.getShipsCount() == 0) {
            playerShip.win();
        }
        removeDeadBullets();
        if (!playerShip.isAlive) {
            stopGameWithDelay();
        }
    }

    private void stopGame(boolean isWin) {
        isGameStopped = true;
        if (isWin) {
            showMessageDialog(Color.RED, "You are WINNER!!! CONGRATULATION!!!", Color.GREEN, 30);
        } else {
            showMessageDialog(Color.BLANCHEDALMOND, "Your die and don`t cry!!! Try again", Color.RED, 30);
        }
        stopTurnTimer();
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }

    public void onTurn(int frequency) {
        moveSpaceObjects();
        check();
        Bullet bullet = enemyFleet.fire(this);
        if (bullet != null) {
            enemyBullets.add(bullet);
        }
        setScore(score);
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if (key.equals(Key.SPACE) && isGameStopped) {
            createGame();
        }
        if (key.equals(Key.RIGHT)) {
            playerShip.setDirection(Direction.RIGHT);
        } else if (key.equals(Key.LEFT)) {
            playerShip.setDirection(Direction.LEFT);
        }
        if (key.equals(Key.SPACE)) {
            Bullet bullet = playerShip.fire();
            if (bullet != null && playerBullets.size() < PLAYER_BULLETS_MAX) {
                playerBullets.add(bullet);
            }
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if (key.equals(Key.LEFT) && playerShip.getDirection().equals(Direction.LEFT)) {
            playerShip.setDirection(Direction.UP);
        } else if (key.equals(Key.RIGHT) && playerShip.getDirection().equals(Direction.RIGHT)) {
            playerShip.setDirection(Direction.UP);
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        if (x > HEIGHT || y > WIDTH) {
            super.setCellValueEx(x, y, cellColor, value);
        }
    }
}