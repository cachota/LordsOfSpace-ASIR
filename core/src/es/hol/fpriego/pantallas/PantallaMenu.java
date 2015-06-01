package es.hol.fpriego.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import es.hol.fpriego.Constantes;
import es.hol.fpriego.Main;

public class PantallaMenu implements Screen{

	private Main game;
	private AssetManager manager;
	private Stage stage;
	private TextureAtlas atlas;
	private Image imgFondo;
	private TextButton botonJugar , botonCreditos;
	private Label titulo;
	private Skin skin;
	private Group grupoBotones;
	private SequenceAction desapareceBotones;
	private Sound sonidoBoton;
	
	public PantallaMenu(Main game) {
		this.game = game;
	}

	@Override
	public void show() {
		
		manager = game.getRecursos().getManager();
		skin = game.getRecursos().getSkin();
		atlas = manager.get("atlas/los04.atlas");
		sonidoBoton = manager.get("sound/sfx-twoTone.ogg", Sound.class);
		
		stage = new Stage(new FitViewport(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA));
		grupoBotones = new Group();
		imgFondo = new Image(atlas.findRegion("fondoMenu"));
		titulo = new Label("LORDS OF SPACE", skin);
		titulo.setFontScale(2f);
		titulo.setPosition(50, Constantes.ALTO_PANTALLA/1.4f);
		titulo.setWidth(Constantes.ANCHO_BOTONES);
		botonJugar = new TextButton("JUGAR", skin);
		botonJugar.setPosition(150, Constantes.ALTO_PANTALLA/2f);
		botonJugar.setWidth(Constantes.ANCHO_BOTONES);
		botonCreditos = new TextButton("CREDITOS", skin);
		botonCreditos.setPosition(150, Constantes.ALTO_PANTALLA/3f);
		botonCreditos.setWidth(Constantes.ANCHO_BOTONES);
		
		grupoBotones.addActor(titulo);
		grupoBotones.addActor(botonJugar);
		grupoBotones.addActor(botonCreditos);
		
		stage.addActor(imgFondo);
		stage.addActor(grupoBotones);
		
		Gdx.input.setInputProcessor(stage);
		
		grupoBotones.addAction(Actions.sequence(Actions.fadeOut(0f),Actions.fadeIn(1f)));
		desapareceBotones = new SequenceAction(Actions.fadeOut(1f));
		
		botonJugar.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				sonidoBoton.play();
				botonJugar.removeListener(botonJugar.getListeners().get(0));
				grupoBotones.addAction(Actions.sequence(desapareceBotones,new Action(){

					@Override
					public boolean act(float delta) {
						game.getRecursos().loadMap1();
						game.setScreen(game.getLevel1());
						return false;
					}
				}));
			}
		});
		
		botonCreditos.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				sonidoBoton.play();
				botonCreditos.removeListener(botonJugar.getListeners().get(0));
				grupoBotones.addAction(Actions.sequence(desapareceBotones,new Action(){

					@Override
					public boolean act(float delta) {
						game.getRecursos().loadMap1();
						game.setScreen(game.getLevel1());
						return false;
					}
					
				}));
			}
		});
		
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act(delta);
        stage.draw();
        
        if(Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
        	Gdx.app.exit();
        }
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
