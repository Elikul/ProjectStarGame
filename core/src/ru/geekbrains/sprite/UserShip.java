package ru.geekbrains.sprite;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.SpaceShip;
import ru.geekbrains.math.Rect;

public class UserShip extends SpaceShip {

    protected static float LEN = 0.03f;

    private Vector2 touch;
    private Vector2 v0;
    private Vector2 v;

    public UserShip(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        touch = new Vector2();
        v0 = new Vector2();
        v = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        v0.set(touch);
        if (v0.sub(pos).len() <= LEN) {
            pos.set(touch);
            setLeftHalf();
        } else {
            pos.add(v);
            setRightHalf();
        }

    }

    @Override
    public void resize(Rect wordBounds) {
        setHeightProportion(0.15f);
        setBottom(wordBounds.getBottom() + 0.03f);
    }

    @Override
    public void touchDown(Vector2 touch, int pointer, int button) {
        this.touch = touch;
        v.set(touch.cpy().sub(pos).setLength(LEN));
    }

    @Override
    public void touchUp(Vector2 touch, int pointer, int button) {
        this.touch = touch;
        v.set(touch.cpy().sub(pos).setLength(LEN));
    }

}
