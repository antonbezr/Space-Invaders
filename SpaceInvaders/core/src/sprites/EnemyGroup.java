package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.spaceinvaders.game.SpaceInvaders;

public class EnemyGroup {

    public static final int ENEMY_Y_SPACING = 17;

    private Array<Enemy> enemies;
    private int firing;
    private float firstToLast;

    public EnemyGroup(float x, float y) {
        enemies = new Array<Enemy>();

        Texture e0 = new Texture("enemy1-v1.png");
        Texture e1 = new Texture("enemy2-v1.png");
        Texture e2 = new Texture("enemy3-v1.png");

        Enemy enemy0 = new Enemy(x - e0.getWidth() * SpaceInvaders.SCALE / 2, y, 0);
        Enemy enemy1 = new Enemy(x - e0.getWidth() * SpaceInvaders.SCALE / 2,
                              y + ENEMY_Y_SPACING * SpaceInvaders.SCALE, 0);
        Enemy enemy2 = new Enemy(x - e1.getWidth() * SpaceInvaders.SCALE / 2,
                              y + ENEMY_Y_SPACING * SpaceInvaders.SCALE * 2, 1);
        Enemy enemy3 = new Enemy(x - e1.getWidth() * SpaceInvaders.SCALE / 2,
                              y + ENEMY_Y_SPACING * SpaceInvaders.SCALE * 3, 1);
        Enemy enemy4 = new Enemy(x - e2.getWidth() * SpaceInvaders.SCALE / 2,
                              y + ENEMY_Y_SPACING * SpaceInvaders.SCALE * 4, 2);

        firing = 0;
        enemy0.startFiring();

        enemies.add(enemy0);
        enemies.add(enemy1);
        enemies.add(enemy2);
        enemies.add(enemy3);
        enemies.add(enemy4);

        firstToLast = enemy0.getPosition().x + enemy4.getPosition().x +
                      enemy4.getTexture().getWidth() * SpaceInvaders.SCALE;
    }

    public void update(float dt, Player p, Array<BlockGroup> bg) {
        if (firing < enemies.size && enemies.get(firing).isDead()) {
            enemies.get(firing).stopFiring();
            firing += 1;
            if (firing < enemies.size && !enemies.get(firing).isDead()) {
                enemies.get(firing).startFiring();
            }
        }
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).update(dt, p, bg);
        }

    }

    public void render(SpriteBatch sb) {
        for (int i = 0; i < enemies.size; i++) {
            enemies.get(i).render(sb);
        }
    }

    public void dispose() {
        for (int i = 0; i < enemies.size; i++) {
            Enemy e = enemies.get(i);
            e.dispose();
        }
    }

    public float getLength() {
        return firstToLast;
    }

    public Array<Enemy> getArray() {
        return enemies;
    }
}