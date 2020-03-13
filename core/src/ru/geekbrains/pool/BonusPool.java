package ru.geekbrains.pool;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Bonus;
import ru.geekbrains.sprite.UserShip;


public class BonusPool extends SpritesPool<Bonus> {

    private UserShip userShip;
    private EnemyPool enemyPool;
    private BulletPool bulletPool;
    private Rect worldBounds;

    public BonusPool(UserShip userShip, EnemyPool enemyPool, BulletPool bulletPool, Rect worldBounds) {
        this.userShip = userShip;
        this.enemyPool = enemyPool;
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
    }


    @Override
    protected Bonus newObject() {
        return new Bonus(userShip,enemyPool,bulletPool,worldBounds);
    }
}
