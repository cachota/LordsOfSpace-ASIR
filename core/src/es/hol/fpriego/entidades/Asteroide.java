package es.hol.fpriego.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Asteroide extends EnemigoBase{

	private int vida, velocidad, tipo, oldTipo;
	private float estado;
	private boolean finaliza, muerto, pausa,dirDcha;
	private TextureRegion[] trExplota;
	private Animation animExplota;
	private TextureAtlas atlas;
	private TextureRegion spriteGris , spriteMarron;
	private Rectangle rect;
	
	public Asteroide(TextureAtlas atlas) {
		
		this.atlas = atlas;
		vida = 3;
		velocidad = MathUtils.random(2,4);
		tipo = MathUtils.random(0, 1);
		oldTipo = tipo;
		estado = 0;
		setFinaliza(false);
		setMuerto(false);
		trExplota = new TextureRegion[8];
		pausa = false;
		
		cargarImagenes();
		
		boolean pos = MathUtils.randomBoolean();
		int x = (int) ((pos)?0-getWidth():764);
		setPosition(x, MathUtils.random(1536,2048));
		
		if(x<0){
			dirDcha = true;
		}
		else{
			dirDcha = false;
		}
		
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color col = getColor();
		batch.setColor(col.r, col.g, col.b, col.a*parentAlpha);
		
		if(tipo==0){
			batch.draw(spriteGris, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation());
		}
		else if(tipo == 1){
			batch.draw(spriteMarron, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation());
		}
		else if(tipo == 3){
			
			estado += Gdx.graphics.getDeltaTime();
			batch.draw(animExplota.getKeyFrame(estado, false),getX(),getY());
			
			if(animExplota.isAnimationFinished(estado)){
				setFinaliza(true);
			}
		}
	}

	@Override
	public void act(float delta) {
		
		if(!pausa){
			if(dirDcha){
				setPosition(getX()+velocidad, getY()-velocidad);
			}
			else if(!dirDcha){
				setPosition(getX()-velocidad, getY()-velocidad);
			}
			rect.setPosition(getX(), getY());
			addAction(Actions.rotateBy(2));
		}
		
		if(vida < 1){
			setMuerto(true);
			tipo = 3;
			velocidad = 0;
		}
		
		if(getY()<1024-getHeight()){
			boolean pos = MathUtils.randomBoolean();
			int x = (int) ((pos)?(0-getWidth()):764);
			setPosition(x, MathUtils.random(1536,2048));
			rect.setPosition(getX(), getY());
			if(x<0){
				dirDcha = true;
			}
			else{
				dirDcha = false;
			}
		}
		super.act(delta);
	}

	@Override
	public int getVida() {
		return vida;
	}

	@Override
	public void setVida(int vida) {
		this.vida = vida;
	}

	private void cargarImagenes(){
		
		spriteGris = atlas.findRegion("meteorGrey-big1");
		spriteMarron = atlas.findRegion("meteorBrown-big1");
		
		TextureRegion tx = atlas.findRegion("romper");
		int x = 0, y = 0;
		int index = 0;
		for(int i=0;i<2;i++){
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
		
		if(tipo==0){
			rect = new Rectangle(getX(),getY(),spriteGris.getRegionWidth(),spriteGris.getRegionHeight());
			setSize(spriteGris.getRegionWidth(), spriteGris.getRegionHeight());
			setOrigin(spriteGris.getRegionWidth()/2, spriteGris.getRegionHeight()/2);
		}
		else{
			rect = new Rectangle(getX(),getY(),spriteMarron.getRegionWidth(),spriteMarron.getRegionHeight());
			setSize(spriteMarron.getRegionWidth(), spriteMarron.getRegionHeight());
			setOrigin(spriteMarron.getRegionWidth()/2, spriteMarron.getRegionHeight()/2);
		}
	}

	public void recolocar(){
		
		boolean pos = MathUtils.randomBoolean();
		int x = (int) ((pos)?(0-getWidth()):764);
		setPosition(x, MathUtils.random(1536,2048));
		rect.setPosition(getX(), getY());
		if(x<0){
			dirDcha = true;
		}
		else{
			dirDcha = false;
		}
		
		velocidad = MathUtils.random(2,4);
		tipo = oldTipo;
		finaliza = false;
		muerto = false;
		vida = 3;
		estado = 0;
	}
	
	public boolean isFinaliza() {
		return finaliza;
	}

	public void setFinaliza(boolean finaliza) {
		this.finaliza = finaliza;
	}

	public boolean isMuerto() {
		return muerto;
	}

	public void setMuerto(boolean muerto) {
		this.muerto = muerto;
	}

	public boolean isPausa() {
		return pausa;
	}

	public void setPausa(boolean pausa) {
		this.pausa = pausa;
	}

	public Rectangle getRect() {
		return rect;
	}
	
}
