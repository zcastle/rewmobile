package com.ob.rewmobile.util;

public final class App {

	public static final String DEVICE_NAME = "TABLET01";

	public static boolean isPedido() {
		return Globals.MODULO.equals(Globals.MODULO_PEDIDO) ? true : false;
	}
	
	public static boolean isCaja() {
		return Globals.MODULO.equals(Globals.MODULO_CAJA) ? true : false;
	}

}
