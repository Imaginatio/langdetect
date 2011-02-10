package com.cybozu.labs.langdetect;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class RealWorldDetectionTest {

	Detector detector;

	@BeforeClass
	public static void setup() throws LangDetectException {
		DetectorFactory.loadProfile();
	}
	
	String text_en = "I bring in the jar A dependency in jar B, my maven " +
			"dependency hierarchy shows that jar B is now usin … ove that " +
			"dependency, commons-dbcp is gone from my maven dependency hierarchy ";
	
	@Test
	public void testDetectEnglish() throws LangDetectException {
		detector = DetectorFactory.create(0.5);
		detector.append(text_en);
		assertEquals("en", detector.detect());
	}
	
	String text_fr = "Le premier ministre, des hauts responsables égyptiens et" +
			" la CIA évoquent une démission \"très probable\" du raïs, au pouvoir " +
			"depuis trente ans. Suivez l'évolution des événements en direct.";
	
	@Test
	public void testDetectFrench() throws LangDetectException {
		detector = DetectorFactory.create(0.5);
		detector.append(text_fr);
		assertEquals("fr", detector.detect());
	}
	
	String text_es = "Fuentes del Gobierno indican que el presidente Mubarak dejará " +
			"el poder en las próximas horas.- El jefe del Ejército asegura en televisión " +
			"que garantiza la seguridad del país y que se cumplirán las peticiones del " +
			"pueblo.- Escenas de emoción en la plaza de la Liberación";
	
	@Test
	public void testDetectSpanish() throws LangDetectException {
		detector = DetectorFactory.create(0.5);
		detector.append(text_es);
		assertEquals("es", detector.detect());
	}
	
}
