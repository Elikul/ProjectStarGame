package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.ActBonus;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.BonusPool;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.Bonus;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.UserShip;

public class BonusEmitter {
    private static final Vector2 v0 = new Vector2(0, -0.2f);
    private static final float HEIGHT = 0.4f;

    private Rect worldBounds;
    private BonusPool bonusPool;
    private EnemyPool enemyPool;
    private BulletPool bulletPool;
    private UserShip userShip;

    private float generateInterval = 10f;
    private float generateTimer;

    private final TextureRegion bluePill;
    private final TextureRegion greenPill;
    private final TextureRegion half_health;
    private final TextureRegion bomb;



    public BonusEmitter(TextureAtlas atlas, BonusPool bonusPool, UserShip userShip, EnemyPool enemyPool, BulletPool bulletPool, Rect worldBounds) {
        this.bonusPool = bonusPool;
        this.userShip = userShip;
        this.enemyPool = enemyPool;
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        this.bluePill = atlas.findRegion("blue_pill");
        this.greenPill = atlas.findRegion("green_pill");
        this.half_health = atlas.findRegion("half_health");
        this.bomb = atlas.findRegion("bomb");
    }



    public void generateBonus(float delta) {
        generateTimer += delta;
        if (generateTimer >= generateInterval) {
            generateTimer = 0f;
            ActBonus killAll  = new ActBonus() {
                @Override
                public void Action(UserShip userShip, EnemyPool enemyPool, BulletPool bulletPool) {
                    for (Enemy enemy : enemyPool.getActiveObjects()) {
                        enemy.destroy();
                    }
                    for (Bullet bullet : bulletPool.getActiveObjects()) {
                        bullet.destroy();
                    }

                }
            };

            ActBonus health10 = new ActBonus() {
                @Override
                public void Action(UserShip userShip, EnemyPool enemyPool, BulletPool bulletPool) {
                    userShip.addHP(10);
                }
            };
            ActBonus health30 = new ActBonus() {
                @Override
                public void Action(UserShip userShip, EnemyPool enemyPool, BulletPool bulletPool) {
                    userShip.addHP(30);
                }
            };
            ActBonus health50 = new ActBonus() {
                @Override
                public void Action(UserShip userShip, EnemyPool enemyPool, BulletPool bulletPool) {
                    userShip.addHP(50);
                }
            };

            Bonus bonus = bonusPool.obtain();

            float type = (float) Math.random();
            if (type < 0.5f)
            {
                bonus.setAction(health10);
                bonus.set(
                        bluePill,
                        v0,
                        HEIGHT
                );
            } else if (type < 0.7f)
            {
                bonus.setAction(killAll);
                bonus.set(
                        bomb,
                        v0,
                        HEIGHT
                );
            }
            else if (type < 0.8f)
            {
                bonus.setAction(health30);
                bonus.set(
                        greenPill,
                        v0,
                        HEIGHT
                );
            }
            else if (type < 0.9f)
            {
                bonus.setAction(health50);
                bonus.set(
                        half_health,
                        v0,
                        HEIGHT
                );
            }

            bonus.setBulletPool(bulletPool);
            bonus.setWorldBounds(worldBounds);
            bonus.setEnemyPool(enemyPool);
            bonus.setUserShip(userShip);
            bonus.setBottom(worldBounds.getTop());
            bonus.pos.x = Rnd.nextFloat(worldBounds.getLeft() + bonus.getHalfWidth(), worldBounds.getRight() - bonus.getHalfWidth());
        }
    }
}
