package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.spaceinvaders.game.SpaceInvaders;
import states.PlayState;

public class Lives {

    int lives;
    Texture l, n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;
    Array<Texture> numbers;

    public Lives() {
        lives = 0;
        l = new Texture("lives.png");
        n0 = new Texture("g0.png");
        n1 = new Texture("g1.png");
        n2 = new Texture("g2.png");
        n3 = new Texture("g3.png");
        n4 = new Texture("g4.png");
        n5 = new Texture("g5.png");
        n6 = new Texture("g6.png");
        n7 = new Texture("g7.png");
        n8 = new Texture("g8.png");
        n9 = new Texture("g9.png");
        numbers = new Array<Texture>();
        numbers.add(n0);
        numbers.add(n1);
        numbers.add(n2);
        numbers.add(n3);
        numbers.add(n4);
        numbers.add(n5);
        numbers.add(n6);
        numbers.add(n7);
        numbers.add(n8);
        numbers.add(n9);
    }

    public void update(int l) {
        lives = l;
    }

    public void render(SpriteBatch sb) {
        int livesCopy, l0, l1;
        if (lives > 99) {
            lives = 99;
        }
        livesCopy = lives;
        l0 = livesCopy / 10;
        livesCopy = livesCopy % 10;
        l1 = livesCopy;
        Texture num = numbers.get(l0);
        sb.draw(l,
                SpaceInvaders.WIDTH - (2 * num.getWidth() + l.getWidth()) * SpaceInvaders.SCALE
                    - PlayState.EDGES * SpaceInvaders.SCALE - PlayState.NUM_SPACING * SpaceInvaders.SCALE * 3,
                SpaceInvaders.HEIGHT - l.getHeight() * SpaceInvaders.SCALE - PlayState.EDGES * SpaceInvaders.SCALE,
                l.getWidth() * SpaceInvaders.SCALE,
                l.getHeight() * SpaceInvaders.SCALE);
        sb.draw(num,
                SpaceInvaders.WIDTH - 2 * num.getWidth() * SpaceInvaders.SCALE
                    - PlayState.EDGES * SpaceInvaders.SCALE - PlayState.NUM_SPACING * SpaceInvaders.SCALE,
                SpaceInvaders.HEIGHT - num.getHeight() * SpaceInvaders.SCALE - PlayState.EDGES * SpaceInvaders.SCALE,
                num.getWidth() * SpaceInvaders.SCALE,
                num.getHeight() * SpaceInvaders.SCALE);
        num = numbers.get(l1);
        sb.draw(num,
                SpaceInvaders.WIDTH - num.getWidth() * SpaceInvaders.SCALE - PlayState.EDGES * SpaceInvaders.SCALE,
                SpaceInvaders.HEIGHT - num.getHeight() * SpaceInvaders.SCALE - PlayState.EDGES * SpaceInvaders.SCALE,
                num.getWidth() * SpaceInvaders.SCALE,
                num.getHeight() * SpaceInvaders.SCALE);
    }

    public void dispose() {
        l.dispose();
        n0.dispose();
        n1.dispose();
        n2.dispose();
        n3.dispose();
        n4.dispose();
        n5.dispose();
        n6.dispose();
        n7.dispose();
        n8.dispose();
        n9.dispose();
        for (int i = 0; i < numbers.size; i++) {
            Texture n = numbers.get(i);
            n.dispose();
        }
    }
}
