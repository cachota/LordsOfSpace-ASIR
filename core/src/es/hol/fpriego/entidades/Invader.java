package es.hol.fpriego.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Invader extends EnemigoBase{

	private TextureAtlas atlas;
	private TextureRegion spriteVerde , spriteAzul;
	private int tipo , vida, nivel, velocidad;
	private Rectangle rect;
	private Vector2 stageCoor, posicion, direccion, velVec, movimiento;
	private boolean setGlobal, muerto , powCheck, finaliza;
	private TextureRegion[] trExplota;
	private Animation animExplota;
	private float estado;
	
	public Invader(TextureAtlas atlas,int tipo, int nivel) {
		
		this.atlas = atlas;
		this.tipo = tipo;
		this.nivel = nivel;
		vida = 2;
		setGlobal = false;
		muerto = false;
		powCheck = false;
		finaliza = false;
		estado = 0;
		velocidad = 200;
		trExplota = new TextureRegion[16];
		
		cargarImagenes();
		
		setSize(spriteVerde.getRegionWidth(), spriteVerde.getRegionHeight());
		posicion = new Vector2();
		direccion = new Vector2();
		velVec = new Vector2();
		movimiento = new Vector2();
		rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color col = getColor();
		batch.setColor(col.r, col.g, col.b, col.a*parentAlpha);
		
		if(tipo==1){
			batch.draw(spriteVerde, getX(), getY());
		}
		else if(tipo == 2){
			batch.draw(spriteAzul, getX(), getY());
		}
		else if(tipo == 3){
			
			estado += Gdx.graphics.getDeltaTime();
			batch.draw(animExplota.getKeyFrame(estado, false),getX(),getY());
			
			if(animExplota.isAnimationFinished(estado)){
				finaliza = true;
				remove();
			}
		}
		
	}

	@Override
	public void act(float delta) {
		
		if(nivel == 1){
			posicion.set(getX(), getY());
			
			if(!setGlobal){
				stageCoor = new Vector2(getParent().localToStageCoordinates(posicion));
				setGlobal = true;
			}
			stageCoor.set(getParent().localToStageCoordinates(posicion));
			rect.setPosition(stageCoor);
		}
		else if(nivel == 2){
			rect.setPosition(getX(),getY());
		}
		
		if(vida < 1){
			muerto = true;
			tipo = 3;
		}
		
		super.act(delta);
	}
	
	public void mover(float x,float y){
		setX(getX() + x);
		setY(getY() + y);
	}
	
	public void moverA(Vector2 destino){
		
		posicion.set(getX(), getY());
		direccion.set(destino).sub(posicion).nor();
		velVec.set(direccion).scl(velocidad);
		movimiento.set(velVec).scl(Gdx.graphics.getDeltaTime());
		if(posicion.dst2(destino) > movimiento.len2())
			posicion.add(movimiento);
		else
			posicion.add(destino);
		
		setPosition(posicion.x, posicion.y);
	}
	
	private void cargarImagenes(){
		
		spriteVerde = atlas.findRegion("enemyGreen4");
		spriteAzul = atlas.findRegion("enemyBlue4");
		
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
	}

	@Override
	public int getVida() {
		return vida;
	}

	@Override
	public void setVida(int vida) {
		this.vida = vida;
	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(float x,float y) {
		rect.setPosition(x, y);
	}

	public Vector2 getStageCoor() {
		return stageCoor;
	}

	public boolean isMuerto() {
		return muerto;
	}

	public void setMuerto(boolean muerto) {
		this.muerto = muerto;
	}

	public boolean isPowCheck() {
		return powCheck;
	}

	public void setPowCheck(boolean powCheck) {
		this.powCheck = powCheck;
	}

	public int getNivel() {
		return nivel;
	}

	public boolean isFinaliza() {
		return finaliza;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}
	
}
