package com.ob.rewmobile.util;

import com.ob.rewmobile.model.PedidoController;

public class Globals {
	
	public static final int ROL_ADMIN = 1;
	public static final int ROL_CAJA = 2;
	public static final int ROL_MOZO = 6;
	
	public static final boolean DIRECTO = true;
	public static final String USUARIO_TABLET = "TABLET";
	
	public static final String MODULO_CAJA = "C";
	public static final String MODULO_PEDIDO = "P";
	public static String MODULO = "";
	
	public static double VA_IGV;
	public static Double VA_SERV;
	public static Double VA_TC;
	
	public static PedidoController PEDIDO_PAGAR;
	
	public static final String MONEDA_SIMBOLO_SOLES = "S/. ";
	public static final String MONEDA_SIMBOLO_DOLARES = "$. ";
	public static final String MONEDA_SIMBOLO = MONEDA_SIMBOLO_SOLES;
	public static final String MONEDA_S = "S";
	public static final String MONEDA_D = "D";
	public static final String MONEDA = "S";
	
	public static String IMPRESORA_P = "";
	public static String IMPRESORA_B = "";
	public static String IMPRESORA_F = "";
	
	public static String VENTA_RAPIDA = "VR";
	public static String VENTA_DETALLADA = "VD";
	
}
