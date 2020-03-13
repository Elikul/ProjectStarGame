package ru.geekbrains.base;

import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.UserShip;

public interface ActBonus {

    void Action( UserShip mainShip, EnemyPool enemyPool,BulletPool bulletPool);
}
