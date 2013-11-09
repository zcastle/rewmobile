package com.gob.rewmobile.util;

import android.content.Context;

import com.epson.eposprint.*;
import com.epson.epsonio.*;

public class PrintTicket {

	public static final String TMU220 = "TM-U220";
	public static final String TMT88V = "TM-T88V";
	public static final String BOLETA = "boleta";
	public static final String FACTURA = "factura";
	public static final String PRECUENTA = "precuenta";
	public static final String ENVIO = "envio";

	private String tipoComprobante;
	private String modeloImpresora;
	private Context context;
	private String ip;

	public PrintTicket() {

	}

	public PrintTicket(Context context, String modeloImpresora, String tipoComprobante, String ip) {
		this.modeloImpresora = modeloImpresora;
		this.tipoComprobante = tipoComprobante;
		this.ip = ip;
		printDoc();
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

	public void printDoc() {
		Print printer = new Print();
		int[] status = new int[1];
		status[0] = 0;

		try {
			Builder builder = new Builder(this.modeloImpresora, Builder.MODEL_ANK);
			builder.addTextLang(Builder.LANG_EN);
			builder.addTextSmooth(Builder.TRUE);
			builder.addTextFont(Builder.FONT_A);
			builder.addTextSize(3, 3);

			// builder.addTextStyle(Builder.FALSE, Builder.FALSE, Builder.TRUE,
			// Builder.PARAM_UNSPECIFIED);

			builder.addText("Hello,\t");
			builder.addText("World!\n");
			builder.addCut(Builder.CUT_FEED);

			printer.openPrinter(Print.DEVTYPE_TCP, this.ip);

			if ((status[0] & Print.ST_OFF_LINE) != Print.ST_OFF_LINE) {
				printer.sendData(builder, 10000, status);
			}
			//
			builder.clearCommandBuffer();
			//
			printer.closePrinter();
		} catch (EposException e) {
			int errStatus = e.getErrorStatus();
			status[0] = e.getPrinterStatus();
			try {
				printer.closePrinter();
			} catch (EposException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
