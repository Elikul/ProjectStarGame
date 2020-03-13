package ru.geekbrains.sprite;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.ActBonus;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;

public class Bonus extends Sprite {

    protected TextureRegion textureRegion;
    private Rect worldBounds;
    private Vector2 v;
    private EnemyPool enemyPool;
    private UserShip userShip;
    private BulletPool bulletPool;
    private ActBonus action;


    public Bonus(UserShip userShip, EnemyPool enemyPool, BulletPool bulletPool, Rect worldBounds) {
        this.userShip = userShip;
        this.enemyPool = enemyPool;
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.v = new Vector2();
    }

    public void setWorldBounds(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    public void setV(Vector2 v) {
        this.v = v;
    }

    public void setEnemyPool(EnemyPool enemyPool) {
        this.enemyPool = enemyPool;
    }

    public void setUserShip(UserShip userShip) {
        this.userShip = userShip;
    }

    public void setBulletPool(BulletPool bulletPool) {
        this.bulletPool = bulletPool;
    }


   public void setAction(ActBonus action) {
        this.action = action;
    }


    @Override
    public void update(float delta) {
        this.pos.mulAdd(v, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public void set(
            TextureRegion region,
            Vector2 v0,
            float height
    ) {
        this.textureRegion = region;
        this.v.set(v0);
        setHeightProportion(height); //NullPointerException не понятно почему
    }

    public void doAction(){
        action.Action(userShip,enemyPool,bulletPool);
    }

}
