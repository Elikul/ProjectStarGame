package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.Sprite;

public class GameName extends Sprite {


    public GameName(TextureAtlas region) {
        super(region.findRegion("game_name"));
        setHeightProportion(0.1f);
        setBottom(-0.0f);
    }

}