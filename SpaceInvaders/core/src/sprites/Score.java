package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.spaceinvaders.game.SpaceInvaders;
import states.PlayState;

public class Score {

    private int score;
    private float scale;
    private Texture s, n0, n1, n2, n3, n4, n5, n6, n7, n8, n9;
    private Array<Texture> numbers;
    private float x, y, old_x, old_y;

    public Score() {
        score = 0;
        scale = 1;
        s = new Texture("score.png");
        n0 = new Texture("w0.png");
        n1 = new Texture("w1.png");
        n2 = new Texture("w2.png");
        n3 = new Texture("w3.png");
        n4 = new Texture("w4.png");
        n5 = new Texture("w5.png");
        n6 = new Texture("w6.png");
        n7 = new Texture("w7.png");
        n8 = new Texture("w8.png");
        n9 = new Texture("w9.png");
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
        x = 0;
        y = 0;
        old_x = PlayState.EDGES * SpaceInvaders.SCALE;
        old_y = SpaceInvaders.HEIGHT - PlayState.EDGES * SpaceInvaders.SCALE - s.getHeight() * SpaceInvaders.SCALE;
    }

    public void update(int s) {
        score = s;
    }

    public void render(SpriteBatch sb) {
        x = old_x;
        y = old_y;

        sb.draw(s, x, y,
                s.getWidth() * SpaceInvaders.SCALE * scale,
                s.getHeight() * SpaceInvaders.SCALE * scale);

        int scoreCopy, s0, s1, s2, s3, s4, s5;

        if (score > 999999) {
            score = 999999;
        }

        scoreCopy = score;

        s0 = scoreCopy / 100000;
        scoreCopy = scoreCopy % 100000;

        s1 = scoreCopy / 10000;
        scoreCopy = scoreCopy % 10000;

        s2 = scoreCopy / 1000;
        scoreCopy = scoreCopy % 1000;

        s3 = scoreCopy / 100;
        scoreCopy = scoreCopy % 100;

        s4 = scoreCopy / 10;
        scoreCopy = scoreCopy % 10;

        s5 = scoreCopy;

        Texture num = numbers.get(s0);
        x += (s.getWidth() + PlayState.NUM_SPACING * 2) * SpaceInvaders.SCALE * scale;
        sb.draw(num, x, y,
                num.getWidth() * SpaceInvaders.SCALE * scale,
                num.getHeight() * SpaceInvaders.SCALE * scale);

        num = numbers.get(s1);
        x += (PlayState.NUM_SPACING + num.getWidth()) * SpaceInvaders.SCALE * scale;
        sb.draw(num, x, y,
                num.getWidth() * SpaceInvaders.SCALE * scale,
                num.getHeight() * SpaceInvaders.SCALE * scale);

        num = numbers.get(s2);
        x += (PlayState.NUM_SPACING + num.getWidth()) * SpaceInvaders.SCALE * scale;
        sb.draw(num, x, y,
                num.getWidth() * SpaceInvaders.SCALE * scale,
                num.getHeight() * SpaceInvaders.SCALE * scale);

        num = numbers.get(s3);
        x += (PlayState.NUM_SPACING + num.getWidth()) * SpaceInvaders.SCALE * scale;
        sb.draw(num, x, y,
                num.getWidth() * SpaceInvaders.SCALE * scale,
                num.getHeight() * SpaceInvaders.SCALE * scale);

        num = numbers.get(s4);
        x += (PlayState.NUM_SPACING + num.getWidth()) * SpaceInvaders.SCALE * scale;
        sb.draw(num, x, y,
                num.getWidth() * SpaceInvaders.SCALE * scale,
                num.getHeight() * SpaceInvaders.SCALE * scale);

        num = numbers.get(s5);
        x += (PlayState.NUM_SPACING + num.getWidth()) * SpaceInvaders.SCALE * scale;
        sb.draw(num, x, y,
                num.getWidth() * SpaceInvaders.SCALE * scale,
                num.getHeight() * SpaceInvaders.SCALE * scale);
    }

    public void dispose() {
        s.dispose();
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

    public void setPositionScale(float y, float m) {
        old_x = SpaceInvaders.WIDTH / 2 -
                (s.getWidth() + n0.getWidth() * 6 +  PlayState.NUM_SPACING * 7) * SpaceInvaders.SCALE * m / 2;
        old_y = y;
        scale = m;
    }

    public void setScore(int s) {
        score = s;
    }

    public int getScore() {
        return score;
    }
}
