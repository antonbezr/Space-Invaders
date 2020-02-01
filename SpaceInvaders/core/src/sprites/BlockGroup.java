package sprites;

import com.badlogic.gdx.utils.Array;
import com.spaceinvaders.game.SpaceInvaders;

public class BlockGroup {

    private Array<Block> blocks;

    public BlockGroup(float x, float y) {
        blocks = new Array<Block>();
        Block block0 = new Block(x, y, 0);
        Block block1 = new Block(x, y + block0.getTexture().getHeight() * SpaceInvaders.SCALE, 0);
        Block block2 = new Block(x, y + block0.getTexture().getHeight() * SpaceInvaders.SCALE * 2, 0);
        Block block3 = new Block(x + block0.getTexture().getWidth() * SpaceInvaders.SCALE,
                y + block0.getTexture().getHeight() * SpaceInvaders.SCALE * 2, 0);
        Block block4 = new Block(x + block0.getTexture().getWidth() * SpaceInvaders.SCALE,
                y + block0.getTexture().getHeight() * SpaceInvaders.SCALE * 3, 0);
        Block block5 = new Block(x + block0.getTexture().getWidth() * SpaceInvaders.SCALE * 2,
                y + block0.getTexture().getHeight() * SpaceInvaders.SCALE * 2, 0);
        Block block6 = new Block(x + block0.getTexture().getWidth() * SpaceInvaders.SCALE * 2,
                y + block0.getTexture().getHeight() * SpaceInvaders.SCALE * 3, 0);
        Block block7 = new Block(x + block0.getTexture().getWidth() * SpaceInvaders.SCALE * 3,
                y + block0.getTexture().getHeight() * SpaceInvaders.SCALE * 2, 0);
        Block block8 = new Block(x + block0.getTexture().getWidth() * SpaceInvaders.SCALE * 3,
                y + block0.getTexture().getHeight() * SpaceInvaders.SCALE, 0);
        Block block9 = new Block(x + block0.getTexture().getWidth() * SpaceInvaders.SCALE *3, y, 0);
        Block corner0 = new Block(x, y + block0.getTexture().getHeight() * SpaceInvaders.SCALE * 3, 1);
        Block corner1 = new Block(x + block0.getTexture().getWidth() * SpaceInvaders.SCALE,
                y + block0.getTexture().getHeight() * SpaceInvaders.SCALE, 4);
        Block corner2 = new Block(x + block0.getTexture().getWidth() * SpaceInvaders.SCALE * 2,
                y + block0.getTexture().getHeight() * SpaceInvaders.SCALE, 3);
        Block corner3 = new Block(x + block0.getTexture().getWidth() * SpaceInvaders.SCALE * 3,
                y + block0.getTexture().getHeight() * SpaceInvaders.SCALE * 3, 2);
        blocks.add(block0);
        blocks.add(block1);
        blocks.add(block2);
        blocks.add(block3);
        blocks.add(block4);
        blocks.add(block5);
        blocks.add(block6);
        blocks.add(block7);
        blocks.add(block8);
        blocks.add(block9);
        blocks.add(corner0);
        blocks.add(corner1);
        blocks.add(corner2);
        blocks.add(corner3);
    }

    public void dispose() {
        for (int i = 0; i < blocks.size; i++) {
            Block b = blocks.get(i);
            b.dispose();
        }
    }

    public Array<Block> getArray() {
        return blocks;
    }
}
