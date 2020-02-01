package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.spaceinvaders.game.SpaceInvaders;
import sprites.*;

public class PlayState extends State {

    public static final int EDGES = 10;
    public static final int TIME_DEAD = 200;
    public static final int TIME_RESPAWN = 1200;
    public static final int ENEMY_TIME_MOVE_START = 1000;
    public static final int SPEED_FORMULA_DIVISOR = 75;
    public static final int ENEMY_ROWS = 11;
    public static final int ENEMY_SPACING = 18;
    public static final int NUM_SPACING = 3;

    private Texture background;
    private Player player;
    private Array<EnemyGroup> enemies;
    private Array<BlockGroup> blocks;
    private Ship ship;
    private Score score;
    private Lives lives;
    private Array<Sound> enemyMoveSounds;

    private int level;
    private int lasersFired;
    private int enemyDeadCount;
    private int prevNumScore;

    private int enemyLevel;
    private boolean enemyLevelDead;

    private long enemyTimeMove;
    private long enemyTimeMoveDiff;
    private boolean enemyMove;
    private int enemyMoveCount;

    private long timeDead;
    private boolean nextLevel;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("background.png");
        player = new Player(EDGES * SpaceInvaders.SCALE);
        ship = new Ship((float) 0.85 * SpaceInvaders.HEIGHT);
        score = new Score();
        lives = new Lives();

        resetEnemies();
        blocks = new Array<BlockGroup>();
        Texture testBlock = new Texture("full-block-1.png");
        for (int i = 1; i < 5; i++) {
            blocks.add(new BlockGroup(SpaceInvaders.WIDTH / 5 * i - testBlock.getWidth() * 4 / 2 * SpaceInvaders.SCALE,
                    SpaceInvaders.HEIGHT / 9));
        }

        enemyMoveSounds = new Array<Sound>();
        enemyMoveSounds.add(Gdx.audio.newSound(Gdx.files.internal("enemy-1.mp3")));
        enemyMoveSounds.add(Gdx.audio.newSound(Gdx.files.internal("enemy-2.mp3")));
        enemyMoveSounds.add(Gdx.audio.newSound(Gdx.files.internal("enemy-3.mp3")));
        enemyMoveSounds.add(Gdx.audio.newSound(Gdx.files.internal("enemy-4.mp3")));

        level = 1;
        lasersFired = 0;
        enemyDeadCount = 0;
        prevNumScore = 0;

        enemyLevel = 0;
        enemyLevelDead = true;

        enemyTimeMove = System.currentTimeMillis();
        enemyTimeMoveDiff = ENEMY_TIME_MOVE_START;
        enemyMove = false;
        enemyMoveCount = 0;

        timeDead = 0;
        nextLevel = false;
    }

    @Override
    public void handleInput() {
        player.handleInput();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            dispose();
            gsm.set(new MenuState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
/* ----------------------- FIGURE OUT IF SHIELDS DOWN ----------------------- */
        int blockHitCount = 0;
        boolean shieldsDown = false;
        for (int i = 0; i < blocks.size; i++) {
            BlockGroup bg = blocks.get(i);
            for (int j = 0; j < bg.getArray().size; j++) {
                Block block = bg.getArray().get(j);
                blockHitCount += block.getHits();
            }
        }
        if (blockHitCount == 4 * 4 * 14) {
            shieldsDown = true;
        }
/* ---------------------- COUNT NUMBER OF DEAD ENEMIES ---------------------- */
        enemyDeadCount = 0;
        for (int i = 0; i < enemies.size; i++) {
            EnemyGroup eg = enemies.get(i);
            for (int j = 0; j < eg.getArray().size; j++) {
                Enemy e = eg.getArray().get(j);
                if (e.isDead()) {
                    enemyDeadCount += 1;
                }
            }
        }
        double multiplier = Math.pow(2, -1.0 * level * enemyDeadCount / SPEED_FORMULA_DIVISOR);
        enemyTimeMoveDiff = (long) (ENEMY_TIME_MOVE_START * multiplier);
/* ----------------- FIGURE OUT IF BOTTOM ENEMY LEVEL DEAD  ----------------- */
        enemyLevelDead = true;
        for (int i = 0; i < enemies.size; i++) {
            EnemyGroup eg = enemies.get(i);
            for (int j = 0; j < eg.getArray().size; j++) {
                if (j == enemyLevel && !eg.getArray().get(j).isDead()) {
                    enemyLevelDead = false;
                }
            }
        }
        if (enemyLevelDead) {
            enemyLevel += 1;
        }
/* ------------------------ INCREMENT ENEMY POSITION ------------------------ */
        if (System.currentTimeMillis() - enemyTimeMove > enemyTimeMoveDiff && enemyDeadCount < 5 * ENEMY_ROWS) {
            enemyTimeMove = System.currentTimeMillis();
            enemyMove = true;
            enemyMoveSounds.get(enemyMoveCount % enemyMoveSounds.size).play();
            enemyMoveCount += 1;
        }
        for (int i = 0; i < enemies.size; i++) {
            EnemyGroup eg = enemies.get(i);
            for (int j = 0; j < eg.getArray().size; j++) {
                Enemy e = eg.getArray().get(j);
                if (enemyMove) {
                    e.move();
                }
                e.changeSpeeds(multiplier);
            }
        }
        enemyMove = false;
/* ---------------------------- UPDATE ENEMIES ---------------------------- */
        for (int i = 0; i < enemies.size; i++) {
            EnemyGroup eg = enemies.get(i);
            eg.update(dt, player, blocks);
            for (int j = 0; j < eg.getArray().size; j++) {
                Enemy e = eg.getArray().get(j);
                if (e.getDrops() > Enemy.TOTAL_DROPS + 2) {
                    player.enemiesInvaded();
                }
                if (shieldsDown) {
                    e.setShieldsDown();
                }
                if (enemyLevelDead) {
                    e.drop();
                }
                if (!nextLevel && e.isDeadAnimation() && enemyDeadCount == enemies.size * eg.getArray().size) {
                    nextLevel = true;
                    timeDead = System.currentTimeMillis();
                }
                if (nextLevel && System.currentTimeMillis() - timeDead > PlayState.TIME_RESPAWN) {
                    nextLevel = false;
                    resetEnemies();
                    level += 1;
                    player.addLife();
                    ship.reset();
                    enemyDeadCount = 0;
                    prevNumScore = score.getScore();
                    enemyLevelDead = false;
                    enemyLevel = 0;
                }
            }
        }
/* ---------------------------- UPDATE SPACESHIP ---------------------------- */
        lasersFired = player.getLasersFired() % 15;
        if (lasersFired < 3) {
            ship.update(dt, player, 50);
        } else if (lasersFired < 6) {
            ship.update(dt, player, 100);
        } else if (lasersFired < 9) {
            ship.update(dt, player, 150);
        } else if (lasersFired < 11) {
            ship.update(dt, player, 200);
        } else if (lasersFired < 14) {
            ship.update(dt, player, 250);
        } else {
            ship.update(dt, player, 300);
        }
        prevNumScore += ship.points();
/* ------------------------- UPDATE LIVES AND SCORE ------------------------- */
        int numScore = prevNumScore;
        for (int i = 0; i < enemies.size; i++) {
            EnemyGroup eg = enemies.get(i);
            for (int j = 0; j < eg.getArray().size; j++) {
                numScore += eg.getArray().get(j).getScore();
            }
        }
        score.update(numScore);
        int numLives = player.getNumLives();
        if (player.justDied() && numLives == 0) {
            dispose();
            gsm.set(new GameOverState(gsm, score.getScore()));
        } else {
            lives.update(numLives);
        }
/* -------------------------------------------------------------------------- */
        player.update(dt, enemies, blocks, ship);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, background.getWidth(), background.getHeight());
        player.render(sb);
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).render(sb);
        }
        for (int i = 0; i < blocks.size; i++) {
            BlockGroup blockGroup = blocks.get(i);
            for (int j = 0; j < blockGroup.getArray().size; j++) {
                Block block = blockGroup.getArray().get(j);
                block.render(sb);
            }
        }
        lives.render(sb);
        score.render(sb);
        ship.render(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        player.dispose();
        for (EnemyGroup eg : enemies) {
            eg.dispose();
        }
        for (BlockGroup bg : blocks) {
            bg.dispose();
        }
        score.dispose();
        lives.dispose();
        ship.dispose();
        for (Sound s : enemyMoveSounds) {
            s.dispose();
        }
    }

    public void resetEnemies() {
        enemies = new Array<EnemyGroup>();
        enemyDeadCount = 0;
        enemyMoveCount = 0;
        Texture testEnemy = new Texture("enemy1-v1.png");
        for (int i = 0; i < ENEMY_ROWS; i++) {
            double start = PlayState.EDGES * SpaceInvaders.SCALE + i * ENEMY_SPACING * SpaceInvaders.SCALE +
                    testEnemy.getWidth() / 2 * SpaceInvaders.SCALE +
                    Enemy.MOVE_DISTANCE * Enemy.TOTAL_MOVES / 2 * SpaceInvaders.SCALE;
            enemies.add(new EnemyGroup((float) start, (float) 0.56 * SpaceInvaders.HEIGHT));
        }
    }
}
