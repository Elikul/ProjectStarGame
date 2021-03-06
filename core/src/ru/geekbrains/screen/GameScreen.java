package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.pool.BonusPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.UserShip;
import ru.geekbrains.sprite.MessageGameOver;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.Bonus;
import ru.geekbrains.utils.EnemiesEmitter;
import ru.geekbrains.utils.BonusEmitter;
import ru.geekbrains.sprite.TrackingStar;
import ru.geekbrains.sprite.ButtonNewGame;
import ru.geekbrains.utils.Font;


public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 128;
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags:";
    private static final String HP = "HP:";
    private static final String LEVEL = "Level:";
    private static final float PADDING = 0.01f;

    private enum State {PLAYING, PAUSE, GAME_OVER}

    private TextureAtlas atlas;
    private TextureAtlas atlas_names;
    private TextureAtlas atlas_bonus;

    private Texture bg;
    private Background background;
    private UserShip userShip;
    private TrackingStar[] stars;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

    private Music music;
    private Sound bulletSound;
    private Sound explosionSound;

    private EnemiesEmitter enemiesEmitter;

    private State state;
    private State pervState;

    private MessageGameOver messageGameOver;
    private ButtonNewGame buttonNewGame;

    private Font font;

    private int frags;

    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;

    private BonusEmitter bonusEmitter;
    private BonusPool bonusPool;

    @Override
    public void show() {
        super.show();
        bg = new Texture("space.jpg");
        background = new Background(bg);
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        atlas_names = new TextureAtlas(Gdx.files.internal("names/names.tpack"));
        atlas_bonus = new TextureAtlas(Gdx.files.internal("bonuses/bonus.tpack"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        stars = new TrackingStar[STAR_COUNT];
        enemyPool = new EnemyPool(bulletPool, explosionPool, bulletSound, worldBounds);
        enemiesEmitter = new EnemiesEmitter(atlas, enemyPool, worldBounds);
        bonusPool = new BonusPool(userShip,enemyPool,bulletPool,worldBounds);
        bonusEmitter = new BonusEmitter(atlas_bonus,bonusPool,userShip,enemyPool,bulletPool,worldBounds);
        userShip = new UserShip(atlas, bulletPool, explosionPool);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new TrackingStar(atlas, userShip.getV());
        }
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        messageGameOver = new MessageGameOver(atlas_names);
        buttonNewGame = new ButtonNewGame(atlas_names, this);
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        frags = 0;
        state = State.PLAYING;
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();

    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars){
            star.resize(worldBounds);
        }
        userShip.resize(worldBounds);
        messageGameOver.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }


    @Override
    public void pause() {
        music.pause();
        pervState = state;
        state = State.PAUSE;
    }

    @Override
    public void resume() {
        music.play();
        state = pervState;
    }


    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        music.dispose();
        bulletSound.dispose();
        explosionSound.dispose();
        userShip.dispose();
        bonusPool.dispose();
        font.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            userShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            userShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            userShip.touchDown(touch, pointer, button);
        }else if (state == State.GAME_OVER) {
            buttonNewGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            userShip.touchUp(touch, pointer, button);
        }else if (state == State.GAME_OVER) {
            buttonNewGame.touchUp(touch, pointer, button);
        }
        return false;
    }


    private void update(float delta) {
        for (Star star : stars){
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            userShip.update(delta);
            bonusPool.updateActiveSprites(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemiesEmitter.generate(delta,frags);
            //bonusEmitter.generateBonus(delta);

        }
    }

    private void checkCollisions() {
        if (state != State.PLAYING) {
            return;
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            float minDist = userShip.getHalfWidth() + enemy.getHalfWidth();
            if (userShip.pos.dst(enemy.pos) <= minDist) {
                enemy.destroy();
                userShip.damage(enemy.getDamage());
                frags++;
            }
        }
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() != userShip) {
                if (userShip.isBulletCollision(bullet)) {
                    userShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
                continue;
            }
            for (Enemy enemy : enemyList) {
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                    if (enemy.isDestroyed()) {
                        frags++;
                    }
                }
            }
        }
        List<Bonus> bonusList = bonusPool.getActiveObjects();
        for (Bonus bonus : bonusList)
        {
            if (bonus.isDestroyed()) {
                continue;
            }
            if(userShip.isBulletCollision(bonus)){
                bonus.destroy();
                bonus.doAction();

            }
        }
        if (userShip.isDestroyed()) {
            state = State.GAME_OVER;
        }
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
        bonusPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.9f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for(Star star : stars){
            star.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        if (state == State.PLAYING) {
            userShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
            bonusPool.drawActiveSprites(batch);
        } else if (state == State.GAME_OVER) {
            messageGameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        printInfo();
        batch.end();
    }

    public void startNewGame() {
        state = State.PLAYING;
        frags = 0;
        userShip.startNewGame();

        bulletPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        bonusPool.freeAllActiveObjects();

    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHp.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + PADDING, worldBounds.getTop() - PADDING);
        font.draw(batch, sbHp.append(HP).append(userShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - PADDING, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemiesEmitter.getLevel()), worldBounds.getRight() - PADDING, worldBounds.getTop() - PADDING, Align.right);
    }

}