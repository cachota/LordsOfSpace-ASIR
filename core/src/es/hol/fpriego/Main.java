package es.hol.fpriego;

import com.badlogic.gdx.Game;

import es.hol.fpriego.pantallas.PantallaLevel1;
import es.hol.fpriego.pantallas.PantallaMenu;
import es.hol.fpriego.pantallas.PantallaSplash;

public class Main extends Game {
	
	private Recursos recursos;
	private PantallaSplash pSplash;
	private PantallaLevel1 pLevel1;
	private PantallaMenu pMenu;
	
	@Override
	public void create () {
		recursos = new Recursos();
		pSplash = new PantallaSplash(this);
		pMenu = new PantallaMenu(this);
		pLevel1 = new PantallaLevel1(this);
		setScreen(pSplash);
	}

	@Override
	public void dispose() {
		recursos.dispose();
		getScreen().dispose();
	}
	
	public void crearLevel1(){
		pLevel1 = new PantallaLevel1(this);
	}

	public Recursos getRecursos() {
		return recursos;
	}

	public PantallaSplash getSplash() {
		return pSplash;
	}

	public PantallaLevel1 getLevel1() {
		return pLevel1;
	}

	public PantallaMenu getMenu() {
		return pMenu;
	}
	
}
