package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.UserShip;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.pool.BulletPool;

import com.badlogic.gdx.audio.Music;
import static ru.geekbrains.sprite.UserShip.getSound;


public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 128;

    private TextureAtlas atlas;

    private Texture bg;
    private Background background;
    private UserShip userShip;
    private Star[] stars;
    private BulletPool bulletPool;

    private Music music;


    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        stars = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++){
            stars[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        userShip = new UserShip(atlas,bulletPool);

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();


    }

    @Override
    public void render(float delta) {
        update(delta);
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
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        music.dispose();
        getSound().dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        userShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        userShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        userShip.touchDown(touch,pointer,button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        userShip.touchUp(touch,pointer,button);
        return false;
    }

    private void update(float delta) {
        for (Star star : stars){
            star.update(delta);
        }
        userShip.update(delta);
        bulletPool.updateActiveSprites(delta);
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.9f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for(Star star : stars){
            star.draw(batch);
        }
        userShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        batch.end();
    }
}