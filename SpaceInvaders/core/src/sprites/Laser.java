package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.spaceinvaders.game.SpaceInvaders;
import states.PlayState;

public class Laser {

    private static final int LASER_VELOCITY_PLAYER = 350;
    private static final int LASER_VELOCITY_ENEMY_SLOW = -200;
    private static final int LASER_VELOCITY_ENEMY_FAST = -300;

    private Texture laser;
    private int setVelocity;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle bounds;
    private boolean laserOffScreen;
    private boolean dead;
    private long timeDead;

    public Laser(float x, float y, int version) {
        if (version == 0) {
            laser = new Texture("laser-player.png");
            setVelocity = LASER_VELOCITY_PLAYER;
        } else if (version == 1) {
            laser = new Texture("laser-enemy-v1.png");
            setVelocity = LASER_VELOCITY_ENEMY_SLOW;
        } else {
            laser = new Texture("laser-enemy-v2.png");
            setVelocity = LASER_VELOCITY_ENEMY_FAST;
        }
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        bounds = new Rectangle(x, y, laser.getWidth() * SpaceInvaders.SCALE,
                laser.getHeight() * SpaceInvaders.SCALE);
        laserOffScreen = false;
        dead = false;
        timeDead = 0;
    }

    public void update(float dt) {
        Texture testLives = new Texture("lives.png");
        if (dead) {
            if (System.currentTimeMillis() - timeDead > 100) {
                position.set(Integer.MIN_VALUE, Integer.MAX_VALUE);
                dead = false;
            }
        } else if (position.y > SpaceInvaders.HEIGHT -
            (PlayState.EDGES  + testLives.getHeight() * 3 + laser.getHeight()) * SpaceInvaders.SCALE) {
            position.set(Integer.MIN_VALUE, Integer.MAX_VALUE);
            bounds.setPosition(position);
            velocity.y = 0;
            laserOffScreen = true;
        } else {
            velocity.scl(dt);
            position.add(velocity);
            velocity.scl(1/dt);
            bounds.setPosition(position);
            laserOffScreen = false;
        }
    }

    public void render(SpriteBatch sb) {
        sb.draw(laser, position.x, position.y,
                laser.getWidth() * SpaceInvaders.SCALE, laser.getHeight() * SpaceInvaders.SCALE);
    }

    public void dispose() {
        laser.dispose();
    }

    public void updateEnemy(Enemy e) {
        if (collides(e.getBounds())) {
            if (!e.isDead()) {
                e.dies();
            }
            setPosition(Integer.MIN_VALUE, Integer.MAX_VALUE);
            laserOffScreen = true;
        }
    }

    public void updatePlayer(Player p) {
        if (collides(p.getBounds())) {
            if (!p.isDead()) {
                p.dies();
            }
            setPosition(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    public void updateBlock(Block b) {
        if (collides(b.getBounds())) {
            b.loseHealth();
            setPosition(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    public void updateShip(Ship s) {
        if (collides(s.getBounds())) {
            if (!s.isDead()) {
                s.dies();
            }
            setPosition(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    public void updateLaser(Laser l) {
        if (collides(l.getBounds())) {
            l.dies();
            setPosition(Integer.MIN_VALUE, Integer.MAX_VALUE);
            velocity = new Vector2(0, 0);
        }
    }

    public void dies() {
        dead = true;
        timeDead = System.currentTimeMillis();
        Texture laserDead = new Texture("laser-dead.png");
        position.x = position.x - (laserDead.getWidth() - laser.getWidth()) / 2;
        position.y = position.y - laserDead.getHeight() / 2;
        laser = laserDead;
        bounds.setPosition(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public boolean collides(Rectangle r) {
        return bounds.overlaps(r);
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public void fire() {
        velocity.y = setVelocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return laser;
    }

    public boolean isLaserOffScreen() {
        return laserOffScreen;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
