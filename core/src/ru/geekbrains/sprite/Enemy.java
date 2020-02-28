package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.pool.BulletPool;


public class Enemy extends Sprite {

    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;

    private Vector2 v;
    private Vector2 v0;
    private Vector2 bulletV;

    protected Rect worldBounds;

    protected int damage;
    protected Sound bulletSound;



    public Enemy(BulletPool bulletPool, Sound bulletSound, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.bulletSound = bulletSound;
        this.worldBounds = worldBounds;
        this.v = new Vector2();
        this.v0 = new Vector2();
        this.bulletV = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletVY,
            int damage,
            float height
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        setHeightProportion(height);
        this.v.set(v0);
    }
}
