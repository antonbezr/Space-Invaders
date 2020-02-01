package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spaceinvaders.game.SpaceInvaders;

public class MenuState extends State {

    private static final int SP_HEIGHT = (int) (SpaceInvaders.HEIGHT * 0.625);
    private static final int IC_HEIGHT = (int) (SpaceInvaders.HEIGHT * 0.20);
    private static final int HOVER_THRESHOLD = 5;

    private Texture background;
    private Texture spaceInvaders;
    private Texture insertCoin;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("background.png");
        spaceInvaders = new Texture("space-invaders.png");
        insertCoin = new Texture("insert-coin-off.png");
    }

    @Override
    public void handleInput() {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        if (x > SpaceInvaders.WIDTH / 2 - insertCoin.getWidth() * SpaceInvaders.SCALE / 2 - HOVER_THRESHOLD &&
                x < SpaceInvaders.WIDTH / 2 + insertCoin.getWidth() * SpaceInvaders.SCALE / 2 + HOVER_THRESHOLD &&
                y > SpaceInvaders.HEIGHT - IC_HEIGHT - insertCoin.getHeight() * SpaceInvaders.SCALE - HOVER_THRESHOLD &&
                y < SpaceInvaders.HEIGHT - IC_HEIGHT + HOVER_THRESHOLD){
            insertCoin = new Texture("insert-coin-on.png");
            if (Gdx.input.justTouched()) {
                dispose();
                gsm.set(new PlayState(gsm));
            }
        } else {
            insertCoin = new Texture("insert-coin-off.png");
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, SpaceInvaders.WIDTH, SpaceInvaders.HEIGHT);
        sb.draw(spaceInvaders,
                SpaceInvaders.WIDTH / 2 - spaceInvaders.getWidth() * (SpaceInvaders.SCALE / 2) * 2,
                SP_HEIGHT,
                spaceInvaders.getWidth() * SpaceInvaders.SCALE * 2,
                spaceInvaders.getHeight() * SpaceInvaders.SCALE * 2);
        sb.draw(insertCoin,
                SpaceInvaders.WIDTH / 2 - insertCoin.getWidth() * SpaceInvaders.SCALE / 2,
                IC_HEIGHT,
                insertCoin.getWidth()* SpaceInvaders.SCALE,
                insertCoin.getHeight() * SpaceInvaders.SCALE);
        sb.end();
    }

    public void dispose() {
        background.dispose();
        spaceInvaders.dispose();
        insertCoin.dispose();
    }
}