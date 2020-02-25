package ru.geekbrains.base;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;

public class SpaceShip extends Sprite{

    public final int PARTCOUNT = 2;
    public final int LEFTWIDTH;
    public final int REG_X;

    public SpaceShip(TextureRegion region) {
        super(region);
        this.regions = new TextureRegion[1];
        this.regions[0] = region;
        LEFTWIDTH = region.getRegionWidth()/PARTCOUNT;
        REG_X = region.getRegionX();
        regions[0].setRegionX(REG_X-LEFTWIDTH);
        regions[0].setRegionWidth(LEFTWIDTH);
    }


    public void setLeftHalf() {
        regions[0].setRegionX(REG_X);
        regions[0].setRegionWidth(LEFTWIDTH);
    }

    public void setRightHalf() {
        regions[0].setRegionWidth(LEFTWIDTH);
        regions[0].setRegionX(REG_X + LEFTWIDTH);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
    }

    @Override
    public void touchDown(Vector2 touch, int pointer, int button) {
        super.touchDown(touch, pointer, button);
    }

    @Override
    public void touchUp(Vector2 touch, int pointer, int button) {
        super.touchUp(touch, pointer, button);
    }
}
