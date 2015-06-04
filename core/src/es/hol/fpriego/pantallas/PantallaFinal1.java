package es.hol.fpriego.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import es.hol.fpriego.Constantes;
import es.hol.fpriego.Main;

public class PantallaFinal1 implements Screen{

	private Main game;
	private AssetManager manager;
	private Stage stage;
	private TextureAtlas atlas;
	private Image imgFondo;
	private Label titulo;
	private Skin skin;
	private SequenceAction desapareceBotones;
	
	public PantallaFinal1(Main game) {
		this.game = game;
	}

	@Override
	public void show() {
		
		manager = game.getRecursos().getManager();
		skin = game.getRecursos().getSkin();
		atlas = manager.get("atlas/los04.atlas");
		
		stage = new Stage(new FitViewport(Constantes.ANCHO_PANTALLA, Constantes.ALTO_PANTALLA));
		imgFondo = new Image(atlas.findRegion("fondoMenu"));
		titulo = new Label("Nivel 1 Completado!", skin);
		titulo.setFontScale(1.5f);
		titulo.setPosition(130, Constantes.ALTO_PANTALLA/1.8f);
		titulo.setWidth(Constantes.ANCHO_BOTONES);
		titulo.setAlignment(Align.center);;
		
		stage.addActor(imgFondo);
		stage.addActor(titulo);
		
		Gdx.input.setInputProcessor(stage);
		
		titulo.addAction(Actions.sequence(Actions.fadeOut(0f),Actions.fadeIn(1f)));
		desapareceBotones = new SequenceAction(Actions.fadeOut(1f));
		
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
        	titulo.addAction(Actions.sequence(desapareceBotones,new Action(){

				@Override
				public boolean act(float delta) {
					game.setScreen(game.getMenu());
					return false;
				}
				
			}));
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
