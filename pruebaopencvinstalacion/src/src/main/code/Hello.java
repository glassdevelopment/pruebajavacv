package src.main.code;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

public class Hello {

	public void run() {
	    System.out.println("\nRunning DetectFaceDemo");
	    String path = "C:/Users/gonzalo1/javaworkspace/pruebaopencvinstalacion/resources/Group-of-People.png";
	    //Mat image2 = Highgui.imread(getClass().getResource("../resources/lena.png").getPath());
	    // Create a face detector from the cascade file in the resources
	    // directory.
	    CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("../resources/lbpcascade_frontalface.xml").getPath().substring(1));
	    Mat image = Highgui.imread(getClass().getResource("../resources/GroupofPeople.png").getPath().substring(1));

	    // Detect faces in the image.
	    // MatOfRect is a special container class for Rect.
	    MatOfRect faceDetections = new MatOfRect();
	    faceDetector.detectMultiScale(image, faceDetections);

	    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

	    // Draw a bounding box around each face.
	    for (Rect rect : faceDetections.toArray()) {
	        Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
	    }

	    // Save the visualized detection.
	    String filename = "faceDetection.png";
	    System.out.println(String.format("Writing %s", filename));
	    Highgui.imwrite(filename, image);
	  }
}



