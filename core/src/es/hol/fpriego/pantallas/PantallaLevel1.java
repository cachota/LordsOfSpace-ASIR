package es.hol.fpriego.pantallas;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import es.hol.fpriego.Constantes;
import es.hol.fpriego.Constantes.ESTADOS_LEVEL_1;
import es.hol.fpriego.Main;
import es.hol.fpriego.entidades.Asteroide;
import es.hol.fpriego.entidades.DisparoPlayer;
import es.hol.fpriego.entidades.Invader;
import es.hol.fpriego.entidades.NaveEnemiga1;
import es.hol.fpriego.entidades.Player;
import es.hol.fpriego.entidades.PowerUp;

public class PantallaLevel1 implements Screen{

	private Main game;
	private Stage hudStage , gameStage;
	private TiledMap mapa;
	private TiledMapRenderer mapRenderer;
	private OrthographicCamera hudCamera , gameCamera;
	private AssetManager manager;
	private Skin skin;
	private Touchpad pad;
	private TextButton botonDisparo;
	private TextureAtlas atlas;
	private Image barraVida , miniPlayer , vida;
	private NinePatch nPatch;
	private Player player;
	private boolean avanza , pausa;
	private float timePausa;
	private Label textReady , fps;
	private ESTADOS_LEVEL_1 estadosLevel1;
	private Group grupoInvaders;
	private int velInvaders;
	private Window ventanaGameOver, ventanaPausa;
	private TextButton botonSalir, botonReplay, botonSalirPausa, botonResumir;
	private Label textGameOver, textPausa;
	private Sound sonidoBoton, sonidoPowDisp, sonidoPowShield, sonidoPowLoose;
	private Array<PowerUp> powerUps;
	private Array<Invader> invaders;
	private Array<Asteroide> asteroides;
	private Array<NaveEnemiga1> naves1;
	private boolean maxInvader;
	private int contadorInvaders;
	
	public PantallaLevel1(Main game) {
		this.game = game;
	}

	@Override
	public void show() {
		
		manager = game.getRecursos().getManager();
		skin = game.getRecursos().getSkin();
		atlas = manager.get("atlas/los04.atlas", TextureAtlas.class);
		avanza = false;
		pausa = false;
		timePausa = 0;
		estadosLevel1 = ESTADOS_LEVEL_1.INICIO;
		velInvaders = 10;
		powerUps = new Array<PowerUp>();
		invaders = new Array<Invader>();
		asteroides = new Array<Asteroide>();
		naves1 = new Array<NaveEnemiga1>();
		maxInvader = false;
		
		sonidoBoton = manager.get("sound/sfx-twoTone.ogg", Sound.class);
		sonidoPowDisp = manager.get("sound/sfx-zap.ogg", Sound.class);
		sonidoPowShield = manager.get("sound/sfx-shieldUp.ogg", Sound.class);
		sonidoPowLoose = manager.get("sound/sfx-shieldDown.ogg", Sound.class);
		
		hudCamera = new OrthographicCamera(Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA);
		hudCamera.setToOrtho(false, Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA);
		hudStage = new Stage(new FitViewport(Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA,hudCamera));
		gameCamera = new OrthographicCamera(Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA);
		gameCamera.setToOrtho(false, Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA);
		gameStage = new Stage(new FitViewport(Constantes.ANCHO_PANTALLA,Constantes.ALTO_PANTALLA,gameCamera));
		
		mapa = game.getRecursos().getMapa1();
		mapRenderer = new OrthogonalTiledMapRenderer(mapa);
		
		barraVida = new Image(atlas.findRegion("barraVida"));
		barraVida.setPosition(540, 960);
		nPatch = new NinePatch((Texture) manager.get("loading.png"),10,10,10,10);
		vida = new Image(nPatch);
		vida.setSize(barraVida.getWidth()-8, barraVida.getHeight()-8);
		vida.setPosition(544, 964);
		miniPlayer = new Image(atlas.findRegion("playerLife"));
		miniPlayer.setPosition(500, 960);
		textReady = new Label("READY!", skin);
		textReady.setPosition(300, Constantes.ALTO_PANTALLA/2);
		textReady.setAlignment(Align.center);
		fps = new Label("",skin);
		fps.setPosition(20, 960);
		ventanaGameOver = new Window("",skin);
		ventanaGameOver.setSize(500, 240);
		ventanaGameOver.setModal(true);
		botonSalir = new TextButton("salir", skin);
		botonReplay = new TextButton("replay", skin);
		textGameOver = new Label("game over",skin);
		ventanaGameOver.add(textGameOver).center().padTop(0).expand().colspan(2).row();
		ventanaGameOver.add(botonSalir).width(200).center().expand();
		ventanaGameOver.add(botonReplay).width(200).center().expand();
		ventanaGameOver.getCells().get(0).colspan(2);
		ventanaGameOver.setPosition(134, Constantes.ALTO_PANTALLA/2.5f);
		
		ventanaPausa = new Window("",skin);
		ventanaPausa.setSize(500, 240);
		ventanaPausa.setModal(true);
		botonSalirPausa = new TextButton("salir", skin);
		botonResumir = new TextButton("volver", skin);
		textPausa = new Label("pausa",skin);
		ventanaPausa.add(textPausa).center().padTop(0).expand().colspan(2).row();
		ventanaPausa.add(botonSalirPausa).width(200).center().expand();
		ventanaPausa.add(botonResumir).width(200).center().expand();
		ventanaPausa.getCells().get(0).colspan(2);
		ventanaPausa.setPosition(134, Constantes.ALTO_PANTALLA/2.5f);
		
		if(Gdx.app.getType()==ApplicationType.Android){
			
			pad = new Touchpad(1, skin);
			pad.setPosition(100, 100);
			botonDisparo = new TextButton("FIRE", skin);
			botonDisparo.setPosition(500, 80);
			botonDisparo.setSize(200, 100);
			
			botonDisparo.addListener(new ChangeListener() {
				
				@Override
				public void changed(ChangeEvent event, Actor actor) {	
					if(estadosLevel1 != ESTADOS_LEVEL_1.INICIO)
						player.disparar();
				}
			});
			hudStage.addActor(pad);
			hudStage.addActor(botonDisparo);
		}
		
		hudStage.addActor(fps);
		hudStage.addActor(barraVida);
		hudStage.addActor(vida);
		hudStage.addActor(miniPlayer);
		hudStage.addActor(textReady);
		
		player = new Player(pad,atlas,manager);
		player.setPosition(Constantes.ANCHO_PANTALLA/2 - player.getWidth(), 200);
		grupoInvaders = new Group();
		
		gameStage.addActor(player);
		gameStage.addActor(grupoInvaders);
		
		float x = 0, y = 0;
		for(int i=0;i<4;i++){
			for(int j=0;j<6;j++){
				int tipo = i%2==0 ? 1 : 2;
				Invader in = new Invader(atlas, tipo, 1);
				grupoInvaders.addActor(in);
				in.setPosition(x, y);
				x += 90;
			}
			x = 0;
			y += 80;
		}
		
		grupoInvaders.setPosition(50, Constantes.ALTO_PANTALLA);
		grupoInvaders.setTransform(false);
		
		Gdx.input.setInputProcessor(hudStage);
		
		botonSalir.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sonidoBoton.play();
				game.setScreen(game.getMenu());
			}
		});
		
		botonReplay.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sonidoBoton.play();
				dispose();
				show();
			}
		});
		
		botonSalirPausa.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sonidoBoton.play();
				game.setScreen(game.getMenu());
			}
		});
		
		botonResumir.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sonidoBoton.play();
				reanudarActores();
				pausa = false;
				ventanaPausa.remove();
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
        
        update();
        
        if(avanza){
        	gameCamera.position.set(player.getX(), player.getY(), 0);
        }
        gameCamera.position.x = MathUtils.clamp(gameCamera.position.x, gameCamera.viewportWidth/2, Constantes.ANCHO_PANTALLA - gameCamera.viewportWidth/2);
        gameCamera.position.y = MathUtils.clamp(gameCamera.position.y, gameCamera.viewportHeight/2, Constantes.ALTO_MAPA - gameCamera.viewportHeight/2);
        
        hudCamera.update();
        gameCamera.update();
        
        if(estadosLevel1 != ESTADOS_LEVEL_1.INICIO){
        	gameStage.act(delta);
        }
        hudStage.act(delta);
        
        mapRenderer.setView(gameCamera);
        mapRenderer.render();
        
        gameStage.draw();
        hudStage.draw();
        
        if((Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) && estadosLevel1 != ESTADOS_LEVEL_1.GAME_OVER){
        	hudStage.addActor(ventanaPausa);
        	pausarActores();
        	pausa = true;
        	sonidoBoton.play();
        }
        
	}
	
	private void update(){
		
		fps.setText("FPS:"+Gdx.graphics.getFramesPerSecond());
		
		if(!pausa){
		
			if(estadosLevel1 == ESTADOS_LEVEL_1.INICIO){
				
				pausarActores();
				timePausa += Gdx.graphics.getDeltaTime();
				
				if(timePausa < 2){
					textReady.setFontScale(timePausa*5);
				}
				if(timePausa > 2.5){
					estadosLevel1 = ESTADOS_LEVEL_1.NIVEL_1_0;
					timePausa = 0;
					reanudarActores();
				}
				if(textReady.getFontScaleX()>5){
					textReady.remove();
				}
			}
			
			else if(estadosLevel1 == ESTADOS_LEVEL_1.NIVEL_1_0){
				
				if(grupoInvaders.getX()==0){
					grupoInvaders.setPosition(grupoInvaders.getX(), grupoInvaders.getY() - 15);
					velInvaders = -velInvaders;
				}
				else if(grupoInvaders.getX()== 250){
					grupoInvaders.setPosition(grupoInvaders.getX(), grupoInvaders.getY() - 15);
					velInvaders = -velInvaders;
				}
				if(grupoInvaders.getY() < -380){
					grupoInvaders.setY(Constantes.ALTO_PANTALLA);
				}
				grupoInvaders.setPosition(grupoInvaders.getX() + velInvaders, grupoInvaders.getY());
				
				for(Actor a:grupoInvaders.getChildren()){
					
					if(a instanceof Invader){
						
						Invader in = (Invader) a;
						colisionInvader(in);
					}
				}
				
				if(grupoInvaders.getChildren().size == 0){
					
					grupoInvaders.clear();
					grupoInvaders.remove();
					estadosLevel1 = ESTADOS_LEVEL_1.NIVEL_1_1;
				}
				
				compruebaPowUp();
			}
			
			else if(estadosLevel1 == ESTADOS_LEVEL_1.NIVEL_1_1){
				
				if(contadorInvaders>=64){
					maxInvader = true;
				}
				else if(invaders.size == 0){
					Invader in = new Invader(atlas, 1, 2);
					in.setPosition(100, Constantes.ALTO_PANTALLA);
					invaders.add(in);
					gameStage.addActor(in);
				}
				
				for(Invader in:invaders){
					
					if(!in.isMuerto()){
						in.mover(0, -5);
						colisionInvader(in);
					}
					else if(in.isMuerto() && !in.isPowCheck()){
						generaPowUp(in);
					}
					
					if(in.getY() < 0 - in.getHeight()){
						in.setPosition(MathUtils.random(0, 704),Constantes.ALTO_PANTALLA);
					}
					
					if(in.isFinaliza()){
						invaders.removeValue(in, true);
						if(!maxInvader){
							anadeInvader();
							contadorInvaders++;
						}
					}
				}
				
				if(invaders.size == 0){
					estadosLevel1 = ESTADOS_LEVEL_1.NIVEL_1_2;
					contadorInvaders = 0;
					maxInvader = false;
				}
				
				compruebaPowUp();
			}
			
			else if(estadosLevel1 == ESTADOS_LEVEL_1.NIVEL_1_2){
				
				if(invaders.size == 0 && contadorInvaders==0){
					Invader in = new Invader(atlas, 1, 2);
					in.setPosition(400, Constantes.ALTO_PANTALLA);
					invaders.add(in);
					gameStage.addActor(in);
				}
				
				for(Invader in:invaders){
					
					if(!in.isMuerto()){
						in.moverA(new Vector2(player.getX(),player.getY()));
						colisionInvader(in);
					}
					else if(in.isMuerto() && !in.isPowCheck()){
						generaPowUp(in);
					}
					
					if(in.isFinaliza()){
						invaders.removeValue(in, true);
						if(!maxInvader){
							anadeInvader();
							contadorInvaders++;
						}
					}
				}
				
				if(invaders.size == 0 && contadorInvaders>=5){
					estadosLevel1 = ESTADOS_LEVEL_1.NIVEL_2_1;
					avanza = true;
					player.setLevel1(false);
				}
				else if(invaders.size > 0 && contadorInvaders>=30){
					maxInvader = true;
				}
				
				compruebaPowUp();
			}
			
			else if(estadosLevel1 == ESTADOS_LEVEL_1.NIVEL_2_1){
				
				if(avanza){
					if(player.getY() > 1536){
						avanza = false;
						for(int i=0;i<6;i++){
							Asteroide aste = new Asteroide(atlas);
							asteroides.add(aste);
							gameStage.addActor(aste);
						}
						
						for(int i=0;i<5;i++){
							NaveEnemiga1 nav = new NaveEnemiga1(atlas);
							naves1.add(nav);
							if(i%2==0){
								nav.setPosition(0-nav.getWidth(), MathUtils.random(1536, 1980));
								nav.setVelocidad(MathUtils.random(2, 4));
								nav.setDirDcha(true);
							}
							else{
								nav.setPosition(764, MathUtils.random(1536, 1980));
								nav.setVelocidad(-(MathUtils.random(2, 4)));
								nav.setDirDcha(false);
							}
							gameStage.addActor(nav);
						}
						
						player.setLevel2(true);
					}
				}
				
				else{
					
					for(Asteroide as:asteroides){
						
						if(!as.isMuerto()){
							colisionAsteroide(as);
						}
						
						if(as.isFinaliza()){
							as.recolocar();
						}						
					}
					
					for(NaveEnemiga1 nav:naves1){
						
						if(!nav.isMuerto()){
							nav.mover();
						}
					}
					
				}
				
				compruebaPowUp();
			}
			
			else if(estadosLevel1 == ESTADOS_LEVEL_1.GAME_OVER){
				hudStage.addActor(ventanaGameOver);
				pausa = !pausa;
			}
			
			if(player.estaMuerto()){
				estadosLevel1 = ESTADOS_LEVEL_1.GAME_OVER;
				pausarActores();
			}
		}
	}
	
	private void quitarVida(int dano){
		
		vida.setSize(vida.getWidth()-(32*dano), vida.getHeight());
		
		if(vida.getWidth()<=0)
			vida.remove();
	}
	
	private void generaPowUp(Invader in){
		
		int rand = MathUtils.random(0, 1000);
		
		if(rand == 0){
			int randT = MathUtils.random(1,2);
			PowerUp p = new PowerUp(atlas, randT);
			
			if(in.getNivel() == 1)
				p.setPosition(in.getStageCoor().x, in.getStageCoor().y);
			
			else if(in.getNivel() == 2)
				p.setPosition(in.getX(), in.getY());
			
			powerUps.add(p);
			gameStage.addActor(p);
			in.setPowCheck(true);
		}
	}
	
	private void compruebaPowUp(){
		
		if(powerUps.size>0){
			
			for(int i=0;i<powerUps.size;i++){
				PowerUp p = powerUps.get(i);
				if(p.getRect().overlaps(player.getRect())){
					if(p.getTipo()==1)
						sonidoPowShield.play();
					else
						sonidoPowDisp.play();
					player.setPower(p.getTipo());
					powerUps.removeIndex(i);
					p.remove();
				}
			}
		}
	}
	
	private void colisionInvader(Invader in){
		
		for(int i=0;i<player.getDisparos().size;i++){
			DisparoPlayer d = player.getDisparos().get(i);
			if(d.getRect().overlaps(in.getRect()) && in.getVida()>0){
				d.setTipo(4);
				d.setVelocidad(0);
				player.getDisparos().removeIndex(i);
				in.setVida(in.getVida()-d.getDano());				
			}
		}
		
		if(player.isTieneEscudo()){
			if(in.getRect().overlaps(player.getBarrera().getRect()) && in.getVida()>0){
				in.setVida(0);
				player.getBarrera().remove();
				player.setTieneEscudo(false);
				sonidoPowLoose.play();
			}
		}
		else if(in.getRect().overlaps(player.getRect()) && in.getVida()>0 && player.getVida()>0){
			in.setVida(0);
			player.setVida(player.getVida()-2);
			if(player.getVida()>0)
				player.parpadeo();
			if(player.getPowDisparo() > 1)
				player.setPowDisparo(player.getPowDisparo()-1);
			quitarVida(2);
		}
		
		if(in.isMuerto() && !in.isPowCheck()){
			generaPowUp(in);
		}
	}
	
	private void colisionAsteroide(Asteroide as){
		
		for(int i=0;i<player.getDisparos().size;i++){
			DisparoPlayer d = player.getDisparos().get(i);
			if(d.getRect().overlaps(as.getRect()) && as.getVida()>0){
				d.setTipo(4);
				d.setVelocidad(0);
				player.getDisparos().removeIndex(i);
				as.setVida(as.getVida()-d.getDano());				
			}
		}
		
		if(player.isTieneEscudo()){
			if(as.getRect().overlaps(player.getBarrera().getRect()) && as.getVida()>0){
				as.setVida(0);
				player.getBarrera().remove();
				player.setTieneEscudo(false);
				sonidoPowLoose.play();
			}
		}
		else if(as.getRect().overlaps(player.getRect()) && as.getVida()>0 && player.getVida()>0){
			as.setVida(0);
			player.setVida(player.getVida()-2);
			if(player.getVida()>0)
				player.parpadeo();
			if(player.getPowDisparo() > 1)
				player.setPowDisparo(player.getPowDisparo()-1);
			quitarVida(2);
		}
		
	}
	
	private void anadeInvader(){
		
		for(int i=0;i<2;i++){
			Invader inTemp = new Invader(atlas, MathUtils.random(1, 2),2);
			inTemp.setPosition(MathUtils.random(0, 704), Constantes.ALTO_PANTALLA);
			inTemp.setRect(inTemp.getX(), inTemp.getY());
			invaders.add(inTemp);
			gameStage.addActor(inTemp);
			if(estadosLevel1 == ESTADOS_LEVEL_1.NIVEL_1_2){
				inTemp.setVelocidad(inTemp.getVelocidad()+(MathUtils.random(100)));
			}
		}
	}
	
	private void pausarActores(){
		
		player.setPausa(true);
		player.pausarDisparos();
		
		if(powerUps.size>0){
			for(int i=0;i<powerUps.size;i++){
				powerUps.get(i).setPausa(true);
			}
		}
		
		if(asteroides.size > 0){
			for(int i=0;i<asteroides.size;i++){
				asteroides.get(i).setPausa(true);
			}
		}
		
	}
	
	private void reanudarActores(){
		
		player.setPausa(false);
		player.reanudarDisparos();
		
		if(powerUps.size>0){
			for(int i=0;i<powerUps.size;i++){
				powerUps.get(i).setPausa(false);
			}
		}
		
		if(asteroides.size > 0){
			for(int i=0;i<asteroides.size;i++){
				asteroides.get(i).setPausa(false);
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		gameStage.getViewport().update(width, height, true);
		hudStage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		hudStage.addActor(ventanaPausa);
    	pausarActores();
    	pausa = true;
	}

	@Override
	public void resume() {
		reanudarActores();
		pausa = false;
		ventanaPausa.remove();
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		hudStage.clear();
		hudStage.dispose();
		gameStage.clear();
		gameStage.dispose();
	}

}
