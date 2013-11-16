package com.gob.rewmobile.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.epson.eposprint.*;
import com.epson.epsonio.*;
import com.gob.rewmobile.model.Destino;
import com.gob.rewmobile.model.Producto;
import com.gob.rewmobile.objects.Pedido;

public class PrintTicket {

	public static final String TMU220 = "TM-U220";
	public static final String TMT88V = "TM-T88V";
	public static final String BOLETA = "boleta";
	public static final String FACTURA = "factura";
	public static final String PRECUENTA = "precuenta";
	public static final String ENVIO = "envio";
	public static final String ELIMINAR_ENVIO = "eliminar_envio";

	private String tipoComprobante;
	private String modeloImpresora;
	private Context context;
	private String ip;
	private Pedido pedido;
	private Destino destino;

	public PrintTicket() {

	}

	public PrintTicket(Context context, String modeloImpresora, String tipoComprobante, Destino destino, Pedido pedido) {
		this.modeloImpresora = modeloImpresora;
		this.tipoComprobante = tipoComprobante;
		this.destino = destino;
		this.pedido = pedido;
	}
	
	public PrintTicket(Context context, String modeloImpresora, String tipoComprobante, Pedido pedido) {
		this.modeloImpresora = modeloImpresora;
		this.tipoComprobante = tipoComprobante;
		this.destino = null;
		this.pedido = pedido;
	}

	private void buscar() {
		int errStatus = IoStatus.SUCCESS;
		String[] mList = null;

		try {
			Finder.start(this.context, DevType.TCP, ip);
			mList = Finder.getResult();
		} catch (EpsonIoException e) {
			errStatus = e.getStatus();
		}
	}

	private void conectar() {
		int errStatus = IoStatus.SUCCESS;
		String[] mList = null;

		try {
			mList = Finder.getResult();
		} catch (EpsonIoException e) {
			errStatus = e.getStatus();
		}
	}

	private void detener() {
		int errStatus = IoStatus.SUCCESS;

		try {
			Finder.stop();
		} catch (EpsonIoException e) {
			errStatus = e.getStatus();
		}
	}

	public int printDoc() {
		int[] printerStatus = new int[1];
		if(tipoComprobante.equals(PrintTicket.ENVIO)) {
			printerStatus[0] = EnviarPedido();
		} else if (tipoComprobante.equals(PrintTicket.ELIMINAR_ENVIO)) {
			printerStatus[0] = EliminarProductoEnviado();
		} else if (tipoComprobante.equals(PrintTicket.PRECUENTA)) {
			printerStatus[0] = EnviarPrecuenta();
		}
		return printerStatus[0];
	}
	
	private int EnviarPedido(){
		Print printer = new Print();
		int[] printerStatus = new int[1];
		printerStatus[0] = 0;
		int timeout = 10000;

		try {
			Builder builder = new Builder(this.modeloImpresora, Builder.MODEL_ANK);
			builder.addTextLang(Builder.LANG_EN);
			builder.addTextSmooth(Builder.TRUE);
			builder.addTextFont(Builder.FONT_B);
			builder.addTextSize(3, 3);

			// builder.addTextStyle(Builder.FALSE, Builder.FALSE, Builder.TRUE,
			// Builder.PARAM_UNSPECIFIED);

			//builder.addText("Hello,\t");
			//builder.addText("World!\n");
			
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String fechaHora = formateador.format(new Date());
			builder.addText("========================================\n");
			builder.addText("         N U E V O  P E D I D O\n");
			builder.addText("========================================\n");
			builder.addText("DESTINO: ".concat(this.destino.getNombre()).concat("\n"));
			builder.addText("MOZO: ".concat(this.pedido.getMozo().toString()).concat("\n"));
			builder.addText("MESA: ".concat("\t"));
			
			builder.addTextStyle(Builder.FALSE, Builder.FALSE, Builder.FALSE, Builder.COLOR_2);
			builder.addText(this.pedido.getMesa().concat("\n"));
			builder.addTextStyle(Builder.FALSE, Builder.FALSE, Builder.FALSE, Builder.COLOR_1);
			
			builder.addText("HORA: ".concat(fechaHora).concat("\n"));
			builder.addText("----------------------------------------\n");
			for (Producto obj: pedido.getProducto()) {
				String cant = String.valueOf(obj.getCantidad().intValue()).concat("  ").substring(0, 3).concat(" ");
				builder.addText(cant.concat(obj.getNombre()).concat("\n"));
				if (!obj.getMensaje().isEmpty()) {
					builder.addText(" -".concat(obj.getMensaje()).concat("\n"));
				}
			}
			builder.addText("========================================\n");
			builder.addCut(Builder.CUT_FEED);

			printer.openPrinter(Print.DEVTYPE_TCP, this.destino.getIp());

			if ((printerStatus[0] & Print.ST_OFF_LINE) != Print.ST_OFF_LINE) {
				printer.sendData(builder, timeout, printerStatus);
			}
			//
			builder.clearCommandBuffer();
			//
			printer.closePrinter();
		} catch (EposException e) {
			int errStatus = e.getErrorStatus();
			if (errStatus == EposException.ERR_OPEN) {
				//openPrinter
			} else if (errStatus == EposException.ERR_TIMEOUT) {
				printerStatus[0] = e.getPrinterStatus(); //sendData
			}
			try {
				printer.closePrinter();
			} catch (EposException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return printerStatus[0];
	}
	
	private int EliminarProductoEnviado(){
		Print printer = new Print();
		int[] printerStatus = new int[1];
		printerStatus[0] = 0;
		int timeout = 10000;

		try {
			Builder builder = new Builder(this.modeloImpresora, Builder.MODEL_ANK);
			builder.addTextLang(Builder.LANG_EN);
			builder.addTextSmooth(Builder.TRUE);
			builder.addTextFont(Builder.FONT_B);
			builder.addTextSize(3, 3);

			// builder.addTextStyle(Builder.FALSE, Builder.FALSE, Builder.TRUE,
			// Builder.PARAM_UNSPECIFIED);

			//builder.addText("Hello,\t");
			//builder.addText("World!\n");
			
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String fechaHora = formateador.format(new Date());
			builder.addText("========================================\n");
			builder.addText("       P E D I D O  A N U L A D O\n");
			builder.addText("========================================\n");
			builder.addText("DESTINO: ".concat(this.destino.getNombre()).concat("\n"));
			builder.addText("MOZO: ".concat(this.pedido.getMozo().toString()).concat("\n"));
			builder.addText("MESA: ".concat("\t"));
			
			builder.addTextStyle(Builder.FALSE, Builder.FALSE, Builder.FALSE, Builder.COLOR_2);
			builder.addText(this.pedido.getMesa().concat("\n"));
			builder.addTextStyle(Builder.FALSE, Builder.FALSE, Builder.FALSE, Builder.COLOR_1);
			
			builder.addText("HORA: ".concat(fechaHora).concat("\n"));
			builder.addText("----------------------------------------\n");
			for (Producto obj: pedido.getProducto()) {
				String cant = String.valueOf(obj.getCantidad().intValue()).concat("  ").substring(0, 3).concat(" ");
				builder.addText(cant.concat(obj.getNombre()).concat("\n"));
				if (!obj.getMensaje().isEmpty()) {
					builder.addText(" -".concat(obj.getMensaje()).concat("\n"));
				}
			}
			builder.addText("========================================\n");
			builder.addCut(Builder.CUT_FEED);

			printer.openPrinter(Print.DEVTYPE_TCP, this.destino.getIp());

			if ((printerStatus[0] & Print.ST_OFF_LINE) != Print.ST_OFF_LINE) {
				printer.sendData(builder, timeout, printerStatus);
			}
			//
			builder.clearCommandBuffer();
			//
			printer.closePrinter();
		} catch (EposException e) {
			int errStatus = e.getErrorStatus();
			if (errStatus == EposException.ERR_OPEN) {
				//openPrinter
			} else if (errStatus == EposException.ERR_TIMEOUT) {
				printerStatus[0] = e.getPrinterStatus(); //sendData
			}
			try {
				printer.closePrinter();
			} catch (EposException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return printerStatus[0];
	}
	
	private int EnviarPrecuenta(){
		Print printer = new Print();
		int[] printerStatus = new int[1];
		printerStatus[0] = 0;
		int timeout = 10000;

		try {
			Builder builder = new Builder(this.modeloImpresora, Builder.MODEL_ANK);
			builder.addTextLang(Builder.LANG_EN);
			builder.addTextSmooth(Builder.TRUE);
			builder.addTextFont(Builder.FONT_B);
			//builder.addTextSize(3, 2);
			
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			DecimalFormat df = new DecimalFormat( "##0.00" );
			
			String fechaHora = formateador.format(new Date());
			builder.addText("========================================\n");
			builder.addText("           P R E C U E N T A\n");
			builder.addText("       COMPROBANTE NO AUTORIZADO\n");
			builder.addText("========================================\n");
			
			String mesa = "MESA: ".concat(this.pedido.getMesa()).concat("                                ").substring(0, 32);
			String mesa_pax = mesa.concat("PAX: ".concat(String.valueOf(this.pedido.getPax())));
			builder.addText(mesa_pax.concat("\n"));
			builder.addText("FECHA: ".concat(fechaHora).concat("\n"));
			builder.addText("MOZO: ".concat(this.pedido.getMozo().toString()).concat("\n"));
			builder.addText("========================================\n");
			builder.addText("Cant. Producto            Unit.    Total\n");
			builder.addText("----------------------------------------\n");
			for (Producto obj: pedido.getProducto()) {
				String cantidad = String.valueOf(obj.getCantidad().intValue()).concat("   ").substring(0, 3).concat(" ");
				String producto = obj.getNombre().concat("                 ").substring(0, 17).concat(" ");
				String unitario = right("        ".concat(df.format(obj.getPrecio())), 8).concat(" ");
				String total = right("         ".concat(df.format(obj.getTotal())), 9);
				String linea = cantidad.concat(producto).concat(unitario).concat(total);
				
				builder.addText(linea.concat("\n"));
			}
			builder.addText("----------------------------------------\n");
			String total = right("          ".concat(df.format(pedido.getTotal())), 10);
			builder.addText("            CONSUMO TOTAL S/.:".concat(total).concat("\n"));
			builder.addText("                         ===============\n");
			builder.addText("\n");
			builder.addText("R U C :---------------------------------\n");
			builder.addText("\n");
			builder.addText("RAZON SOCIAL:---------------------------\n");
			builder.addText("\n");
			builder.addText("----------------------------------------\n");
			
			builder.addCut(Builder.CUT_FEED);

			printer.openPrinter(Print.DEVTYPE_TCP, "192.168.1.20");

			if ((printerStatus[0] & Print.ST_OFF_LINE) != Print.ST_OFF_LINE) {
				printer.sendData(builder, timeout, printerStatus);
			}
			//
			builder.clearCommandBuffer();
			//
			printer.closePrinter();
		} catch (EposException e) {
			int errStatus = e.getErrorStatus();
			if (errStatus == EposException.ERR_OPEN) {
				//openPrinter
			} else if (errStatus == EposException.ERR_TIMEOUT) {
				printerStatus[0] = e.getPrinterStatus(); //sendData
			}
			try {
				printer.closePrinter();
			} catch (EposException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return printerStatus[0];
	}
	
	private String right(String str, int len) {
	      if (str == null) {
	          return null;
	      }
	      if (len < 0) {
	          return "";
	      }
	      if (str.length() <= len) {
	          return str;
	      }
	      return str.substring(str.length() - len);
	  }

}
