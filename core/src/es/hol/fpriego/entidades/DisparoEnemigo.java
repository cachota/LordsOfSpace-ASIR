package es.hol.fpriego.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DisparoEnemigo extends Actor{
	
	private float velocidad , estado;
	private int tipo , dano;
	private TextureRegion disparo1;
	private TextureAtlas atlas;
	private Rectangle rect;
	private TextureRegion[] trImpacto;
	private Animation animImpacto;
	private boolean paused;
	
	public DisparoEnemigo(int tipo,TextureAtlas atlas) {
		
		velocidad = 5;
		this.tipo = tipo;
		this.atlas = atlas;
		estado = 0;
		paused = false;
		trImpacto = new TextureRegion[4];
		
		cargarImagenes();
		
		if(tipo == 1){
			dano = 1;
			setSize(disparo1.getRegionWidth(), disparo1.getRegionHeight());
		}
		
		rect = new Rectangle(getX(),getY(),getWidth(),getHeight());
		
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color col = getColor();
		batch.setColor(col.r, col.g, col.b, col.a*parentAlpha);
		
		if(tipo == 1)
			batch.draw(disparo1, getX(), getY());
		
		else if(tipo == 4){
			estado += Gdx.graphics.getDeltaTime();
			batch.draw(animImpacto.getKeyFrame(estado, false), getX(), getY());
			if(animImpacto.isAnimationFinished(estado)){
				remove();
			}
		}
	}

	@Override
	public void act(float delta) {
		
		if(!paused){
			setY(getY() - velocidad);
			rect.setPosition(getX(),getY());
		}
		super.act(delta);
	}

	private void cargarImagenes(){
		
		disparo1 = atlas.findRegion("laserRed1");

		TextureRegion tx = atlas.findRegion("laserRedSheet");
		int x = 0 , y = 0;
		for(int i=0;i<4;i++){
			trImpacto[i] = new TextureRegion();
			trImpacto[i].setRegion(tx, x, y, 32, 32);
			x += 32;
		}
		animImpacto = new Animation(.05f,trImpacto);
	}

	public int getDano() {
		return dano;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getTipo() {
		return tipo;
	}

	public void setVelocidad(float velocidad) {
		this.velocidad = velocidad;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
}
