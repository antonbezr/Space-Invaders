package sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.spaceinvaders.game.SpaceInvaders;

public class Block {

    private Texture block, b0, b1, b2, b3 ,b4;
    private Array<Texture> blocks;
    private Vector2 position;
    private int health;
    private int hits;
    private Rectangle bounds;

    public Block(float x, float y, int v) {
        drawBlock(v);
        block = b0;
        blocks = new Array<Texture>();
        blocks.add(b0);
        blocks.add(b1);
        blocks.add(b2);
        blocks.add(b3);
        blocks.add(b4);
        position = new Vector2(x, y);
        hits = 0;
        health = 0;
        bounds = new Rectangle(x, y, block.getWidth() * SpaceInvaders.SCALE,
                block.getHeight() * SpaceInvaders.SCALE);
        bounds.setPosition(x, y);
    }

    public void render(SpriteBatch sb) {
        sb.draw(block, position.x, position.y,
                block.getWidth() * SpaceInvaders.SCALE, block.getHeight() * SpaceInvaders.SCALE);
    }

    public void dispose() {
        block.dispose();
        b0.dispose();
        b1.dispose();
        b2.dispose();
        b3.dispose();
        b4.dispose();
        for (Texture t : blocks) {
            t.dispose();
        }
    }

    public void drawBlock(int r) {
        if (r == 0) {
            b0 = new Texture("full-block-1.png");
            b1 = new Texture("full-block-2.png");
            b2 = new Texture("full-block-3.png");
            b3 = new Texture("full-block-4.png");
        }
        if (r == 1) {
            b0 = new Texture("tl-corner-1.png");
            b1 = new Texture("tl-corner-2.png");
            b2 = new Texture("tl-corner-3.png");
            b3 = new Texture("tl-corner-4.png");
        } else if (r == 2) {
            b0 = new Texture("tr-corner-1.png");
            b1 = new Texture("tr-corner-2.png");
            b2 = new Texture("tr-corner-3.png");
            b3 = new Texture("tr-corner-4.png");
        } else if (r == 3) {
            b0 = new Texture("bl-corner-1.png");
            b1 = new Texture("bl-corner-2.png");
            b2 = new Texture("bl-corner-3.png");
            b3 = new Texture("bl-corner-4.png");
        } else if (r == 4) {
            b0 = new Texture("br-corner-1.png");
            b1 = new Texture("br-corner-2.png");
            b2 = new Texture("br-corner-3.png");
            b3 = new Texture("br-corner-4.png");
        }
        b4 = new Texture("full-block-5.png");
    }

    public void loseHealth() {
        health += 1;
        if (health < 5) {
            block = blocks.get(health);
        }
        if (health > 3) {
            position.x = Integer.MIN_VALUE;
            position.y = Integer.MAX_VALUE;
            bounds.setPosition(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        hits += 1;
    }

    public Texture getTexture() {
        return block;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getHits() {
        return hits;
    }
}