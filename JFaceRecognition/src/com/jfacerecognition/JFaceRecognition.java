package com.jfacerecognition;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.util.Locale; 
import javax.speech.Central; 
import javax.speech.synthesis.Synthesizer; 
import javax.speech.synthesis.SynthesizerModeDesc; 

public class JFaceRecognition{
	
	public static void main(String[] args) {
		//loads the OpenCV library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		

		String imgFile = "images/many_happy.jpg"; //this is the file that is being analyzed
		Mat input = Imgcodecs.imread(imgFile);
		
		//Creates a classifier that detects front facing faces
		String xmlFile = "xmls/lbpcascade_frontalface_improved.xml";
		CascadeClassifier cc = new CascadeClassifier(xmlFile);
		
		//Finds the number of detected faces in the inputed image
		MatOfRect faceDetection = new MatOfRect();
		cc.detectMultiScale(input,faceDetection);
		int detectedFaces = faceDetection.toArray().length;
		
		//Remembers the number of detected faces in the picture
		String speech = String.format("Detected faces: %d",detectedFaces);
		System.out.println(speech);
		
		//Creates red squares around each face in the picture
		Scalar color = new Scalar (0,0,225);
		int thickness = 2;
		
		for(Rect rect: faceDetection.toArray()) {
			Point start = new Point(rect.x, rect.y);
			Point end = new Point(rect.x + rect.width, rect.y + rect.height);
			Imgproc.rectangle(input, start, end, color,thickness);
		}
		
		//Writes an image file with the detected faces
		Imgcodecs.imwrite("images/output.jpg",input);
		
		//Verifies that the system has run 
		speech = "Facial Detection Complete, the number of" + speech;
		System.out.println("Facial Detection Complete");
		
		// Relays the number of detected faces to the user using speech
		try {
	        // Set property as Kevin Dictionary 
	        System.setProperty( 
	            "freetts.voices", 
	            "com.sun.speech.freetts.en.us"
	                + ".cmu_us_kal.KevinVoiceDirectory"); 
	
	        // Register Engine 
	        Central.registerEngineCentral( 
	            "com.sun.speech.freetts"
	            + ".jsapi.FreeTTSEngineCentral"); 
	
	        // Creates and utilizes the Synthesizer 
	        Synthesizer synthesizer = Central.createSynthesizer( 
	                new SynthesizerModeDesc(Locale.US));
            synthesizer.allocate(); 
            synthesizer.resume(); 
            synthesizer.speakPlainText(speech, null); 
            synthesizer.waitEngineState(Synthesizer.QUEUE_EMPTY); 
            synthesizer.deallocate();
	        
		}catch (Exception e) { 
            e.printStackTrace(); 
        } 
	}
}
