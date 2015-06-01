package es.hol.fpriego.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class NaveEnemiga1 extends EnemigoBase{

	private TextureAtlas atlas;
	private TextureRegion spriteVerde;
	private int tipo , vida, nivel, velocidad;
	private Rectangle rect;
	private boolean muerto , powCheck, finaliza, dirDcha;
	private TextureRegion[] trExplota;
	private Animation animExplota;
	private float estado;
	
	public NaveEnemiga1(TextureAtlas atlas) {
		
		this.atlas = atlas;
		tipo = 1;
		vida = 3;
		muerto = false;
		powCheck = false;
		finaliza = false;
		estado = 0;
		velocidad = 2;
		trExplota = new TextureRegion[16];
		
		cargarImagenes();
		
		setSize(spriteVerde.getRegionWidth(), spriteVerde.getRegionHeight());
		rect = new Rectangle(getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		Color col = getColor();
		batch.setColor(col.r, col.g, col.b, col.a*parentAlpha);
		
		if(tipo==1){
			batch.draw(spriteVerde, getX(), getY());
		}
		else if(tipo == 3){
			
			estado += Gdx.graphics.getDeltaTime();
			batch.draw(animExplota.getKeyFrame(estado, false),getX(),getY());
			
			if(animExplota.isAnimationFinished(estado)){
				finaliza = true;
			}
		}
		
	}

	@Override
	public void act(float delta) {
		
		if(getX() > 900){
			recolocarIz();
		}
		
		if(getX() < -100){
			recolocarDch();
		}
		
		rect.setPosition(getX(),getY());
		
		if(vida < 1){
			muerto = true;
			tipo = 3;
		}
		
		super.act(delta);
	}
	
	public void mover(){
		setX(getX() + velocidad);
	}
	
	private void recolocarIz(){
		setPosition(0-getWidth(), MathUtils.random(1536, 1980));
	}
	
	private void recolocarDch(){
		setPosition(764, MathUtils.random(1536, 1980));
	}
	
	private void cargarImagenes(){
		
		spriteVerde = atlas.findRegion("enemyGreen1");
		
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

	public boolean isDirDcha() {
		return dirDcha;
	}

	public void setDirDcha(boolean dirDcha) {
		this.dirDcha = dirDcha;
	}
}
