package src.main.code;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_contrib.FaceRecognizer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;
import org.bytedeco.javacpp.opencv_core.CvRect;


import static org.bytedeco.javacpp.opencv_contrib.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvEqualizeHist;
import static org.bytedeco.javacpp.opencv_imgproc.CV_INTER_LINEAR;
import static org.bytedeco.javacpp.opencv_imgproc.cvResize;




public class OpenCVFaceRecognizer {
	
	private static final String CASCADE_FILE = "C:/Users/gonzalo1/javaworkspace/pruebajavacv/src/src/main/resources/haarcascade_frontalface_alt.xml";
	final static CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));
	
	 public static void main(String[] args) {
		 
		 	//String path = "../resources";
		 	String path = "C:/Users/gonzalo1/javaworkspace/pruebajavacv/src/src/main/resources";
		 	String pathImg = "../resources2";
		 	String pathImg2 = "C:/Users/gonzalo1/javaworkspace/pruebajavacv/src/src/main/resources2";
	        String trainingDir = path;
	        Mat testImage = imread(pathImg2, CV_LOAD_IMAGE_GRAYSCALE);
	        
	        IplImage target = new IplImage();
            target = cvLoadImage("C:/Users/gonzalo1/javaworkspace/pruebajavacv/src/src/main/resources2/cr7_target.jpg");
            CvSeq faces2 = detectFace(target);
            CvRect r2 = new CvRect(cvGetSeqElem(faces2,0));
            target=preprocessImage(target, r2);
            Mat targetImage = new Mat(target);

	        File root = new File(trainingDir);

	        FilenameFilter imgFilter = new FilenameFilter() {
	            public boolean accept(File dir, String name) {
	                name = name.toLowerCase();
	                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
	            }
	        };

	        File[] imageFiles = root.listFiles(imgFilter);

	        MatVector images = new MatVector(imageFiles.length);

	        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
	        IntBuffer labelsBuf = labels.getIntBuffer();

	        int counter = 0;

	        IplImage[] trainImages = new IplImage[5];
	        
	        MatVector vec = new MatVector();
	        
	        for(int i=1; i <= 5; i++){
                trainImages[i-1]=cvLoadImage("C:/Users/gonzalo1/javaworkspace/pruebajavacv/src/src/main/resources/terry"+i+".jpg");
                CvSeq faces = detectFace(trainImages[i-1]);
                CvRect r = new CvRect(cvGetSeqElem(faces,0));
                //trainImages[i-1]=preprocessImage(trainImages[i-1], r);
                vec.put(i-1, new Mat(preprocessImage(trainImages[i-1], r)));
            }
	        
	       
	        
	        for (File image : imageFiles) {
//	            Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
//
//	            int label = Integer.parseInt(image.getName().split("\\-")[0]);
//
//	          
//              
//	            
//	            images.put(counter, img);
//
//	            labelsBuf.put(counter, label);
//
//	            counter++;
	        }

	        FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
	         //FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
	         //FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();

	        faceRecognizer.train(vec, labels);

	        int predictedLabel = faceRecognizer.predict(targetImage);

	        System.out.println("Predicted label: " + predictedLabel);
	    }
	 
	 protected static CvSeq detectFace(IplImage originalImage) {
         CvSeq faces = null;
         Loader.load(opencv_objdetect.class);
         try {
                 IplImage grayImage = IplImage.create(originalImage.width(), originalImage.height(), IPL_DEPTH_8U, 1);
                 cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);
                 CvMemStorage storage = CvMemStorage.create();
                 faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1, 0);

         } catch (Exception e) {
                 e.printStackTrace();
         }
         return faces;
 }
	 
	 public static IplImage preprocessImage(IplImage image, CvRect r){
         IplImage gray = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
         IplImage roi = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
         CvRect r1 = new CvRect();
         r1.x(r.x()-10);
         r1.y(r.y()-10);
         r1.width(r.width()+10);
         r1.height(r.height()+10);
         //CvRect r1 = new CvRect(r.x()-10, r.y()-10, r.width()+10, r.height()+10);
         cvCvtColor(image, gray, CV_BGR2GRAY);
         cvSetImageROI(gray, r1);
         cvResize(gray, roi, CV_INTER_LINEAR);
         cvEqualizeHist(roi, roi);
         return roi;
 }
}
