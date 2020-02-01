package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.spaceinvaders.game.SpaceInvaders;
import states.PlayState;

public class Player {

    private static final int PLAYER_SPEED = 300;
    private static final int INVULNERABLE_TIME = 3000;

    private Texture player;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle bounds;
    private Sound deadSound;

    private int leftBound;
    private int rightBound;

    private Laser laser;
    private Sound shoot;
    private int lasersFired;

    private int numLives;
    private boolean dead;
    private boolean deadAnimation;
    private long timeDead;
    private boolean justDied;
    private long timeRespawn;

    public Player(int y) {
        player = new Texture("player.png");
        position = new Vector2(SpaceInvaders.WIDTH / 2 - player.getWidth() * SpaceInvaders.SCALE / 2, y);
        velocity = new Vector2(0, 0);
        bounds = new Rectangle(position.x, position.y, player.getWidth() * SpaceInvaders.SCALE,
                player.getHeight() * SpaceInvaders.SCALE);
        deadSound = Gdx.audio.newSound(Gdx.files.internal("player-dead.mp3"));

        leftBound = PlayState.EDGES * SpaceInvaders.SCALE;
        rightBound = SpaceInvaders.WIDTH - PlayState.EDGES * SpaceInvaders.SCALE - player.getWidth() * SpaceInvaders.SCALE;

        laser = new Laser(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        shoot = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
        lasersFired = 0;

        numLives = 0;
        dead = false;
        deadAnimation = false;
        timeDead = 0;
        justDied = false;
        timeRespawn = 0;
    }

    public void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && position.x <  rightBound && !dead) {
            velocity.x = PLAYER_SPEED;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && position.x > leftBound && !dead) {
            velocity.x = -PLAYER_SPEED;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && !dead) {
            if (laser.isLaserOffScreen()) {
                laser.setPosition(position.x +
                                player.getWidth() / 2 * SpaceInvaders.SCALE
                                - laser.getTexture().getWidth() / 2 * SpaceInvaders.SCALE,
                        position.y +
                                player.getHeight() / 2 * SpaceInvaders.SCALE
                                + laser.getTexture().getHeight() / 2 * SpaceInvaders.SCALE);
                shoot.play();
                laser.fire();
                lasersFired += 1;
            }
        }
    }

    public void update(float dt, Array<EnemyGroup> eg, Array<BlockGroup> bg, Ship s) {
        if (System.currentTimeMillis() - timeDead > PlayState.TIME_DEAD && dead && !deadAnimation) {
            deadAnimation = true;
            position.set(Integer.MIN_VALUE, position.y);
        } else if (System.currentTimeMillis() - timeDead > PlayState.TIME_RESPAWN && dead && deadAnimation) {
            if (numLives > 0) {
                dead = false;
                numLives -= 1;
                deadAnimation = false;
                player = new Texture("player.png");
                position.x = SpaceInvaders.WIDTH / 2 - player.getWidth() * SpaceInvaders.SCALE / 2;
                bounds.setPosition(position);
                timeRespawn = System.currentTimeMillis();
            } else {
                justDied = true;
            }
        } else {
            justDied = false;
            velocity.scl(dt);
            position.add(velocity);
            velocity.scl(1 / dt);
            velocity.x = 0;
            if (position.x > rightBound && !dead) {
                position.x = rightBound;
            }
            if (position.x < leftBound && !dead) {
                position.x = leftBound;
            }
            bounds.setPosition(position);
        }
        laser.update(dt);

        for (int i = 0; i < eg.size; i++) {
            EnemyGroup enemies = eg.get(i);
            for (int j = 0; j < enemies.getArray().size; j++) {
                Enemy enemy = enemies.getArray().get(j);
                laser.updateEnemy(enemy);
                Queue<Laser> enemyLasers = enemy.getLasers();
                for (Laser l : enemyLasers){
                    laser.updateLaser(l);
                }
            }
        }
        for (int i = 0; i < bg.size; i++) {
            BlockGroup blockGroup = bg.get(i);
            for (int j = 0; j < blockGroup.getArray().size; j++) {
                Block block = blockGroup.getArray().get(j);
                laser.updateBlock(block);
            }
        }
        laser.updateShip(s);
    }

    public void render(SpriteBatch sb) {
        sb.draw(player, position.x, position.y,
                player.getWidth() * SpaceInvaders.SCALE,
                player.getHeight() * SpaceInvaders.SCALE);
        laser.render(sb);
    }

    public void dispose() {
        player.dispose();
        deadSound.dispose();
        laser.dispose();
        shoot.dispose();
    }

    public void enemiesInvaded() {
        numLives = 0;
        dies();
    }

    public void dies() {
        if (System.currentTimeMillis() - timeRespawn > INVULNERABLE_TIME) {
            dead = true;
            player = new Texture("player-dead.png");
            bounds.setPosition(Integer.MIN_VALUE, position.y);
            deadSound.play();
            timeDead = System.currentTimeMillis();
        }
    }

    public void addLife() {
        numLives += 1;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean justDied() {
        return justDied;
    }

    public int getNumLives() {
        return numLives;
    }

    public int getLasersFired() {
        return lasersFired;
    }
}
