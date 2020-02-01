package sprites;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.spaceinvaders.game.SpaceInvaders;
import states.PlayState;

public class Enemy {

    public static final int START_FIRE_SPEED = 20000;
    public static final int START_INCREMENT = 2000;
    public static final int TOTAL_MOVES = 40;
    public static final int TOTAL_DROPS = 7;
    public static final double MOVE_DISTANCE = 2.43589744;

    private Texture enemy;
    private Queue<Texture> animation;
    private Vector2 position;
    private Rectangle bounds;
    private Sound deadSound;

    private int moves;
    private int drops;

    private Queue<Laser> lasers;
    private boolean firingLasers;

    private boolean dead;
    private boolean deadAnimation;
    private boolean move;

    private int points;

    private long timeDead;
    private long timeFire;

    private Random random;
    private long randNum;

    private int fireSpeed;
    private int increment;

    private boolean shieldsDown;

    public Enemy(float x, float y, int v) {
        if (v == 0) {
            enemy = new Texture("enemy1-v1.png");
            animation = new Queue<Texture>();
            animation.addLast(new Texture("enemy1-v2.png"));
            points = 10;
        } else if (v == 1) {
            enemy = new Texture("enemy2-v1.png");
            animation = new Queue<Texture>();
            animation.addLast(new Texture("enemy2-v2.png"));
            points = 20;
        } else {
            enemy = new Texture("enemy3-v1.png");
            animation = new Queue<Texture>();
            animation.addLast(new Texture("enemy3-v2.png"));
            points = 30;
        }
        position = new Vector2(x, y);
        bounds = new Rectangle(x, y, enemy.getWidth() * SpaceInvaders.SCALE,
                enemy.getHeight() * SpaceInvaders.SCALE);
        deadSound = Gdx.audio.newSound(Gdx.files.internal("enemy-dead.mp3"));

        moves = TOTAL_MOVES / 2;
        drops = 0;

        lasers = new Queue<Laser>();
        firingLasers = false;

        dead = false;
        deadAnimation = false;
        move = false;

        timeDead = 0;
        timeFire = System.currentTimeMillis();

        fireSpeed = START_FIRE_SPEED;
        increment = START_INCREMENT;

        shieldsDown = false;

        random = new Random();
        randNum = (long) System.currentTimeMillis() + random.nextInt(fireSpeed) + increment;
    }

    public void update(float dt, Player p, Array<BlockGroup> bg) {
        for (int i = 0; i < lasers.size; i++) {
            Laser laser = lasers.get(i);
            if (laser.isLaserOffScreen()) {
                lasers.removeIndex(i);
                laser.dispose();
                i -= 1;
            } else {
                laser.update(dt);
                laser.updatePlayer(p);
                for (int j = 0; j < bg.size; j++) {
                    BlockGroup blocks = bg.get(j);
                    for (int k = 0; k < blocks.getArray().size; k++) {
                        Block block = blocks.getArray().get(k);
                        laser.updateBlock(block);
                    }
                }
            }
        }
        if (System.currentTimeMillis() - timeDead > PlayState.TIME_DEAD && dead && !deadAnimation) {
            deadAnimation = true;
            position.set(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        if (move) {
            move = false;
            if (moves < TOTAL_MOVES) {
                position.x += MOVE_DISTANCE * SpaceInvaders.SCALE;
                moves += 1;
            } else if (moves == TOTAL_MOVES) {
                if (shieldsDown || drops < TOTAL_DROPS) {
                    position.y -= EnemyGroup.ENEMY_Y_SPACING * SpaceInvaders.SCALE;
                    drops += 1;
                }
                moves += 1;
            } else if (moves <= TOTAL_MOVES * 2) {
                position.x -= MOVE_DISTANCE * SpaceInvaders.SCALE;
                moves += 1;
            } else {
                if (shieldsDown || drops < TOTAL_DROPS) {
                    position.y -= EnemyGroup.ENEMY_Y_SPACING * SpaceInvaders.SCALE;
                    drops += 1;
                }
                moves = 0;
            }
            if (!dead) {
                bounds.setPosition(position.x, position.y);
                Texture prev = enemy;
                enemy = animation.removeFirst();
                animation.addLast(prev);
            }
        }
        if (System.currentTimeMillis() - timeFire > randNum && !dead && firingLasers) {
            timeFire = System.currentTimeMillis();

            randNum = (long) random.nextInt(4);

            if (randNum < 3) {
                fire(1);
            } else {
                fire(2);
            }
            randNum = (long) random.nextInt(fireSpeed) + increment;
        }
    }

    public void render(SpriteBatch sb) {
        sb.draw(enemy, position.x, position.y,
                enemy.getWidth() * SpaceInvaders.SCALE, enemy.getHeight() * SpaceInvaders.SCALE);
        for (int i = 0; i < lasers.size; i++) {
            Laser laser = lasers.get(i);
            if (laser.getPosition().y < 0) {
                laser = lasers.removeIndex(i);
                laser = null;
            } else {
                laser.render(sb);
            }
        }
    }

    public void dispose() {
        enemy.dispose();
        for (Texture t : animation) {
            t.dispose();
        }
        deadSound.dispose();
        for (Laser l : lasers) {
            l.dispose();
        }
    }

    public void fire(int type) {
        Laser laser = new Laser(Integer.MIN_VALUE, Integer.MAX_VALUE, type);
        laser.setPosition(position.x + enemy.getWidth() / 2 * SpaceInvaders.SCALE -
                        laser.getTexture().getWidth() / 2 * SpaceInvaders.SCALE,
                position.y - laser.getTexture().getHeight() * SpaceInvaders.SCALE);
        laser.fire();
        lasers.addFirst(laser);
    }

    public void dies() {
        dead = true;
        Texture enemyDead = new Texture("enemy-dead.png");
        position.x = position.x - (enemyDead.getWidth() - enemy.getWidth()) / 2;
        enemy = enemyDead;
        bounds.setPosition(Integer.MIN_VALUE, Integer.MAX_VALUE);
        deadSound.play();
        timeDead = System.currentTimeMillis();
    }

    public void move() {
        move = true;
    }

    public void startFiring() {
        firingLasers = true;
        randNum = (long) random.nextInt(fireSpeed) + increment;
        timeFire = System.currentTimeMillis();
    }

    public void stopFiring() {
        firingLasers = true;
    }

    public void changeSpeeds(double m) {
        fireSpeed = (int) (START_FIRE_SPEED * m);
        increment = (int) (START_INCREMENT * m);
    }

    public void drop() {
        drops -= 1;
    }

    public void setShieldsDown() {
        shieldsDown = true;
    }

    public int getScore() {
        if (dead) return points;
        return 0;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return enemy;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isDeadAnimation() {
        return deadAnimation;
    }

    public int getDrops() {
        return drops;
    }

    public Queue<Laser> getLasers() {
        return lasers;
    }
}
