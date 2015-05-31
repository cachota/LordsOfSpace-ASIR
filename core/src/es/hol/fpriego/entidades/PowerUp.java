package es.hol.fpriego.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class PowerUp extends Actor{

	private TextureAtlas atlas;
	private Sprite power1, power2;
	private int tipo;
	private Rectangle rect;
	private boolean pausa;
	
	public PowerUp(TextureAtlas atlas,int tipo) {
		
		this.atlas = atlas;
		this.tipo = tipo;
		pausa = false;
		cargarImagenes();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color col = getColor();
		batch.setColor(col.r, col.g, col.b, col.a*parentAlpha);
		
		if(tipo==1)
			batch.draw(power1, getX(), getY());
		else if(tipo==2)
			batch.draw(power2, getX(), getY());
	}

	@Override
	public void act(float delta) {
		
		if(!pausa){
			if(getY()<0 - getWidth())
				remove();
			
			setY(getY()-1);
			rect.setPosition(getX(), getY());
			
			if(getActions().size==0){
				addAction(Actions.sequence(Actions.alpha(.5f, .2f),Actions.alpha(1f, .2f)));
			}
		}
		
		super.act(delta);
	}

	private void cargarImagenes(){
		
		power1 = new Sprite(atlas.findRegion("shieldPowerup"));
		power2 = new Sprite(atlas.findRegion("shootPowerup"));
		
		setSize(24, 24);
		rect = new Rectangle(getX(), getY(), power1.getWidth(), power1.getHeight());
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setPausa(boolean pausa) {
		this.pausa = pausa;
	}

	public int getTipo() {
		return tipo;
	}
	
}
