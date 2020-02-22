package ru.geekbrains;

import com.badlogic.gdx.Game;
import ru.geekbrains.screen.MenuScreen;

public class MyStarGame extends Game {
	@Override
	public void create() {
		setScreen(new MenuScreen());
	}
}
