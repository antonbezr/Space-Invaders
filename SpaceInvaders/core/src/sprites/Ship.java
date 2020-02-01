package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.spaceinvaders.game.SpaceInvaders;

public class Ship {

    public static final int SHIP_TIME = 30000;
    public static final int MAX_FLY_COUNT = 5;

    private Texture ship;
    private Texture shipAlive;
    private Texture shipDead;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 reset;
    private Rectangle bounds;
    private Sound flying;
    private Sound deadSound;

    private boolean dead;
    private boolean deadAnimation;

    private int flyCount;
    private int points;
    private boolean givePoints;

    private long timeStart;
    private long timeDead;

    public Ship(float y) {
        ship = new Texture("ship.png");
        shipAlive = new Texture("ship.png");
        shipDead = new Texture("ship-dead.png");

        position = new Vector2(SpaceInvaders.WIDTH, y);
        velocity = new Vector2(-140, 0);
        reset = new Vector2(SpaceInvaders.WIDTH, y);
        bounds = new Rectangle(SpaceInvaders.WIDTH, y, ship.getWidth() * SpaceInvaders.SCALE,
                ship.getHeight() * SpaceInvaders.SCALE);
        flying = Gdx.audio.newSound(Gdx.files.internal("flying.mp3"));
        deadSound = Gdx.audio.newSound(Gdx.files.internal("ship-dead.mp3"));

        dead = true;
        deadAnimation = false;

        points = 0;
        givePoints = false;

        timeStart = System.currentTimeMillis();
        timeDead = 0;
    }

    public void update(float dt, Player p, int points) {
        this.points = points;
        if (position.x > 0 - shipDead.getWidth() * SpaceInvaders.SCALE && !dead) {
            velocity.scl(dt);
            position.add(velocity.x, 0);
            velocity.scl(1/dt);
            bounds.setPosition(position.x, position.y);
        } else {
            flying.stop();
        }
        if (System.currentTimeMillis() - timeDead > 100 && dead && !deadAnimation) {
            deadAnimation = true;
            position.set(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        else if (System.currentTimeMillis() - timeStart > SHIP_TIME && flyCount < MAX_FLY_COUNT) {
            timeStart = System.currentTimeMillis();
            flyCount += 1;
            revive();
        }
    }

    public void render(SpriteBatch sb) {
        sb.draw(ship, position.x, position.y,
                ship.getWidth() * SpaceInvaders.SCALE, ship.getHeight() * SpaceInvaders.SCALE);
    }

    public void dispose() {
        ship.dispose();
        shipAlive.dispose();
        shipDead.dispose();
        flying.dispose();
        deadSound.dispose();
    }

    public void dies() {
        dead = true;
        givePoints = true;
        deadAnimation = false;
        position.x = position.x - (shipDead.getWidth() - shipAlive.getWidth()) / 2;
        ship = shipDead;
        bounds.setPosition(Integer.MIN_VALUE, Integer.MAX_VALUE);
        flying.stop();
        deadSound.play();
        timeDead = System.currentTimeMillis();
        timeStart = System.currentTimeMillis();
    }

    public void revive() {
        flying.loop();
        dead = false;
        ship = shipAlive;
        position.set(reset);
        bounds.setPosition(position);
    }

    public void reset() {
        flyCount = 0;
        timeStart = System.currentTimeMillis();
    }

    public int points() {
        if (dead && givePoints) {
            givePoints = false;
            return points;
        }
        return 0;
    }

    public boolean isDead() {
        return dead;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
