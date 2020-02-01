package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spaceinvaders.game.SpaceInvaders;

import sprites.Score;

public class GameOverState extends State {

    private static final int GO_HEIGHT = (int) (SpaceInvaders.HEIGHT * 0.7);
    private static final int SC_HEIGHT = (int) (SpaceInvaders.HEIGHT * 0.53);
    private static final int IC_HEIGHT = (int) (SpaceInvaders.HEIGHT * 0.20);
    private static final float SCORE_SCALE = (float) 1.5;
    private static final int HOVER_THRESHOLD = 5;

    private Texture background;
    private Texture gameOver;
    private Texture insertCoin;
    private Score score;

    public GameOverState(GameStateManager gsm, int s) {
        super(gsm);
        background = new Texture("background.png");
        gameOver = new Texture("game-over.png");
        insertCoin = new Texture("insert-coin-off.png");
        score = new Score();
        score.setScore(s);
        score.setPositionScale(SC_HEIGHT, SCORE_SCALE);
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
        sb.draw(gameOver,
                SpaceInvaders.WIDTH / 2 - gameOver.getWidth() * SpaceInvaders.SCALE / 2 * 2,
                GO_HEIGHT,
                gameOver.getWidth() * SpaceInvaders.SCALE * 2,
                gameOver.getHeight() * SpaceInvaders.SCALE * 2);
        sb.draw(insertCoin,
                SpaceInvaders.WIDTH / 2 - insertCoin.getWidth() * SpaceInvaders.SCALE / 2,
                IC_HEIGHT,
                insertCoin.getWidth()* SpaceInvaders.SCALE,
                insertCoin.getHeight() * SpaceInvaders.SCALE);
        score.render(sb);
        sb.end();
    }

    public void dispose() {
        background.dispose();
        gameOver.dispose();
        insertCoin.dispose();
    }
}
