package es.hol.fpriego.entidades;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class EnemigoBase extends Actor{
	
	private int vida;

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

}
