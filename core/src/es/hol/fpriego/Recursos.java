package es.hol.fpriego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Recursos {
	
	private AssetManager manager;
	private Skin skin;
	private TiledMap mapa1;
	
	public Recursos(){
		manager = new AssetManager();
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
	}

	public AssetManager getManager() {
		return manager;
	}
	
	public void loadMap1(){
		mapa1 = new TmxMapLoader().load("maps/los-mapa1.tmx");
	}
	
	public void unLoadMap1(){
		mapa1.dispose();
	}
	
	public TiledMap getMapa1() {
		return mapa1;
	}

	public Skin getSkin() {
		return skin;
	}

	public void dispose(){
		manager.dispose();
		skin.dispose();
	}
}
