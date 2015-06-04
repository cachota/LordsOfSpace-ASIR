package es.hol.fpriego.entidades;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.Array;

import es.hol.fpriego.Constantes;

public class Player extends Actor{
	
	private Sprite spritePlayer1 , spritePlayer2 , spritePlayer3;
	private Touchpad pad;
	private float velocidad , velDiagonal;
	private int powDisparo , vida;
	private boolean pausa , muerto;
	private Array<DisparoPlayer> disparos;
	private TextureAtlas atlas;
	private Rectangle rect;
	private TextureRegion[] trExplota;
	private Animation animExplota;
	private float estado;
	private Sound sonidoDisparo, sonidoDisparo2;
	private AssetManager manager;
	private Color color;
	private boolean level1,level2;
	private boolean tieneEscudo;
	private Shield barrera;
	
	public Player(Touchpad pad,TextureAtlas atlas,AssetManager manager) {
		
		this.pad = pad;
		this.atlas = atlas;
		this.manager = manager;
		velocidad = 5f;
		velDiagonal = (float) ((float) velocidad * Math.sqrt(.5f));
		powDisparo = 1;
		vida = 6;
		pausa = false;
		muerto = false;
		level1 = true;
		level2 = false;
		tieneEscudo = false;
		estado = 0;
		trExplota = new TextureRegion[16];
		disparos = new Array<DisparoPlayer>();
		
		cargarImagenes();
		
		color = getColor();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color col = getColor();
		batch.setColor(col.r, col.g, col.b, col.a*parentAlpha);
		
		if(vida<=0){
			pausa = true;
			estado += Gdx.graphics.getDeltaTime();
			batch.draw(animExplota.getKeyFrame(estado, false),getX(),getY());
			if(animExplota.isAnimationFinished(estado)){
				muerto = true;
				remove();
			}
		}
		else if(powDisparo == 1){
			batch.draw(spritePlayer1, getX(), getY());
		}
		else if(powDisparo == 2){
			batch.draw(spritePlayer2, getX(), getY());
		}
		else if(powDisparo == 3){
			batch.draw(spritePlayer3, getX(), getY());
		}
	}

	@Override
	public void act(float delta) {
		
		if(!pausa){
		
			if(Gdx.app.getType()==ApplicationType.Android){
				setX(getX() - pad.getKnobPercentX()*getVelocidad());
				setY(getY() - pad.getKnobPercentY()*getVelocidad());
			}
			
			if(Gdx.app.getType()==ApplicationType.Desktop || Gdx.app.getType()==ApplicationType.Applet){
				
				if(Gdx.input.isKeyPressed(Keys.LEFT) && Gdx.input.isKeyPressed(Keys.UP)){
					setPosition(getX() - velDiagonal, getY() + velDiagonal);
				}
				else if(Gdx.input.isKeyPressed(Keys.LEFT) && Gdx.input.isKeyPressed(Keys.DOWN)){
					setPosition(getX() - velDiagonal, getY() - velDiagonal);
				}
				else if(Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.UP)){
					setPosition(getX() + velDiagonal, getY() + velDiagonal);
				}
				else if(Gdx.input.isKeyPressed(Keys.RIGHT) && Gdx.input.isKeyPressed(Keys.DOWN)){
					setPosition(getX() + velDiagonal, getY() - velDiagonal);
				}
				else if(Gdx.input.isKeyPressed(Keys.LEFT)){
					setX(getX() - getVelocidad());
				}
				else if(Gdx.input.isKeyPressed(Keys.RIGHT)){
					setX(getX() + getVelocidad());
				}
				else if(Gdx.input.isKeyPressed(Keys.UP)){
					setY(getY() + getVelocidad());
				}
				else if(Gdx.input.isKeyPressed(Keys.DOWN)){
					setY(getY() - getVelocidad());
				}
				
				if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
					disparar();
				}
				
			}
			
			if(getX()<0){
				setX(0);
			}
			
			if(getX()>Constantes.ANCHO_PANTALLA - getWidth()){
				setX(Constantes.ANCHO_PANTALLA - getWidth());
			}
			
			if(getY()<0){
				setY(0);
			}
			
			if(level1){
				
				if(getY()>Constantes.ALTO_PANTALLA-getHeight()){
					setY(Constantes.ALTO_PANTALLA-getHeight());
				}
			}
			
			else if(level2){
				if(getY()<Constantes.ALTO_PANTALLA){
					setY(Constantes.ALTO_PANTALLA);
				}
				else if(getY()>Constantes.ALTO_MAPA-getHeight()){
					setY(Constantes.ALTO_MAPA-getHeight());
				}
			}
		}
		
		rect.setPosition(getX(), getY());
			
		if(disparos.size > 0){
			for(int i=0;i<disparos.size;i++){
				DisparoPlayer d = disparos.get(i);
				if(d.getY() > d.getInicialY() + 1024){
					d.remove();
					disparos.removeIndex(i);
				}
			}
		}
		
		super.act(delta);
	}

	public void disparar(){
		
		DisparoPlayer d1 = new DisparoPlayer(powDisparo,atlas,getY());
		DisparoPlayer d2 = new DisparoPlayer(powDisparo,atlas,getY());
		d1.setPosition(getX() + 14, getY() + getHeight());
		d2.setPosition(getX() + 44, getY() + getHeight());
		getStage().addActor(d1);
		getStage().addActor(d2);
		disparos.add(d1);
		disparos.add(d2);
		
		if(d1.getTipo()==1 || d1.getTipo()==2)
			sonidoDisparo.play();
		else if(d1.getTipo()==3)
			sonidoDisparo2.play();
	}
	
	public void pausarDisparos(){
		
		for(DisparoPlayer d:disparos){
			d.setPaused(true);
		}
		
	}
	
	public void reanudarDisparos(){
		
		for(DisparoPlayer d:disparos){
			d.setPaused(false);
		}
		
	}
	
	public boolean estaMuerto(){
		return muerto;
	}
	
	public void parpadeo(){
		if(getActions().size==0 && !pausa)
			addAction(Actions.sequence(Actions.color(Color.RED, .2f),Actions.color(color, .2f)));
	}
	
	public void setPower(int tipo){
		
		if(tipo==2){
			if(powDisparo == 1)
				powDisparo = 2;
			else if(powDisparo == 2)
				powDisparo = 3;
			if(getActions().size == 0)
				addAction(Actions.sequence(Actions.color(Color.GREEN, .2f),Actions.color(color, .2f)));
		}
		else{
			if(!tieneEscudo){
				barrera = new Shield(this);
				barrera.setPosition(getX()-13, getY());
				getStage().addActor(barrera);
				tieneEscudo = true;
			}
			if(getActions().size == 0)
				addAction(Actions.sequence(Actions.color(Color.GREEN, .2f),Actions.color(color, .2f)));
		}
	}
	
	private void cargarImagenes(){
		
		spritePlayer1 = new Sprite(atlas.findRegion("playerShip1"));
		spritePlayer2 = new Sprite(atlas.findRegion("playerShip2"));
		spritePlayer3 = new Sprite(atlas.findRegion("playerShip3"));
		
		TextureRegion tx = atlas.findRegion("explosion");
		int x = 0, y = 0;
		int index = 0;
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				trExplota[index] = new TextureRegion();
				trExplota[index].setRegion(tx, x, y, 64, 64);
				x += 64;
				index++;
			}
			x = 0;
			y += 64;
		}
		animExplota = new Animation(.05f, trExplota);
		
		sonidoDisparo = manager.get("sound/sfx-laser1.ogg", Sound.class);
		sonidoDisparo2 = manager.get("sound/sfx-laser2.ogg", Sound.class);
		
		setSize(spritePlayer1.getWidth(), spritePlayer1.getHeight());
		rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	public Shield getBarrera() {
		return barrera;
	}

	public float getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(float velocidad) {
		this.velocidad = velocidad;
	}

	public Array<DisparoPlayer> getDisparos() {
		return disparos;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public void setPausa(boolean pausa) {
		this.pausa = pausa;
	}
	
	public boolean isTieneEscudo() {
		return tieneEscudo;
	}

	public void setTieneEscudo(boolean tieneEscudo) {
		this.tieneEscudo = tieneEscudo;
	}
	
	public void setLevel1(boolean level1) {
		this.level1 = level1;
	}

	public void setLevel2(boolean level2) {
		this.level2 = level2;
	}

	public int getPowDisparo() {
		return powDisparo;
	}

	public void setPowDisparo(int powDisparo) {
		this.powDisparo = powDisparo;
	}

	public class Shield extends Actor{
		
		private Sprite barrera;
		private Rectangle rect;
		private Player player;
		
		public Shield(Player player){
			
			this.player = player;
			barrera = new Sprite(atlas.findRegion("shield"));
			rect = new Rectangle(getX(),getY(),barrera.getWidth(),barrera.getHeight());
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
		
			Color col = getColor();
			batch.setColor(col.r, col.g, col.b, col.a*parentAlpha);
			
			batch.draw(barrera, player.getX()-13, player.getY());
		}

		@Override
		public void act(float delta) {
			
			rect.setPosition(player.getX()-13, player.getY());
			super.act(delta);
		}

		public Rectangle getRect() {
			return rect;
		}
		
	}
}
