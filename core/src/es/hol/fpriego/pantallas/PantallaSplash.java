package es.hol.fpriego.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

import es.hol.fpriego.Constantes;
import es.hol.fpriego.Main;

public class PantallaSplash implements Screen{

	private Main game;
	private Stage stage;
	private NinePatch nPatch;
	private AssetManager manager;
	private Image imgCarga , imgFondo;
	private Label textCarga;
	
	public PantallaSplash(Main game) {
		this.game = game;
	}

	@Override
	public void show() {
		
		manager = game.getRecursos().getManager();
		manager.load("loading.png", Texture.class);
		manager.load("fondoMenu.png", Texture.class);
		manager.finishLoading();
		
		stage = new Stage(new FitViewport(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA));
		imgFondo = new Image(manager.get("fondoMenu.png", Texture.class));
		nPatch = new NinePatch((Texture) manager.get("loading.png"),10,10,10,10);
		imgCarga = new Image(nPatch);
		imgCarga.setPosition(156, Constantes.ALTO_PANTALLA/2);
		textCarga = new Label("Loading...", game.getRecursos().getSkin());
		textCarga.setPosition(156, Constantes.ALTO_PANTALLA/2 + 60);
		
		stage.addActor(imgFondo);
		stage.addActor(imgCarga);
		stage.addActor(textCarga);
		
		if(manager.update()){
        	
        	manager.load("atlas/los04.atlas", TextureAtlas.class);
        	manager.load("sound/sfx-laser1.ogg", Sound.class);
        	manager.load("sound/sfx-laser2.ogg", Sound.class);
        	manager.load("sound/sfx-lose.ogg", Sound.class);
        	manager.load("sound/sfx-shieldDown.ogg", Sound.class);
        	manager.load("sound/sfx-shieldUp.ogg", Sound.class);
        	manager.load("sound/sfx-twoTone.ogg", Sound.class);
        	manager.load("sound/sfx-zap.ogg", Sound.class);
        }
		
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(manager.update()){
        	game.setScreen(game.getMenu());
        }
        else{
        	imgCarga.addAction(Actions.sizeTo(manager.getProgress()*500, imgCarga.getHeight()));
        }
        
        stage.act(delta);
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		stage.clear();
		stage.dispose();
	}

}
