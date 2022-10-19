package com.mgdsstudio.blueberet.gamelibraries;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.oldlevelseditor.Point;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.IGame;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.TouchEvent;


import java.io.File;


public abstract class GameMechanics implements IGame{
        private static Vec2 mutVec1 = new Vec2();
        private static Vec2 mutVec2 = new Vec2();
        private static Vec2 mutVec3 = new Vec2();

        public static byte[][] notUpdatedSectors(byte cellsAlongX, byte cellsAlongY, int playerAngle, int playerSeeingAngle){
            return null;
        }



        public static float getVectorAngleInDegrees(Vec2 vector){
            float angleValueInDegrees = 0f;
            angleValueInDegrees = PApplet.degrees(PApplet.atan(vector.y/vector.x));
            if (vector.x>0) {
            }
            else {
                if (angleValueInDegrees>0){
                    angleValueInDegrees+=180;
                }
                else if (angleValueInDegrees<0){
                    angleValueInDegrees-=180;
                }
            }
            if (angleValueInDegrees<0) angleValueInDegrees+=360;
            return angleValueInDegrees;
        }

    public static float getVectorAngleInRadians(Vec2 vector){
        float angleValueInDegrees = getVectorAngleInDegrees(vector);
        return PApplet.radians(angleValueInDegrees);
    }

        public static Vec2 getCenterBetweenTwoPoints(Vec2 firstPoint, Vec2 secondPoint){
            float deltaX = 0f;
            float deltaY = 0f;
            if (secondPoint.x>firstPoint.x) deltaX = secondPoint.x-firstPoint.x;
            else deltaX = firstPoint.x-secondPoint.x;
            if (secondPoint.y>firstPoint.y) deltaY = secondPoint.y-firstPoint.y;
            else deltaY = firstPoint.y-secondPoint.y;
            Vec2 center = new Vec2(firstPoint.x+deltaX/2, (secondPoint.y+deltaY/2));
            return center;
        }

        static PVector getPositionBehindPerson(PVector position, int angle, int distance){
            PVector positionBehindPerson = new PVector(position.x, position.y);
            PVector personPosition = new PVector(position.x, position.y);
            PVector vectorToPositionBehindPerson = new PVector(distance, 0);
            float angleAfterItWasConverted = convertAngleFrom0_to_360ScaleToSimetricalScale(angle);
            /*if (angleAfterItWasConverted < 0 && angleAfterItWasConverted> - 90){
                vectorToPositionBehindPerson.rotate(angleAfterItWasConverted+90);
            }*/
            vectorToPositionBehindPerson.rotate(Program.engine.radians(angleAfterItWasConverted));
            positionBehindPerson = personPosition.sub(vectorToPositionBehindPerson);
            //System.out.println("Player pos: " + position + " angle was: " + angle + " angle is: " + angleAfterItWasConverted);
            //System.out.println("Place behind pos: " + positionBehindPerson);
            return positionBehindPerson;
        }

        public static float convertRadiansToDegrees(float angleInRadians){
            float angleInDegrees = PApplet.degrees(angleInRadians);
            if (angleInDegrees > 360) {
                while (angleInDegrees > 360){
                    angleInDegrees-=360;
                }
            }
            else if (angleInDegrees<-360){
                while (angleInDegrees<-360){
                    angleInDegrees+=360;
                }
            }
            return angleInDegrees;
        }

        public static byte getDirection(int angle){
            if (angle >=22.5f && angle < 67.5f) return SOUTH_EAST_DIRECTION;
            else if (angle >=67.5f && angle < 112.5f) return SOUTH_DIRECTION;
            else if (angle >=112.5f && angle < 157.5f) return SOUTH_WEST_DIRECTION;
            else if (angle >=157.5f && angle < 202.5f) return WEST_DIRECTION;
            else if (angle >=202.5f && angle < 247.5f) return NORTH_WEST_DIRECTION;
            else if (angle >=247.5f && angle < 292.5f) return NORTH_DIRECTION;
            else if (angle >=292.5f && angle < 337.5f) return NORTH_EAST_DIRECTION;
            else return EAST_DIRECTION;
        }

        public static boolean getRotateDirectionFromFirstAngleToSecond(float angle1, float angle2){
            if (angle1 > angle2) {
                if (Program.engine.abs(angle1 - angle2) < 180) return CCW_DIRECTION;
                else return CW_DIRECTION;
            }
            else {
                if (Program.engine.abs(angle1 - angle2) < 180) return CW_DIRECTION;
                else return CCW_DIRECTION;
            }
        }

        public static String[] getListOfAllTileset(){
            File f = null;
            File[] paths;
            try {
                String assetsPath = System.getProperty("user.dir");
                assetsPath+='\\';
                assetsPath+="Assets";
                // create new file
                f = new File(assetsPath);
                // returns pathnames for files and directory
                paths = f.listFiles();

                // for each pathname in pathname array
                for(File path:paths) {

                    // prints file and directory paths
                    //System.out.println(path);
                }
                String [] data = new String[paths.length];
                for (int i = 0; i < data.length; i++){
                    data[i] = String.valueOf(paths[i]);
                }
                for(String path:data) {
                    // prints file and directory paths
                    System.out.println(path);
                }
                return data;

            } catch(Exception e) {
                System.out.println("Can not get list of files in Assets folder");
                e.printStackTrace();
            }
            return null;
        }

    private static Vec2 getRightLowerVectorFromTwo(Vec2 firstPoint, Vec2 secondPoint){
        if (firstPoint.x<secondPoint.x){
            if (firstPoint.y<secondPoint.y){
                return secondPoint;
            }
            else{
                return new Vec2(secondPoint.x, firstPoint.y);
            }
        }
        else{
            if (firstPoint.y>secondPoint.y){
                return firstPoint;
            }
            else {
                return new Vec2(firstPoint.x, secondPoint.y);
            }
        }
    }

        private static Vec2 getLeftUpperVectorFromTwo(Vec2 firstPoint, Vec2 secondPoint){
            if (firstPoint.x<secondPoint.x){
                if (firstPoint.y<secondPoint.y){
                    return firstPoint;
                }
                else{
                    return new Vec2(firstPoint.x, secondPoint.y);
                }
            }
            else{
                if (firstPoint.y>secondPoint.y){
                    return secondPoint;
                }
                else {
                    return new Vec2(secondPoint.x, firstPoint.y);
                }
            }
        }

        public static Vec2 getAlignedRectCenter(Vec2 firstPoint, Vec2 secondPoint){
            Vec2 center = new Vec2();
            Vec2 leftUpper = getLeftUpperVectorFromTwo(firstPoint, secondPoint);
            Vec2 rightLower = getRightLowerVectorFromTwo(firstPoint, secondPoint);
            float width = rightLower.x - leftUpper.x;
            float height = rightLower.y-leftUpper.y;

            center.x=leftUpper.x+width/2;
            center.y = leftUpper.y+height/2;
            /*

            if (firstPoint.x < secondPoint.x) center.x = firstPoint.x + width / 2;
            else center.x = secondPoint.x + width / 2;
            if (firstPoint.y < secondPoint.y) center.y = firstPoint.y + height / 2;
            else center.y = secondPoint.y + height / 2;
            */
            /*
            float width = PApplet.abs(PApplet.abs(secondPoint.x) - PApplet.abs(firstPoint.x));
            float height = PApplet.abs(PApplet.abs(secondPoint.y) - PApplet.abs(firstPoint.y));
            if (firstPoint.x < secondPoint.x) center.x = firstPoint.x + width / 2;
            else center.x = secondPoint.x + width / 2;
            if (firstPoint.y < secondPoint.y) center.y = firstPoint.y + height / 2;
            else center.y = secondPoint.y + height / 2;
            */

            /*
            float width = PApplet.abs(PApplet.abs(secondPoint.x) - PApplet.abs(firstPoint.x));
            float height = PApplet.abs(PApplet.abs(secondPoint.y) - PApplet.abs(firstPoint.y));
            if (firstPoint.x < secondPoint.x) center.x = firstPoint.x + width / 2;
            else center.x = secondPoint.x + width / 2;
            if (firstPoint.y < secondPoint.y) center.y = firstPoint.y + height / 2;
            else center.y = secondPoint.y + height / 2;
            */

            //if (((firstPoint.x>0 && secondPoint.x>0)&&(firstPoint.y>0 && secondPoint.y>0))||((firstPoint.x<0 && secondPoint.x<0)&&(firstPoint.y<0 && secondPoint.y<0))) {
            //
            //if (((firstPoint.x>0 && secondPoint.x<0)&&(firstPoint.y>0 && secondPoint.y>0))||((firstPoint.x<0 && secondPoint.x<0)&&(firstPoint.y<0 && secondPoint.y<0))) {


                /*
                float width = PApplet.abs(PApplet.abs(secondPoint.x) - PApplet.abs(firstPoint.x));
                float height = PApplet.abs(PApplet.abs(secondPoint.y) - PApplet.abs(firstPoint.y));
                if (firstPoint.x < secondPoint.x) center.x = firstPoint.x + width / 2;
                else center.x = secondPoint.x + width / 2;
                if (firstPoint.y < secondPoint.y) center.y = firstPoint.y + height / 2;
                else center.y = secondPoint.y + height / 2;
                */
            //}
           // else{
            /*
                float width = PApplet.abs(PApplet.abs(secondPoint.x) - PApplet.abs(firstPoint.x));
                float height = PApplet.abs(PApplet.abs(secondPoint.y) - PApplet.abs(firstPoint.y));
                if (firstPoint.x < secondPoint.x) center.x = firstPoint.x + width / 2;
                else center.x = secondPoint.x + width / 2;
                if (firstPoint.y < secondPoint.y) center.y = firstPoint.y + height / 2;
                else center.y = secondPoint.y + height / 2;
                */
            //}
            return center;
        }
        
        public static boolean isPointInCircle(PVector point, PVector center, float radius) {
        	if (Program.engine.dist(center.x, center.y, point.x, point.y) <= radius) return true;
        	else return false;
        }

    public static boolean isPointInCircle(Vec2 point, Vec2 center, float radius) {
        if (Program.engine.dist(center.x, center.y, point.x, point.y) <= radius) return true;
        else return false;
    }

    public static boolean isPointInCircle(int x1, int y1,  int x2, int y2, float radius) {
        if (Program.engine.dist(x1, y1, x2, y2) <= radius) return true;
        else return false;
    }

    public static boolean isPointInCircle(float x1, float y1,  float x2, float y2, float radius) {
        if (Program.engine.dist(x1, y1, x2, y2) <= radius) return true;
        else return false;
    }


        public static boolean isPointInCircle(TouchEvent.Pointer point, PVector center, float radius) {
            if (Program.engine.dist(center.x, center.y, point.x, point.y) <= radius) return true;
            else return false;
        }

        public static boolean intersectionLineWithCircle(PVector lineFirstPoint, PVector lineSecondPoint, PVector circleCenter, int radius){
            final float lineAngle = angleDetermining(lineFirstPoint, lineSecondPoint);  // bestimmen des Winkelwert der Linie
            final float angleToCircleCenter = angleDetermining(lineFirstPoint, circleCenter);
            final float angleDirectionFromCenterToNearestPoint = angleToCircleCenter + 90;
            PVector vectorToNearestPoint = new PVector(radius* Program.engine.cos(Program.engine.radians(angleDirectionFromCenterToNearestPoint)), radius* Program.engine.sin(Program.engine.radians(angleDirectionFromCenterToNearestPoint)));
            PVector nearestPointOnCircle = new PVector(circleCenter.x, circleCenter.y);
            nearestPointOnCircle.add(vectorToNearestPoint);
            PVector vectorToFurthermostPoint = new PVector(-radius* Program.engine.cos(Program.engine.radians(angleDirectionFromCenterToNearestPoint)), -radius* Program.engine.sin(Program.engine.radians(angleDirectionFromCenterToNearestPoint)));
            PVector furthermostPointOnCircle = new PVector(circleCenter.x, circleCenter.y);
            furthermostPointOnCircle.add(vectorToFurthermostPoint);
            final float minAngle = angleDetermining( lineFirstPoint, nearestPointOnCircle);
            final float maxAngle = angleDetermining( lineFirstPoint, furthermostPointOnCircle);
            //println("minAngle=" + minAngle + "; maxAngle = " + maxAngle + ". Line angle: " + lineAngle + " . angleToCircleCenter = "  + angleToCircleCenter);
            if ((lineAngle >= minAngle && lineAngle <= maxAngle && maxAngle > minAngle) ||
                    (lineAngle <= minAngle && lineAngle >= maxAngle && maxAngle > minAngle) ||
                    (maxAngle <= minAngle && lineAngle <= maxAngle && lineAngle > 0)||
                    (maxAngle <= minAngle && lineAngle >= minAngle && lineAngle <= 360)) return true;  // zwei letze sind falsh
            else return false;
        }

        public static byte getMaximalValueFromArray(float[] array){
            if (array.length<127){
                byte maxElementNumber = 0;
                float maxElementValue = array[0];
                for (byte i = 1; i < array.length; i++){
                    if (array[i]>maxElementValue){
                        maxElementValue = array[i];
                        maxElementNumber = i;
                    }
                }
                return maxElementNumber;
            }
            else {
                System.out.println("The array is too short!");
                return (byte)0;
            }
        }

        public static byte getMinimalValueFromArray(float[] array){
            if (array.length<127){
                byte minElementNumber = 0;
                float minElementValue = array[0];
                for (byte i = 1; i < array.length; i++){
                    if (array[i]<minElementValue){
                        minElementValue = array[i];
                        minElementNumber = i;
                    }
                }
                return minElementNumber;
            }
            else {
                System.out.println("The array is too short!");
                return (byte)0;
            }
        }

        public static boolean intersectionLineWithNonisometricRect(PVector lineFirstPoint, PVector lineSecondPoint, PVector rectLeftUpperCorner, PVector rectRightDownCorner){
            float lineAngle = angleDetermining(lineFirstPoint, lineSecondPoint);  // bestimmen des Winkelwert der Linie
            PVector rectRightUpperCorner = new PVector (rectRightDownCorner.x, rectLeftUpperCorner.y);
            PVector rectLeftDownCorner = new PVector (rectLeftUpperCorner.x, rectRightDownCorner.y);
            float [] anglesToRectCorners = new float[4];
            anglesToRectCorners[0] = angleDetermining(lineFirstPoint, rectLeftUpperCorner);  // bestimmen des Winkelwerts zur Ecke
            anglesToRectCorners[1] = angleDetermining(lineFirstPoint, rectRightUpperCorner);  // bestimmen des Winkelwerts zur Ecke
            anglesToRectCorners[2] = angleDetermining(lineFirstPoint, rectLeftDownCorner);  // bestimmen des Winkelwerts zur Ecke
            anglesToRectCorners[3] = angleDetermining(lineFirstPoint, rectRightDownCorner);  // bestimmen des Winkelwerts zur Ecke
            byte minAngleCornerNumber = getMinimalValueFromArray(anglesToRectCorners);
            byte maxAngleCornerNumber = getMaximalValueFromArray(anglesToRectCorners);
            //println("Min angle: " + anglesToRectCorners[minAngleCornerNumber] + ". Max angle: " + anglesToRectCorners[maxAngleCornerNumber] + ". Line angle: " + lineAngle);
            if (anglesToRectCorners[maxAngleCornerNumber] - anglesToRectCorners[minAngleCornerNumber] > 180) {
                float valueToBeSave = anglesToRectCorners[minAngleCornerNumber];
                anglesToRectCorners[minAngleCornerNumber] = anglesToRectCorners[maxAngleCornerNumber];
                anglesToRectCorners[maxAngleCornerNumber] = valueToBeSave;
            }
            if ((lineAngle >= anglesToRectCorners[minAngleCornerNumber] && lineAngle <= anglesToRectCorners[maxAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber] > anglesToRectCorners[minAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber]>anglesToRectCorners[minAngleCornerNumber]) ||
                    (lineAngle <= anglesToRectCorners[minAngleCornerNumber] && lineAngle >= anglesToRectCorners[maxAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber] > anglesToRectCorners[minAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber]>anglesToRectCorners[minAngleCornerNumber]) ||
                    (anglesToRectCorners[maxAngleCornerNumber]<anglesToRectCorners[minAngleCornerNumber] && lineAngle <= anglesToRectCorners[maxAngleCornerNumber]) ||
                    (anglesToRectCorners[maxAngleCornerNumber]<anglesToRectCorners[minAngleCornerNumber] && lineAngle >= anglesToRectCorners[minAngleCornerNumber])
            ) return true;
            else return false;
        }

    public static int rotateAngleTo180(int angle) {
            angle-=180;
            if (angle < 0) angle = 360+angle;
            else if (angle>360) angle-=360;
            return angle;
    }

    public static Vec2 getTriangleGraphicCenter(Point firstPoint, Point secondPoint, Point thirdPoint) {
        Vec2 center = new Vec2();
        float left = firstPoint.getPosition().x;
        if (secondPoint.getPosition().x<left) left = secondPoint.getPosition().x;
        if (thirdPoint.getPosition().x<left) left = thirdPoint.getPosition().x;
        float width = getTriangleWidthByPoints(firstPoint, secondPoint, thirdPoint);
        center.x = left+width/2f;
        float upper = firstPoint.getPosition().y;
        if (secondPoint.getPosition().y<upper) upper=secondPoint.getPosition().y;
        if (thirdPoint.getPosition().y<upper) upper=thirdPoint.getPosition().y;
        float height = getTriangleHeightByPoints(firstPoint, secondPoint, thirdPoint);
        center.y = upper+height/2f;
        return center;
    }

    public static Vec2 getTriangleGraphicCenter(Vec2 first, Vec2 second, Vec2 third) {
        Coordinate firstPoint = new Coordinate(first.x, first.y);
        Coordinate secondPoint = new Coordinate(second.x, second.y);
        Coordinate thirdPoint = new Coordinate(third.x, third.y);
        Point p1 = new Point(new PVector(firstPoint.x, firstPoint.y));
        Point p2 = new Point(new PVector(secondPoint.x, secondPoint.y));
        Point p3 = new Point(new PVector(thirdPoint.x, thirdPoint.y));
        Vec2 center = getTriangleGraphicCenter(p1,p2,p3);
        return center;
    }

    public static Vec2 getTriangleGraphicCenter(Coordinate firstPoint, Coordinate secondPoint, Coordinate thirdPoint) {
            Point p1 = new Point(new PVector(firstPoint.x, firstPoint.y));
        Point p2 = new Point(new PVector(secondPoint.x, secondPoint.y));
        Point p3 = new Point(new PVector(thirdPoint.x, thirdPoint.y));
        Vec2 center = getTriangleGraphicCenter(p1,p2,p3);
        return center;
    }


    public static Vec2 getTriangleGeometricalCenter(Point firstPoint, Point secondPoint, Point thirdPoint) {
        Vec2 center = new Vec2((firstPoint.getPosition().x + secondPoint.getPosition().x+thirdPoint.getPosition().x)/3f, (firstPoint.getPosition().y + secondPoint.getPosition().y+thirdPoint.getPosition().y)/3f);
        return center;
        }

    public static Vec2 getTriangleGeometricalCenter(Coordinate firstPoint, Coordinate secondPoint, Coordinate thirdPoint) {
        Vec2 center = new Vec2((firstPoint.x + secondPoint.x+thirdPoint.x)/3f, (firstPoint.y + secondPoint.y+thirdPoint.y)/3f);
        return center;
    }

    public static Vec2 getTriangleGeometricalCenter(Vec2 firstPoint, Vec2 secondPoint, Vec2 thirdPoint) {
        Vec2 center = new Vec2((firstPoint.x + secondPoint.x+thirdPoint.x)/3f, (firstPoint.y + secondPoint.y+thirdPoint.y)/3f);
        return center;
    }

    public static int getTriangleWidthByPoints(Point firstPoint, Point secondPoint, Point thirdPoint) {
         float leftPoint = firstPoint.getPosition().x;
         if (secondPoint.getPosition().x<leftPoint) leftPoint = secondPoint.getPosition().x;
         if (thirdPoint.getPosition().x<leftPoint) leftPoint = thirdPoint.getPosition().x;
         float rightPoint = thirdPoint.getPosition().x;
         if (secondPoint.getPosition().x>rightPoint) rightPoint = secondPoint.getPosition().x;
         if (firstPoint.getPosition().x>rightPoint) rightPoint = firstPoint.getPosition().x;
         return (int)(rightPoint-leftPoint);
    }

    public static int getTriangleWidthByCoordinates(Coordinate firstPoint, Coordinate secondPoint, Coordinate thirdPoint) {
        float leftPoint = firstPoint.x;
        if (secondPoint.x<leftPoint) leftPoint = secondPoint.x;
        if (thirdPoint.x<leftPoint) leftPoint = thirdPoint.x;
        float rightPoint = thirdPoint.x;
        if (secondPoint.x>rightPoint) rightPoint = secondPoint.x;
        if (firstPoint.x>rightPoint) rightPoint = firstPoint.x;
        return (int)(rightPoint-leftPoint);
    }

    public static int getTriangleWidthByVectors(Vec2 firstPoint, Vec2 secondPoint, Vec2 thirdPoint) {
        float leftPoint = firstPoint.x;
        if (secondPoint.x<leftPoint) leftPoint = secondPoint.x;
        if (thirdPoint.x<leftPoint) leftPoint = thirdPoint.x;
        float rightPoint = thirdPoint.x;
        if (secondPoint.x>rightPoint) rightPoint = secondPoint.x;
        if (firstPoint.x>rightPoint) rightPoint = firstPoint.x;
        return (int)(rightPoint-leftPoint);
    }

    public static int getTriangleHeightByPoints(Point firstPoint, Point secondPoint, Point thirdPoint) {
        float upperPoint = firstPoint.getPosition().y;
        if (secondPoint.getPosition().y<upperPoint) upperPoint = secondPoint.getPosition().y;
        if (thirdPoint.getPosition().y<upperPoint) upperPoint = thirdPoint.getPosition().y;
        float lowerPoint = thirdPoint.getPosition().y;
        if (secondPoint.getPosition().y>lowerPoint) lowerPoint = secondPoint.getPosition().y;
        if (firstPoint.getPosition().y>lowerPoint) lowerPoint = firstPoint.getPosition().y;
        return (int)(lowerPoint-upperPoint);
    }

    public static int getTriangleHeightByCoordinates(Coordinate firstPoint, Coordinate secondPoint, Coordinate thirdPoint) {
        float upperPoint = firstPoint.y;
        if (secondPoint.y<upperPoint) upperPoint = secondPoint.y;
        if (thirdPoint.y<upperPoint) upperPoint = thirdPoint.y;
        float lowerPoint = thirdPoint.y;
        if (secondPoint.y>lowerPoint) lowerPoint = secondPoint.y;
        if (firstPoint.y>lowerPoint) lowerPoint = firstPoint.y;
        return (int)(lowerPoint-upperPoint);
    }

    public static int getTriangleHeightByVectors(Vec2 firstPoint, Vec2 secondPoint, Vec2 thirdPoint) {
        float upperPoint = firstPoint.y;
        if (secondPoint.y<upperPoint) upperPoint = secondPoint.y;
        if (thirdPoint.y<upperPoint) upperPoint = thirdPoint.y;
        float lowerPoint = thirdPoint.y;
        if (secondPoint.y>lowerPoint) lowerPoint = secondPoint.y;
        if (firstPoint.y>lowerPoint) lowerPoint = firstPoint.y;
        return (int)(lowerPoint-upperPoint);
    }

    public static boolean isPointInRect(float pointX, float pointY, float centerX, float centerY, int width, int height, float angle) {
            //Find rotated point
            float simpleAngle = angle;
            if (angle > 90 && angle < 270) {
                angle-=180;
            }
            mutVec1 = rotatedPoint(angle, pointX, pointY, mutVec1);
            //System.out.println("Old pos; " + (int) pointX + "x" + (int)pointY +"; New: " + (int)mutVec1.x + "x"+ (int)mutVec1.y);
            //Find rotated center
            mutVec2 = rotatedPoint(angle, centerX, centerY, mutVec2);
            System.out.println("Bullet; " + (int)mutVec1.x + "x"+ (int)mutVec1.y + " rect: " + (int)centerX + " x " + (int)centerY + "; Width: " + width +"x" + height);
            return isPointInRect(mutVec1, mutVec2, width, height);
    }

    private static Vec2 rotatedPoint(float rotationAngle, float x, float y, Vec2 result){

            float hipotenuze = PApplet.sqrt(x*x+y*y);
            float pointAngle = PApplet.acos(x/hipotenuze);
            float newAngle = pointAngle-PApplet.radians(rotationAngle);
            //System.out.println("New angle: " + PApplet.degrees(newAngle) + "; Luggage angle: " + rotationAngle + "; Start angle to point: " + PApplet.degrees(pointAngle));
            result.x = hipotenuze*PApplet.cos((newAngle));
            result.y = hipotenuze*PApplet.sin((newAngle));
            return result;
    }

    public static float getAngleToObject(PVector source, PVector target) {
           // float angleInRad = PApplet.sqrt(PApplet.sq(target.x-source.x)+PApplet.sq(target.y-source.y));
            float angleInRad = PApplet.atan((PApplet.abs(target.y-source.y))/(PApplet.abs(target.x-source.x)));
            float inDegrees = PApplet.degrees(angleInRad);
            int quarter = getQuarter(source, target);
            if (quarter == 1){
                return (360-inDegrees);
            }
            else if (quarter == 3){
                return (180-inDegrees);
            }
            else if (quarter == 2){
                return (180+inDegrees);
            }
            return inDegrees;
    }

    private static int getQuarter(PVector source, PVector target) {
         if (target.x>source.x)   {
             if (target.y>source.y){
                 return 4;  //Right lower
             }
             else return 1;

         }
         else {
             if (target.y>source.y){
                 return 3;  //Right lower
             }
             else return 2;
         }
    }

    public boolean intersectionLineWithIsometricRect(PVector lineFirstPoint, PVector lineSecondPoint, PVector centerPosition, int squareSide){
            final float halfDiagonal = squareSide/ Program.engine.sqrt(2);
            float lineAngle = angleDetermining(lineFirstPoint, lineSecondPoint);  // bestimmen des Winkelwert der Linie
            PVector upperCorner = new PVector (centerPosition.x, centerPosition.y-halfDiagonal);
            PVector rightCorner = new PVector (centerPosition.x+halfDiagonal, centerPosition.y);
            PVector downCorner = new PVector (centerPosition.x, centerPosition.y+halfDiagonal);
            PVector leftCorner = new PVector (centerPosition.x-halfDiagonal, centerPosition.y);
            float [] anglesToRectCorners = new float[4];
            anglesToRectCorners[0] = angleDetermining(lineFirstPoint, upperCorner);  // bestimmen des Winkelwerts zur Ecke
            anglesToRectCorners[1] = angleDetermining(lineFirstPoint, rightCorner);  // bestimmen des Winkelwerts zur Ecke
            anglesToRectCorners[2] = angleDetermining(lineFirstPoint, downCorner);  // bestimmen des Winkelwerts zur Ecke
            anglesToRectCorners[3] = angleDetermining(lineFirstPoint, leftCorner);  // bestimmen des Winkelwerts zur Ecke
            byte minAngleCornerNumber = getMinimalValueFromArray(anglesToRectCorners);
            byte maxAngleCornerNumber = getMaximalValueFromArray(anglesToRectCorners);
            //println("Min angle: " + anglesToRectCorners[minAngleCornerNumber] + ". Max angle: " + anglesToRectCorners[maxAngleCornerNumber] + ". Line angle: " + lineAngle);
            if (anglesToRectCorners[maxAngleCornerNumber] - anglesToRectCorners[minAngleCornerNumber] > 180) {
                float valueToBeSave = anglesToRectCorners[minAngleCornerNumber];
                anglesToRectCorners[minAngleCornerNumber] = anglesToRectCorners[maxAngleCornerNumber];
                anglesToRectCorners[maxAngleCornerNumber] = valueToBeSave;
            }
            if ((lineAngle >= anglesToRectCorners[minAngleCornerNumber] && lineAngle <= anglesToRectCorners[maxAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber] > anglesToRectCorners[minAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber]>anglesToRectCorners[minAngleCornerNumber]) ||
                    (lineAngle <= anglesToRectCorners[minAngleCornerNumber] && lineAngle >= anglesToRectCorners[maxAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber] > anglesToRectCorners[minAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber]>anglesToRectCorners[minAngleCornerNumber]) ||
                    (anglesToRectCorners[maxAngleCornerNumber]<anglesToRectCorners[minAngleCornerNumber] && lineAngle <= anglesToRectCorners[maxAngleCornerNumber]) ||
                    (anglesToRectCorners[maxAngleCornerNumber]<anglesToRectCorners[minAngleCornerNumber] && lineAngle >= anglesToRectCorners[minAngleCornerNumber])
            ) return true;
            else return false;
        }

        public static boolean intersectionLineWithTriangle(PVector lineFirstPoint, PVector lineSecondPoint, PVector firstCorner, PVector secondCorner, PVector thirdCorner){
            float lineAngle = angleDetermining(lineFirstPoint, lineSecondPoint);  // bestimmen des Winkelwert der Linie
            float [] anglesToRectCorners = new float[3];
            anglesToRectCorners[0] = angleDetermining(lineFirstPoint, firstCorner);  // bestimmen des Winkelwerts zur Ecke
            anglesToRectCorners[1] = angleDetermining(lineFirstPoint, secondCorner);  // bestimmen des Winkelwerts zur Ecke
            anglesToRectCorners[2] = angleDetermining(lineFirstPoint, thirdCorner);  // bestimmen des Winkelwerts zur Ecke
            byte minAngleCornerNumber = getMinimalValueFromArray(anglesToRectCorners);
            byte maxAngleCornerNumber = getMaximalValueFromArray(anglesToRectCorners);
            if (anglesToRectCorners[maxAngleCornerNumber] - anglesToRectCorners[minAngleCornerNumber] > 180) {
                float valueToBeSave = anglesToRectCorners[minAngleCornerNumber];
                anglesToRectCorners[minAngleCornerNumber] = anglesToRectCorners[maxAngleCornerNumber];
                anglesToRectCorners[maxAngleCornerNumber] = valueToBeSave;
            }
            if ((lineAngle >= anglesToRectCorners[minAngleCornerNumber] && lineAngle <= anglesToRectCorners[maxAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber] > anglesToRectCorners[minAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber]>anglesToRectCorners[minAngleCornerNumber]) ||
                    (lineAngle <= anglesToRectCorners[minAngleCornerNumber] && lineAngle >= anglesToRectCorners[maxAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber] > anglesToRectCorners[minAngleCornerNumber] && anglesToRectCorners[maxAngleCornerNumber]>anglesToRectCorners[minAngleCornerNumber]) ||
                    (anglesToRectCorners[maxAngleCornerNumber]<anglesToRectCorners[minAngleCornerNumber] && lineAngle <= anglesToRectCorners[maxAngleCornerNumber]) ||
                    (anglesToRectCorners[maxAngleCornerNumber]<anglesToRectCorners[minAngleCornerNumber] && lineAngle >= anglesToRectCorners[minAngleCornerNumber])
            ) return true;
            else return false;
        }

     static float convertAngleFrom0_to_360ScaleToSimetricalScale(float angleToBeConvert){  // Maybe error ! from -180- to 180 into 0 to 360
        float angleToBeReturn = angleToBeConvert;
        if (angleToBeReturn<180) return angleToBeReturn;
        else{
            angleToBeReturn-=360;
            return angleToBeReturn;
        }

    }

        public static float convertAngleFromSimetricalScaleInto0_to_360Scale(float angleToBeConvert){  // Maybe error ! from -180- to 180 into 0 to 360
            float angleToBeReturn = angleToBeConvert+180;
            if (angleToBeConvert > 360) angleToBeReturn = 360-angleToBeReturn;
            return angleToBeReturn;
        }

        public static float convertAngleInto0_to_360Scale(float angleToBeConvert){  // from -180- to 180 into 0 to 360
            float angleToBeReturn = angleToBeConvert;
            if (angleToBeConvert < 0) angleToBeReturn = 360+angleToBeConvert;
            return angleToBeReturn;
        }

        public static int angleDetermining(int x1, int y1, int x2, int y2 ){
            int angle = (int) Program.engine.degrees(Program.engine.atan2(y1-y2, x1-x2));
            if (angle < 0) {
                angle= 360 + angle;
            }
            return angle;
        }

        public static float angleDetermining(float x1, float y1, float x2, float y2){
            float angle = Program.engine.degrees(Program.engine.atan2(y1-y2, x1-x2));
            if (angle < 0) {
                angle= 360 + angle;
            }
            return angle;
        }

  /*public static float angleDetermining(PVector object1, PVector object2 ){
    float angle = degrees(atan2(object1.y-object2.y, object1.x-object2.x));
    if (angle < 0) {
       angle= 360 + angle;
    }
    return angle;
  }
  */

    static int getVectorAngleFrom0_to_360(PVector vector){
        final float angle = convertAngleInto0_to_360Scale(Program.engine.degrees(vector.heading()));
        return (int)angle;
    }

        public static int angleDetermining(PVector object1, PVector object2 ){
            int angle = (int) Program.engine.degrees(Program.engine.atan2(object1.y-object2.y, object1.x-object2.x));
            if (angle < 0) {
                angle= 360 + angle;
            }
            return angle;
        }

        public static float getDeltaAngle(PVector firstVector, PVector secondVector){  // Diese Funktion liefet Werte von -180 bis 180 zurueck!
            final float angle1 = Program.engine.degrees(firstVector.heading());
            final float angle2 = Program.engine.degrees(secondVector.heading());
            float angleToBeReturn = angle1 - angle2;
            if (angle1 > angle2) {
                //angleToBeReturn = angle1-angle2;
                if (Program.engine.abs(angleToBeReturn) > 180) angleToBeReturn = 360-angle1+angle2;
            }
            else {
                if (Program.engine.abs(angleToBeReturn) > 180) angleToBeReturn = 360-angle2+angle1;
            }
            return angleToBeReturn;
        }

        public static int getDeltaAngleAbsolutValue(int angle1, int angle2){// Diese Funktion liefert absolute Werte von bis 180 zurueck!
            int angleToBeReturn = 0;
            if (angle1 > angle2) {
                angleToBeReturn = angle1-angle2;
                if (angleToBeReturn > 180) angleToBeReturn = 360-angle1+angle2;
            }
            else {
                angleToBeReturn = angle2-angle1;
                if (angleToBeReturn > 180) angleToBeReturn = 360-angle2+angle1;
            }
            return angleToBeReturn;
        }

        public static float getDeltaAngleAbsolutValue(float angle1, float angle2){// Diese Funktion liefert absolute Werte von bis 180 zurueck!
            float angleToBeReturn = 0;
            if (angle1 > angle2) {
                angleToBeReturn = angle1-angle2;
                if (angleToBeReturn > 180) angleToBeReturn = 360-angle1+angle2;
            }
            else {
                angleToBeReturn = angle2-angle1;
                if (angleToBeReturn > 180) angleToBeReturn = 360-angle2+angle1;
            }
            return angleToBeReturn;
        }

        public static int getDeltaAngle(int angle1, int angle2){// Diese Funktion liefert Werte von bis 180 zurueck! Der Unterschied muss weniger als 180 Grad sein
            int angleToBeReturn = 0;
            if (angle1 > angle2) {
                angleToBeReturn = angle1-angle2;
                if (angleToBeReturn > 180) angleToBeReturn = 360-angle1+angle2;
            }
            else {
                angleToBeReturn = angle2-angle1;
                if (angleToBeReturn > 180) angleToBeReturn = 360-angle2+angle1;
            }
            return angleToBeReturn;
        }

        public static boolean canPersonBeSawUnderOverviewAngle(PVector playerPosition, int playerAngle, int playerOverviewAngle, PVector personPosition){
            int angleToPerson = angleDetermining(personPosition, playerPosition);
            int deltaAngle = getDeltaAngle(playerAngle, angleToPerson);
            if (deltaAngle < (playerOverviewAngle/2)) return true;
            else return false;
        }

        public static boolean isPointInRect(PVector point, PVector rectLeftUpperCorner, int rectWidth, int rectHeight){
            if (point.x>rectLeftUpperCorner.x &&
                point.x<(rectLeftUpperCorner.x+rectWidth) &&
                point.y>rectLeftUpperCorner.y &&
                point.y<(rectLeftUpperCorner.y+rectHeight)) return true;
                else return false;
        }



    public static boolean isPointInRect(Vec2 point, Vec2 rectLeftUpperCorner, float rectWidth, float rectHeight){
        if (point.x>rectLeftUpperCorner.x &&
                point.x<(rectLeftUpperCorner.x+rectWidth) &&
                point.y>rectLeftUpperCorner.y &&
                point.y<(rectLeftUpperCorner.y+rectHeight)) return true;
        else return false;
    }

    public static boolean isPointInRect(Vec2 point, PVector rectLeftUpperCorner, float rectWidth, float rectHeight){
        if (point.x>rectLeftUpperCorner.x &&
                point.x<(rectLeftUpperCorner.x+rectWidth) &&
                point.y>rectLeftUpperCorner.y &&
                point.y<(rectLeftUpperCorner.y+rectHeight)) return true;
        else return false;
    }

    public static boolean isPointInRect(int pointX, int pointY, int leftUpperX, int leftUpperY, int rectWidth, int rectHeight){
        if (pointX>leftUpperX &&
                pointX<(leftUpperX+rectWidth) &&
                pointY>leftUpperY &&
                pointY<(leftUpperY+rectHeight)) return true;
        else return false;
    }

    public static boolean isPointInRect(float pointX, float pointY, float leftUpperX, float leftUpperY,float rectWidth, float rectHeight){
        if (pointX>leftUpperX &&
                pointX<(leftUpperX+rectWidth) &&
                pointY>leftUpperY &&
                pointY<(leftUpperY+rectHeight)) return true;
        else return false;
    }

    public static boolean isPointInRect(float pointX, float pointY, Rectangular rect){
        if (pointX>rect.getLeftUpperX() &&
                pointX<(rect.getRightLowerX()) &&
                pointY>rect.getLeftUpperY() &&
                pointY<(rect.getRightLowerY())) return true;
        else return false;
    }

    public static boolean isPointInRect(PVector point, Flag flag){
        if (point.x>(flag.getPosition().x-flag.getWidth()/2) &&
                point.x<(flag.getPosition().x+flag.getWidth()/2) &&
                point.y>(flag.getPosition().y-flag.getHeight()/2) &&
                point.y<(flag.getPosition().y+flag.getHeight()/2)) return true;
        else return false;
    }


        public static boolean isPointInRect(Vec2 point, Vec2 center, int rectWidth, int rectHeight){
            if (point.x>(center.x-rectWidth/2) &&
                    point.x<(center.x+rectWidth/2) &&
                    point.y>(center.y-rectHeight/2) &&
                    point.y<(center.y+rectHeight/2)) return true;
            else return false;
        }

    public static boolean isIntersectionBetweenTwoLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

        float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
        float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
            return true;
        }

        return false;
    }

    public static Vec2 pVectorToVec2(PVector pVector){
        return new Vec2(pVector.x, pVector.y);
    }

    /* This code works
    float x1 = 0;      // points for line (controlled by mouse)
float y1 = 0;
float x2 = 0;      // static point
float y2 = 0;

float sx = 200;    // square position
float sy = 100;
float sw = 200;    // and size
float sh = 200;


void setup() {
  size(600, 400);

  strokeWeight(5);  // make the line easier to see
}


void draw() {
  background(255);

  // set end of line to mouse coordinates
  x1 = mouseX;
  y1 = mouseY;

  // check if line has hit the square
  // if so, change the fill color
  boolean hit = lineRect(x1,y1,x2,y2, sx,sy,sw,sh);
  if (hit) fill(255,150,0);
  else fill(0,150,255);
  noStroke();
  rect(sx, sy, sw, sh);

  // draw the line
  stroke(0, 150);
  line(x1, y1, x2, y2);
}


// LINE/RECTANGLE
boolean lineRect(float x1, float y1, float x2, float y2, float rx, float ry, float rw, float rh) {

  // check if the line has hit any of the rectangle's sides
  // uses the Line/Line function below
  boolean left =   lineLine(x1,y1,x2,y2, rx,ry,rx, ry+rh);
  boolean right =  lineLine(x1,y1,x2,y2, rx+rw,ry, rx+rw,ry+rh);
  boolean top =    lineLine(x1,y1,x2,y2, rx,ry, rx+rw,ry);
  boolean bottom = lineLine(x1,y1,x2,y2, rx,ry+rh, rx+rw,ry+rh);

  // if ANY of the above are true, the line
  // has hit the rectangle
  if (left || right || top || bottom) {
    return true;
  }
  return false;
}


// LINE/LINE
boolean lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

  // calculate the direction of the lines
  float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
  float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));

  // if uA and uB are between 0-1, lines are colliding
  if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {

    // optionally, draw a circle where the lines meet
    float intersectionX = x1 + (uA * (x2-x1));
    float intersectionY = y1 + (uA * (y2-y1));
    fill(255,0,0);
    noStroke();
    ellipse(intersectionX, intersectionY, 20, 20);

    return true;
  }
  return false;
}
    */

    public static boolean lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
        float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
            return true;
        }
        return false;
    }

    public static boolean colisionPointWithAlignedRect(Vec2 point, Vec2 center, float width, float height){
        if (point.x>=(center.x-width/2) && point.x <= (center.x+width/2) &&
            point.y>=(center.y-height/2) && point.y<=(center.y+height/2)){
            return true;
        }
        else return false;
    }

    public static boolean colisionPointWithAlignedRect(float pointX, float pointY, float centerX, float centerY, float width, float height){
        if (pointX>=(centerX-width/2) && pointX <= (centerX+width/2) &&
                pointY>=(centerY-height/2) && pointY<=(centerY+height/2)){
            return true;
        }
        else return false;
    }

    public static boolean colisionPointWithAlignedRect(float pointX, float pointY, RectangularWithAngle rectangular){
        Vec2 [] points = rectangular.points;
        if (pointX>=(points[0].x) && pointX <= (points[1].x) &&
                pointY>=(points[0].y) && pointY<=(points[2].y)){
            return true;
        }
        else return false;
    }

    public static void getRectangularFromData(RectangularWithAngle resultRectangular, Vec2 allignedRectLeftUpperCorner, float allingedRectWidth, float allingedRectHeight){
        resultRectangular.setNewPoints(allignedRectLeftUpperCorner, allingedRectWidth, allingedRectHeight);
    }
    /*
    public static boolean coalisionAllignedRectWithRect(RectangularWithAngle first , RectangularWithAngle second, Vec2 allignedRectLeftUpperCorner, float allingedRectWidth, float allingedRectHeight){
        Vec2 [] vertices1 = first.points;
        Vec2 [] vertices2 = second.points;
        for (byte i = 0; i < vertices2.length; i++){
            if (colisionPointWithAlignedRect(vertices2[i], new Vec2(allignedRectLeftUpperCorner.x+allingedRectWidth/2, allignedRectLeftUpperCorner.y+allingedRectHeight/2), allingedRectWidth, allingedRectHeight)) {
                return true;
            }
        }
        for (byte i = 0; i < 4; i++){
            for (byte j = 0; j < 4; j++){
                byte firstEndPointIndex =  (byte)(i+1);
                byte secondEndPointIndex = (byte)(j+1);
                if (firstEndPointIndex >= 4) firstEndPointIndex = 0;
                if (secondEndPointIndex >= 4) secondEndPointIndex = 0;
                if (isIntersectionBetweenTwoLines(vertices1[i].x, vertices1[i].y,
                        vertices1[firstEndPointIndex].x, vertices1[firstEndPointIndex].y,
                        vertices2[j].x, vertices2[j].y,
                        vertices2[secondEndPointIndex].x, vertices2[secondEndPointIndex].y)){
                    return true;
                }
            }
        }
        return false;
    }*/

    public static boolean coalisionAllignedRectWithRect(RectangularWithAngle first, RectangularWithAngle second){
        Vec2 [] vertices1 = first.points;
        Vec2 [] vertices2 = second.points;
        if(vertices2[3].x < vertices1[0].x || vertices1[3].x < vertices2[0].x) return false;
        if(vertices2[3].y < vertices1[0].y || vertices1[3].y < vertices2[0].y) return false;
        /*
        float left1 = vertices1[0].x;
        float top1 = vertices1[0].y;
        float right1 = vertices1[3].x;
        float bottom1 = vertices1[3].y;
        float left2 = vertices2[0].x;
        float top2 = vertices2[0].y;
        float right2 = vertices2[3].x;
        float bottom2 = vertices2[3].y;
        if(right2 < left1 || right1 < left2) return false;
        if(bottom2 < top1 || bottom1 < top2) return false;
         */
        return true;
    }

    public static boolean coalisionAllignedRectWithRectNotWorks(RectangularWithAngle first, RectangularWithAngle second){
        Vec2 [] vertices1 = first.points;
        Vec2 [] vertices2 = second.points;
        //PVector majorRectCenter, float smallRectCenterX, float smallRectCenterY, float majorWidth, float majorHeight, float smallRectWidth, float smallRectHeight){
        float ax = vertices1[0].x;
        float ay = vertices1[0].y;
        float ax1 = vertices1[3].x;
        float ay1 = vertices1[3].y;

        float bx = vertices2[0].x;
        float by = vertices2[0].y;
        float bx1 = vertices2[3].x;
        float by1 = vertices2[3].y;

        return(
                (
                        (
                                ( ax>=bx && ax<=bx1 )||( ax1>=bx && ax1<=bx1  )
                        ) && (
                                ( ay>=by && ay<=by1 )||( ay1>=by && ay1<=by1 )
                        )
                )||(
                        (
                                ( bx>=ax && bx<=ax1 )||( bx1>=ax && bx1<=ax1  )
                        ) && (
                                ( by>=ay && by<=ay1 )||( by1>=ay && by1<=ay1 )
                        )
                )
        )||(
                (
                        (
                                ( ax>=bx && ax<=bx1 )||( ax1>=bx && ax1<=bx1  )
                        ) && (
                                ( by>=ay && by<=ay1 )||( by1>=ay && by1<=ay1 )
                        )
                )||(
                        (
                                ( bx>=ax && bx<=ax1 )||( bx1>=ax && bx1<=ax1  )
                        ) && (
                                ( ay>=by && ay<=by1 )||( ay1>=by && ay1<=by1 )
                        )
                )
        );
    }

    public static boolean coalisionAllignedRectWithRectOld(RectangularWithAngle first, RectangularWithAngle second){
        Vec2 [] vertices1 = first.points;
        Vec2 [] vertices2 = second.points;
        System.out.println("This function is wrong");
        for (byte i = 0; i < vertices2.length; i++){
            if (colisionPointWithAlignedRect(vertices2[i].x, vertices2[i].y, first)) {
                return true;
            }
        }
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                int firstEndPointIndex =  i+1;
                int secondEndPointIndex = j+1;
                if (firstEndPointIndex >= 4) firstEndPointIndex = 0;
                if (secondEndPointIndex >= 4) secondEndPointIndex = 0;
                if (isIntersectionBetweenTwoLines(vertices1[i].x, vertices1[i].y,
                        vertices1[firstEndPointIndex].x, vertices1[firstEndPointIndex].y,
                        vertices2[j].x, vertices2[j].y,
                        vertices2[secondEndPointIndex].x, vertices2[secondEndPointIndex].y)){
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean coalisionAllignedRectWithRect(Vec2 allignedRectLeftUpperCorner, float allingedRectWidth, float allingedRectHeight, Vec2 center, float angle, float width, float height){
        RectangularWithAngle first = new RectangularWithAngle(allignedRectLeftUpperCorner, allingedRectWidth, allingedRectHeight);
        RectangularWithAngle second = new RectangularWithAngle(center, angle, width, height);
        Vec2 [] vertices1 = first.points;
        Vec2 [] vertices2 = second.points;
        for (byte i = 0; i < vertices2.length; i++){
            if (colisionPointWithAlignedRect(vertices2[i].x, vertices2[i].y, allignedRectLeftUpperCorner.x+allingedRectWidth/2, allignedRectLeftUpperCorner.y+allingedRectHeight/2, allingedRectWidth, allingedRectHeight)) {
               return true;
            }
        }
        for (byte i = 0; i < 4; i++){
            for (byte j = 0; j < 4; j++){
                byte firstEndPointIndex =  (byte)(i+1);
                byte secondEndPointIndex = (byte)(j+1);
                if (firstEndPointIndex >= 4) firstEndPointIndex = 0;
                if (secondEndPointIndex >= 4) secondEndPointIndex = 0;
                if (isIntersectionBetweenTwoLines(vertices1[i].x, vertices1[i].y,
                        vertices1[firstEndPointIndex].x, vertices1[firstEndPointIndex].y,
                        vertices2[j].x, vertices2[j].y,
                        vertices2[secondEndPointIndex].x, vertices2[secondEndPointIndex].y)){
                    return true;
                }
            }
        }
        return false;
    }
    /*
    public static boolean coalisionAllignedRectWithRect(Vec2 allingedRectCenter, float allingedRectWidth, float allingedRectHeight, Vec2 center, float angle, float width, float height){
        Rectangular first = new Rectangular(allingedRectCenter, allingedRectWidth, allingedRectHeight);
        Rectangular second = new Rectangular(center, angle, width, height);
        Vec2 [] vertices1 = first.points;
        Vec2 [] vertices2 = second.points;
        for (byte i = 0; i < vertices2.length; i++){
            if (colisionPointWithAlignedRect(vertices2[i], allingedRectCenter, allingedRectWidth, allingedRectHeight)) {
                //System.out.println("Returned true");
                return true;
            }
        }
        for (byte i = 0; i < 4; i++){
            for (byte j = 0; j < 4; j++){
                byte firstEndPointIndex =  (byte)(i+1);
                byte secondEndPointIndex = (byte)(j+1);
                if (firstEndPointIndex >= 4) firstEndPointIndex = 0;
                if (secondEndPointIndex >= 4) secondEndPointIndex = 0;
                if (isIntersectionBetweenTwoLines(vertices1[i].x, vertices1[i].y,
                                                  vertices1[firstEndPointIndex].x, vertices1[firstEndPointIndex].y,
                                                  vertices2[j].x, vertices2[j].y,
                                                  vertices2[secondEndPointIndex].x, vertices2[secondEndPointIndex].y)){
                    return true;
                }
            }
        }
        return false;

    }

    */

    public static boolean isIntersectionBetweenTwoRectsByCorners(Vec2 firstLeftUpperCorner, int firstWidth, int firstHeight, Vec2 secondLeftUpperCorner, int secondWidth, int secondHeight){
        PVector [] cornerPoints = new PVector[4];
        cornerPoints[0] = new PVector(firstLeftUpperCorner.x, firstLeftUpperCorner.y);
        cornerPoints[1] = new PVector(firstLeftUpperCorner.x+firstWidth, firstLeftUpperCorner.y);
        cornerPoints[2] = new PVector(firstLeftUpperCorner.x, firstLeftUpperCorner.y+firstHeight);
        cornerPoints[3] = new PVector(firstLeftUpperCorner.x+firstWidth, firstLeftUpperCorner.y+firstHeight);
        for (int i = 0; i < cornerPoints.length;i++){
            if (isPointInRect(cornerPoints[i], new PVector(secondLeftUpperCorner.x, secondLeftUpperCorner.y), secondWidth, secondHeight) == true) return true;
        }
        return false;
    }

    public static boolean isPointInAlignedRect(float rectCenterX, float rectCenterY, float pointX, float pointY, float rectWidth, float rectHeight){
        if (pointX>(rectCenterX-rectWidth/2f) &&
                pointX<(rectCenterX+rectWidth/2f) &&
                pointY>(rectCenterY-rectHeight/2f) &&
                pointY<(rectCenterY+rectHeight/2f)) return true;
        else return false;
    }

    public static boolean isIntersectionBetweenAllignedRects(PVector majorRectCenter, float smallRectCenterX, float smallRectCenterY, float majorWidth, float majorHeight, float smallRectWidth, float smallRectHeight){
        float ax = majorRectCenter.x-majorWidth/2f;
        float ay = majorRectCenter.y-majorHeight/2f;
        float ax1 = majorRectCenter.x+majorWidth/2f;
        float ay1 = majorRectCenter.y+majorHeight/2f;

        float bx = smallRectCenterX-smallRectWidth/2f;
        float by = smallRectCenterY-smallRectHeight/2f;
        float bx1 = smallRectCenterX+smallRectWidth/2f;
        float by1 = smallRectCenterY+smallRectHeight/2f;
        return(
                (
                        (
                                ( ax>=bx && ax<=bx1 )||( ax1>=bx && ax1<=bx1  )
                        ) && (
                                ( ay>=by && ay<=by1 )||( ay1>=by && ay1<=by1 )
                        )
                )||(
                        (
                                ( bx>=ax && bx<=ax1 )||( bx1>=ax && bx1<=ax1  )
                        ) && (
                                ( by>=ay && by<=ay1 )||( by1>=ay && by1<=ay1 )
                        )
                )
        )||(
                (
                        (
                                ( ax>=bx && ax<=bx1 )||( ax1>=bx && ax1<=bx1  )
                        ) && (
                                ( by>=ay && by<=ay1 )||( by1>=ay && by1<=ay1 )
                        )
                )||(
                        (
                                ( bx>=ax && bx<=ax1 )||( bx1>=ax && bx1<=ax1  )
                        ) && (
                                ( ay>=by && ay<=by1 )||( ay1>=by && ay1<=by1 )
                        )
                )
        );

        /*
        float cornerX = smallRectCenterX-smallRectWidth;   //Left upper
        float cornerY = smallRectCenterY-smallRectHeight;
        if (isPointInAlignedRect(majorRectCenter.x, majorRectCenter.y, cornerX, cornerY, majorWidth, majorHeight) == true) return true;
        else {
            cornerX = smallRectCenterX+smallRectWidth; //Right upper
            cornerY = smallRectCenterY-smallRectHeight;
            if (isPointInAlignedRect(majorRectCenter.x, majorRectCenter.y, cornerX, cornerY, majorWidth, majorHeight) == true) return true;
            else{
                cornerX = smallRectCenterX-smallRectWidth;   //Left lower
                cornerY = smallRectCenterY+smallRectHeight;
                if (isPointInAlignedRect(majorRectCenter.x, majorRectCenter.y, cornerX, cornerY, majorWidth, majorHeight) == true) return true;
                else{
                    cornerX = smallRectCenterX+smallRectWidth;   //right lower
                    cornerY = smallRectCenterY+smallRectHeight;
                    if (isPointInAlignedRect(majorRectCenter.x, majorRectCenter.y, cornerX, cornerY, majorWidth, majorHeight) == true) return true;
                    else{
                        float largeRectLeftX = majorRectCenter.x-majorWidth/2f;
                        float largeRectUpperY = majorRectCenter.y-majorHeight/2f;
                        float largeRectRightX = majorRectCenter.x+majorWidth/2f;
                        float largeRectLowerY = majorRectCenter.y+majorHeight/2f;
                        float smallRectLeftX = smallRectCenterX-majorWidth/2f;
                        float smallRectUpperY = smallRectCenterY-majorHeight/2f;
                        float smallRectRightX = smallRectCenterX+majorWidth/2f;
                        float smallRectLowerY = smallRectCenterY+majorHeight/2f;


                        if (smallRectUpperY > (majorRectCenter.y-smallRectHeight/2) && smallRectUpperY < (majorRectCenter.y+smallRectHeight/2)){
                            if ((smallRectCenterX < largeRectLeftX) && (smallRectCenterY>largeRectLeftX) || (smallRectCenterX < largeRectLeftX) && (smallRectCenterY>largeRectLeftX)){

                            }
                        }
                    }
                }
            }
            */
        //}
        //System.out.println("Is not visible");
        //return false;
    }

    public static boolean isIntersectionBetweenAllignedRects(float majorRectCenterX, float majorRectCenterY, float smallRectCenterX, float smallRectCenterY, float majorWidth, float majorHeight, float smallRectWidth, float smallRectHeight){
        float ax = majorRectCenterX-majorWidth/2f;
        float ay = majorRectCenterY-majorHeight/2f;
        float ax1 = majorRectCenterX+majorWidth/2f;
        float ay1 = majorRectCenterY+majorHeight/2f;

        float bx = smallRectCenterX-smallRectWidth/2f;
        float by = smallRectCenterY-smallRectHeight/2f;
        float bx1 = smallRectCenterX+smallRectWidth/2f;
        float by1 = smallRectCenterY+smallRectHeight/2f;
        return(
                (
                        (
                                ( ax>=bx && ax<=bx1 )||( ax1>=bx && ax1<=bx1  )
                        ) && (
                                ( ay>=by && ay<=by1 )||( ay1>=by && ay1<=by1 )
                        )
                )||(
                        (
                                ( bx>=ax && bx<=ax1 )||( bx1>=ax && bx1<=ax1  )
                        ) && (
                                ( by>=ay && by<=ay1 )||( by1>=ay && by1<=ay1 )
                        )
                )
        )||(
                (
                        (
                                ( ax>=bx && ax<=bx1 )||( ax1>=bx && ax1<=bx1  )
                        ) && (
                                ( by>=ay && by<=ay1 )||( by1>=ay && by1<=ay1 )
                        )
                )||(
                        (
                                ( bx>=ax && bx<=ax1 )||( bx1>=ax && bx1<=ax1  )
                        ) && (
                                ( ay>=by && ay<=by1 )||( ay1>=by && ay1<=by1 )
                        )
                )
        );
    }


    public static boolean isIntersectionBetweenTwoRectsByCorners(PVector firstLeftUpperCorner, int firstWidth, int firstHeight, PVector secondLeftUpperCorner, int secondWidth, int secondHeight){
        PVector [] cornerPoints = new PVector[4];
        cornerPoints[0] = new PVector(firstLeftUpperCorner.x, firstLeftUpperCorner.y);
        cornerPoints[1] = new PVector(firstLeftUpperCorner.x+firstWidth, firstLeftUpperCorner.y);
        cornerPoints[2] = new PVector(firstLeftUpperCorner.x, firstLeftUpperCorner.y+firstHeight);
        cornerPoints[3] = new PVector(firstLeftUpperCorner.x+firstWidth, firstLeftUpperCorner.y+firstHeight);
        for (int i = 0; i < cornerPoints.length;i++){
            if (isPointInRect(cornerPoints[i], secondLeftUpperCorner, secondWidth, secondHeight) == true) return true;
        }
        return false;
    }

    /*
    public static boolean isIntersectionBetweenTwoRects(PVector firstLeftUpperCorner, int firstWidth, int firstHeight, PVector secondLeftUpperCorner, int secondWidth, int secondHeight){
        if ((firstLeftUpperCorner.x < (secondLeftUpperCorner.x + secondWidth) &&
                secondLeftUpperCorner.x < (firstLeftUpperCorner.x + firstWidth) &&
                firstLeftUpperCorner.y < (secondLeftUpperCorner.y + secondHeight) &&
                secondLeftUpperCorner.y < (firstLeftUpperCorner.y + firstHeight))) {
            return true;
        }
        else return false;
    }

     */

    public static boolean canPersonBeSawUnderOverviewAngle(PVector playerPosition, int playerAngle, int playerRadius, int playerOverviewAngle, PVector personPosition){

        //final PVector vectorToPlayersBack = new PVector(playerRadius*Game2D.engine.cos(Game2D.engine.radians(playerAngle), playerRadius*Game2D.engine.sin(Game2D.engine.radians(playerAngle));        //if (playerBackPosition.x>0) playerBackPosition.x-
        //final PVector playerBackPosition = new PVector()
        int angleToPerson = angleDetermining(personPosition, playerPosition);
        int deltaAngle = getDeltaAngle(playerAngle, angleToPerson);
        if (deltaAngle < (playerOverviewAngle/2)) return true;
        else return false;
    }

        public static boolean isPersonInTransparentRegion(PVector playerPosition, int playerAngle, int playerOverviewAngle, PVector personPosition, int deadZoneAngle){
            int angleToPerson = angleDetermining(personPosition, playerPosition);
            int deltaAngle = getDeltaAngle(playerAngle, angleToPerson);
            if (deltaAngle > (playerOverviewAngle/2) && deltaAngle < ((playerOverviewAngle/2)+deadZoneAngle)) return true;
            else return false;
        }

        public static int getTransparentValue(PVector playerPosition, int playerAngle, int playerOverviewAngle, PVector personPosition, int deadZoneAngle){
            int angleToPerson = angleDetermining(personPosition, playerPosition);
            int deltaAngle = Program.engine.abs(getDeltaAngle(playerAngle, angleToPerson));
            if (deltaAngle > (playerOverviewAngle/2) && deltaAngle < ((playerOverviewAngle/2)+deadZoneAngle)){  // was and deltaAngle > (playerOverviewAngle/2) &&
                int transparentValue = (int) Program.engine.map(deltaAngle, (playerOverviewAngle/2), ((playerOverviewAngle/2)+deadZoneAngle), 0, 255);
                if (transparentValue > 255) transparentValue = 255;
                transparentValue = 255-transparentValue;
                if (transparentValue < 0) transparentValue = 0;
                return transparentValue;
            }
            else return 125;
        }

        public static PVector rotateVectorAroundOn180Grad(PVector startVector){
            PVector newVector = new PVector(-startVector.x, -startVector.y);
            return newVector;
        }

        public static PVector rotateVectorAroundOn90Grad(PVector startVector){
            PVector newVector = new PVector(startVector.y, -startVector.x);
            return newVector;
        }

        public static PVector rotateVectorAroundOnMinus90Grad(PVector startVector){
            PVector newVector = new PVector(-startVector.y, startVector.x);
            return newVector;
        }

        public static byte quarterDetermining(int x1, int y1, int x2, int y2){
            if (x2>x1) {
                if (y2>y1) return SOUTH_EAST_QUARTER;
                else return NORTH_EAST_QUARTER;
            }
            else {
                if (y2>y1) return SOUTH_WEST_QUARTER;
                else return NORTH_WEST_QUARTER;
            }
        }

        public static byte quarterDetermining(PVector basicPoint, PVector testPoint){
            if (testPoint.x>basicPoint.x) {
                if (testPoint.y>basicPoint.y) return SOUTH_EAST_QUARTER;
                else return NORTH_EAST_QUARTER;
            }
            else {
                if (testPoint.y>basicPoint.y) return SOUTH_WEST_QUARTER;
                else return NORTH_WEST_QUARTER;
            }
        }

        public static boolean isIntersectionBetweenTwoCircles(PVector firstCirclePosition, int radius1, PVector secondCirclePosition, int radius2){
            if (Program.engine.dist(firstCirclePosition.x, firstCirclePosition.y, secondCirclePosition.x, secondCirclePosition.y)<(radius1+radius2)) return true;
            else return false;
        }

        public static boolean isIntersectionBetweenTwoCircles(int x1, int y1, int radius1, int x2, int y2, int radius2){
            if (Program.engine.dist(x1, y1, x2, y2)<(radius1+radius2)) return true;
            else return false;
        }

        public static PVector getDeepOfIntersectionBetweenTwoCircles(PVector firstCirclePosition, int radius1, PVector secondCirclePosition, int radius2){
            float deepValue = (radius1+radius2)- Program.engine.dist(firstCirclePosition.x,firstCirclePosition.y, secondCirclePosition.x, secondCirclePosition.y);
            int angle = angleDetermining((int)firstCirclePosition.x, (int)firstCirclePosition.y, (int)secondCirclePosition.x, (int)secondCirclePosition.y);
            //println(angle);
            PVector deepVector = new PVector(deepValue*(Program.engine.cos(Program.engine.radians(angle))), deepValue*(Program.engine.sin(Program.engine.radians(angle))));
            return deepVector;
        }

        public static PVector getDeepOfIntersectionBetweenTwoCircles(PVector firstCirclePosition, PVector secondCirclePosition, int radius){
            float deepValue = radius- Program.engine.dist(firstCirclePosition.x,firstCirclePosition.y, secondCirclePosition.x, secondCirclePosition.y);
            int angle = angleDetermining((int)firstCirclePosition.x, (int)firstCirclePosition.y, (int)secondCirclePosition.x, (int)secondCirclePosition.y);
            //println(angle);
            PVector deepVector = new PVector(deepValue*(Program.engine.cos(Program.engine.radians(angle))), deepValue*(Program.engine.sin(Program.engine.radians(angle))));
            return deepVector;
        }

        public static PVector getDeepOfIntersectionBetweenTwoCircles(int x1, int y1, int radius1, int x2, int y2, int radius2){
            float deepValue = (radius1+radius2)- Program.engine.dist(x1,y1,x2,y2);
            int angle = angleDetermining(x1, y1, x2, y2);
            //println(angle);
            PVector deepVector = new PVector(deepValue*(Program.engine.cos(Program.engine.radians(angle))), deepValue*(Program.engine.sin(Program.engine.radians(angle))));
            return deepVector;
        }

        public static boolean isIntersectionBetweenCircleAndNonisometricRect(int xCircle, int yCircle, int radius, int xRectCenter, int yRectCenter, int rectWidth, int rectHeight){
            if((yCircle>(yRectCenter-rectHeight/2)) && (yCircle<(yRectCenter+rectHeight/2))){  // an derselben Hoehe
                if ((xCircle>(xRectCenter-radius-rectWidth/2)) && (xCircle<(xRectCenter+radius+rectWidth/2))){ // von linker bis rechter seite
                    return true;
                }
                else return false;
            }
            else {
                if((xCircle>(xRectCenter-rectWidth/2)) && (xCircle<(xRectCenter+rectWidth/2))){
                    if ((yCircle>(yRectCenter-radius-rectHeight/2)) && (yCircle<(yRectCenter+radius+rectHeight/2))){
                        return true;
                    }
                    else return false;
                }
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter-rectWidth/2, yRectCenter-rectHeight/2) < radius) return true;
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter+rectWidth/2, yRectCenter-rectHeight/2) < radius) return true;
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter-rectWidth/2, yRectCenter+rectHeight/2) < radius) return true;
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter+rectWidth/2, yRectCenter+rectHeight/2) < radius) return true;
                else return false;
            }
        }

        public static boolean isIntersectionBetweenCircleAndNonisometricRect(PVector circlePosition, int radius, PVector leftUpperCorner, PVector rightLowerCorner){
            if((circlePosition.y>leftUpperCorner.y) && (circlePosition.y<(rightLowerCorner.y))){
                if ((circlePosition.x>(leftUpperCorner.x-radius)) && (circlePosition.x<(rightLowerCorner.x+radius))){
                    return true;
                }
                else return false;
            }
            else {
                if((circlePosition.x>(leftUpperCorner.x)) && (circlePosition.x<(rightLowerCorner.x))){
                    if ((circlePosition.y>(leftUpperCorner.y-radius)) && (circlePosition.y<(rightLowerCorner.y+radius))){
                        return true;
                    }
                    else return false;
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, leftUpperCorner.x, leftUpperCorner.y) < radius) return true;
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, rightLowerCorner.x, leftUpperCorner.y) < radius) return true;
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, leftUpperCorner.x, rightLowerCorner.y) < radius) return true;
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, rightLowerCorner.x, rightLowerCorner.y) < radius) return true;
                else return false;
            }
        }

        public static PVector getDeepOfIntersectionBetweenCircleAndNonisometricRect(PVector circlePosition, int radius, PVector leftUpperCorner, PVector rightLowerCorner){
            PVector deepVector = new PVector(0,0);
            float deepValue;
            if((circlePosition.y>(leftUpperCorner.y)) && (circlePosition.y<(rightLowerCorner.y))){
                if ((circlePosition.x>(leftUpperCorner.x-radius)) && (circlePosition.x<(rightLowerCorner.x+radius))){
                    if (circlePosition.x < ((leftUpperCorner.x+rightLowerCorner.x)/2)) {
                        deepValue = ((leftUpperCorner.x)-circlePosition.x-radius);
                    }
                    else {
                        deepValue = ((rightLowerCorner.x)-circlePosition.x+radius);
                    }
                    deepVector = new PVector(deepValue,0);
                }
            }
            else {
                if((circlePosition.x>(leftUpperCorner.x)) && (circlePosition.x<(rightLowerCorner.x))){
                    if (circlePosition.y < ((leftUpperCorner.y+rightLowerCorner.y)/2)) {
                        deepValue = ((leftUpperCorner.y)-circlePosition.y-radius);
                    }
                    else {
                        deepValue = ((rightLowerCorner.y)-circlePosition.y+radius);
                    }
                    deepVector = new PVector(0,deepValue);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, leftUpperCorner.x, leftUpperCorner.y) < radius) {
                    deepValue = radius- Program.engine.dist(circlePosition.x, circlePosition.y, leftUpperCorner.x, leftUpperCorner.y);
                    float angle = angleDetermining(circlePosition.x, circlePosition.y, leftUpperCorner.x, leftUpperCorner.y);
                    deepVector = new PVector(deepValue* Program.engine.cos(Program.engine.radians(angle)), deepValue* Program.engine.sin(Program.engine.radians(angle)));
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, rightLowerCorner.x, leftUpperCorner.y) < radius) {
                    deepValue = radius- Program.engine.dist(circlePosition.x, circlePosition.y, rightLowerCorner.x, leftUpperCorner.y);
                    float angle = angleDetermining(circlePosition.x, circlePosition.y, rightLowerCorner.x, leftUpperCorner.y);
                    deepVector = new PVector(deepValue* Program.engine.cos(Program.engine.radians(angle)), deepValue* Program.engine.sin(Program.engine.radians(angle)));
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, leftUpperCorner.x, rightLowerCorner.y) < radius) {
                    deepValue = radius- Program.engine.dist(circlePosition.x, circlePosition.y, leftUpperCorner.x, rightLowerCorner.y);
                    float angle = angleDetermining(circlePosition.x, circlePosition.y, leftUpperCorner.x, rightLowerCorner.y);
                    deepVector = new PVector(deepValue* Program.engine.cos(Program.engine.radians(angle)), deepValue* Program.engine.sin(Program.engine.radians(angle)));
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, rightLowerCorner.x, rightLowerCorner.y) < radius) {
                    deepValue = radius- Program.engine.dist(circlePosition.x, circlePosition.y, rightLowerCorner.x, rightLowerCorner.y);
                    float angle = angleDetermining(circlePosition.x, circlePosition.y, rightLowerCorner.x, rightLowerCorner.y);
                    deepVector = new PVector(deepValue* Program.engine.cos(Program.engine.radians(angle)), deepValue* Program.engine.sin(Program.engine.radians(angle)));
                }
            }
            return deepVector;
        }

        public static PVector getDeepOfIntersectionBetweenCircleAndNonisometricRect(int xCircle, int yCircle, int radius, int xRectCenter, int yRectCenter, int rectWidth, int rectHeight){
            PVector deepVector = new PVector(0,0);
            float deepValue;
            if((yCircle>(yRectCenter-rectHeight/2)) && (yCircle<(yRectCenter+rectHeight/2))){
                if ((xCircle>(xRectCenter-radius-rectWidth/2)) && (xCircle<(xRectCenter+radius+rectWidth/2))){
                    if (xCircle < xRectCenter) {
                        deepValue = ((xRectCenter-rectWidth/2)-xCircle-radius);
                    }
                    else {
                        deepValue = ((xRectCenter+rectWidth/2)-xCircle+radius);
                    }
                    deepVector = new PVector(deepValue,0);
                }
            }
            else {
                if((xCircle>(xRectCenter-rectWidth/2)) && (xCircle<(xRectCenter+rectWidth/2))){
                    if (yCircle < yRectCenter) {
                        deepValue = ((yRectCenter-rectHeight/2)-yCircle-radius);
                    }
                    else {
                        deepValue = ((yRectCenter+rectHeight/2)-yCircle+radius);
                    }
                    deepVector = new PVector(0,deepValue);
                }
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter-rectWidth/2, yRectCenter-rectHeight/2) < radius) {
                    deepValue = radius- Program.engine.dist(xCircle, yCircle, xRectCenter-rectWidth/2, yRectCenter-rectHeight/2);
                    int angle = angleDetermining(xCircle, yCircle, xRectCenter-rectWidth/2, yRectCenter-rectHeight/2);
                    deepVector = new PVector(deepValue* Program.engine.cos(Program.engine.radians(angle)), deepValue* Program.engine.sin(Program.engine.radians(angle)));
                }
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter+rectWidth/2, yRectCenter-rectHeight/2) < radius) {
                    deepValue = radius- Program.engine.dist(xCircle, yCircle, xRectCenter+rectWidth/2, yRectCenter-rectHeight/2);
                    int angle = angleDetermining(xCircle, yCircle, xRectCenter+rectWidth/2, yRectCenter-rectHeight/2);
                    deepVector = new PVector(deepValue* Program.engine.cos(Program.engine.radians(angle)), deepValue* Program.engine.sin(Program.engine.radians(angle)));
                }
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter-rectWidth/2, yRectCenter+rectHeight/2) < radius) {
                    deepValue = radius- Program.engine.dist(xCircle, yCircle, xRectCenter-rectWidth/2, yRectCenter+rectHeight/2);
                    int angle = angleDetermining(xCircle, yCircle, xRectCenter-rectWidth/2, yRectCenter+rectHeight/2);
                    deepVector = new PVector(deepValue* Program.engine.cos(Program.engine.radians(angle)), deepValue* Program.engine.sin(Program.engine.radians(angle)));
                }
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter+rectWidth/2, yRectCenter+rectHeight/2) < radius) {
                    deepValue = radius- Program.engine.dist(xCircle, yCircle, xRectCenter+rectWidth/2, yRectCenter+rectHeight/2);
                    int angle = angleDetermining(xCircle, yCircle, xRectCenter+rectWidth/2, yRectCenter+rectHeight/2);
                    deepVector = new PVector(deepValue* Program.engine.cos(Program.engine.radians(angle)), deepValue* Program.engine.sin(Program.engine.radians(angle)));
                }
            }
            return deepVector;
        }

        public static byte getRightTriangleType(PVector triangleRightVertex, PVector triangleSecondVertex, PVector triangleThirdVertex){
            if (triangleRightVertex.y == triangleSecondVertex.y && triangleRightVertex.x == triangleThirdVertex.x) {
                if (triangleRightVertex.y < triangleThirdVertex.y) return NORTH_WEST_ORIENTED_TRIANGLE_OBSTACLE;
                else return SOUTH_EAST_ORIENTED_TRIANGLE_OBSTACLE;
            }
            else if (triangleRightVertex.y == triangleThirdVertex.y && triangleRightVertex.x == triangleSecondVertex.x) {
                if (triangleRightVertex.y < triangleSecondVertex.y) return NORTH_EAST_ORIENTED_TRIANGLE_OBSTACLE;
                else return SOUTH_WEST_ORIENTED_TRIANGLE_OBSTACLE;
            }
            else {
                System.out.println ("There are no type for this triangle!");
                return 0;
            }
        }

        public static boolean isIntersectionBetweenCircleAndTriangle(PVector circlePosition, int radius, PVector triangleRightVertex, PVector triangleSecondVertex, PVector triangleThirdVertex){ // triangle coordinates from the right corner in clockwise direction
            byte orientation = getRightTriangleType(triangleRightVertex, triangleSecondVertex, triangleThirdVertex);
            //println(orientation);
            if (orientation == NORTH_WEST_ORIENTED_TRIANGLE_OBSTACLE){
                final float ISOMETRIC_ANGLE = Program.engine.atan(Program.engine.abs(triangleThirdVertex.y-triangleRightVertex.y)/ Program.engine.abs(triangleSecondVertex.x-triangleRightVertex.x));
                final PVector COLLIDING_CIRCLE_POINT = new PVector(circlePosition.x-radius* Program.engine.sin(ISOMETRIC_ANGLE), circlePosition.y-radius* Program.engine.cos(ISOMETRIC_ANGLE));
                final float HORIZONTAL_CATHETUS = Program.engine.abs(triangleSecondVertex.x-triangleRightVertex.x);
                final float VERTICAL_CATHETUS = Program.engine.abs(triangleThirdVertex.y-triangleRightVertex.y);
                final float RELATIVE_VALUE = Program.engine.map(COLLIDING_CIRCLE_POINT.x, triangleRightVertex.x, triangleSecondVertex.x, VERTICAL_CATHETUS, HORIZONTAL_CATHETUS);

                if ((Program.engine.abs(COLLIDING_CIRCLE_POINT.x-triangleRightVertex.x)+ Program.engine.abs(COLLIDING_CIRCLE_POINT.y-triangleRightVertex.y))<RELATIVE_VALUE && ((circlePosition.x) > triangleRightVertex.x) && ((circlePosition.y) > triangleRightVertex.y) ||
                        ((circlePosition.y > triangleRightVertex.y) && (circlePosition.y < triangleThirdVertex.y) && (circlePosition.x>(triangleRightVertex.x-radius)) && (circlePosition.x <= triangleRightVertex.x)) ||
                        ((circlePosition.x > triangleRightVertex.x) && (circlePosition.x < triangleSecondVertex.x) && (circlePosition.y>(triangleRightVertex.y-radius)) && (circlePosition.y <= triangleRightVertex.y)) ||
                        (Program.engine.dist(circlePosition.x, circlePosition.y, triangleRightVertex.x, triangleRightVertex.y)<=radius) ||
                        Program.engine.dist(circlePosition.x, circlePosition.y, triangleSecondVertex.x, triangleSecondVertex.y)<=radius ||
                        Program.engine.dist(circlePosition.x, circlePosition.y, triangleThirdVertex.x, triangleThirdVertex.y)<=radius
                ){  // SOUTH-EAST
                    return true;
                }
                else return false;
            }
            //right!
            else if (orientation == NORTH_EAST_ORIENTED_TRIANGLE_OBSTACLE){    // war SOUTH_WEST_ORIENTED_TRIANGLE_OBSTACLE
                final float ISOMETRIC_ANGLE = Program.engine.atan(Program.engine.abs(triangleSecondVertex.y-triangleRightVertex.y)/ Program.engine.abs(triangleRightVertex.x-triangleThirdVertex.x));
                final PVector COLLIDING_CIRCLE_POINT = new PVector(circlePosition.x+radius* Program.engine.sin(ISOMETRIC_ANGLE), circlePosition.y-radius* Program.engine.cos(ISOMETRIC_ANGLE));
                final float HORIZONTAL_CATHETUS = Program.engine.abs(triangleRightVertex.x-triangleThirdVertex.x);
                final float VERTICAL_CATHETUS = Program.engine.abs(triangleSecondVertex.y-triangleRightVertex.y);
                final float RELATIVE_VALUE = Program.engine.map(COLLIDING_CIRCLE_POINT.x, triangleThirdVertex.x, triangleRightVertex.x, HORIZONTAL_CATHETUS, VERTICAL_CATHETUS);
                if ((Program.engine.abs(COLLIDING_CIRCLE_POINT.x-triangleRightVertex.x)+ Program.engine.abs(COLLIDING_CIRCLE_POINT.y-triangleRightVertex.y))<RELATIVE_VALUE && ((circlePosition.x) < triangleRightVertex.x) && ((circlePosition.y) > triangleRightVertex.y) ||
                        ((circlePosition.y > triangleRightVertex.y) && (circlePosition.y < triangleSecondVertex.y) && (circlePosition.x<(triangleRightVertex.x+radius)) && (circlePosition.x >= triangleRightVertex.x)) ||
                        ((circlePosition.x < triangleRightVertex.x) && (circlePosition.x > triangleThirdVertex.x) && (circlePosition.y>(triangleRightVertex.y-radius)) && (circlePosition.y <= triangleRightVertex.y)) ||
                        (Program.engine.dist(circlePosition.x, circlePosition.y, triangleRightVertex.x, triangleRightVertex.y)<=radius) ||
                        Program.engine.dist(circlePosition.x, circlePosition.y, triangleSecondVertex.x, triangleSecondVertex.y)<=radius ||
                        Program.engine.dist(circlePosition.x, circlePosition.y, triangleThirdVertex.x, triangleThirdVertex.y)<=radius
                ){  // SOUTH_WEST
                    return true;
                }
                else return false;
            }

            // Right!
            else if (orientation == SOUTH_EAST_ORIENTED_TRIANGLE_OBSTACLE){
                final float ISOMETRIC_ANGLE = Program.engine.atan(Program.engine.abs(triangleThirdVertex.y-triangleRightVertex.y)/ Program.engine.abs(triangleRightVertex.x-triangleSecondVertex.x));
                final PVector COLLIDING_CIRCLE_POINT = new PVector(circlePosition.x+radius* Program.engine.sin(ISOMETRIC_ANGLE), circlePosition.y+radius* Program.engine.cos(ISOMETRIC_ANGLE));
                final float HORIZONTAL_CATHETUS = Program.engine.abs(triangleRightVertex.x-triangleSecondVertex.x);
                final float VERTICAL_CATHETUS = Program.engine.abs(triangleRightVertex.y-triangleThirdVertex.y);
                final float RELATIVE_VALUE = Program.engine.map(COLLIDING_CIRCLE_POINT.x, triangleSecondVertex.x, triangleRightVertex.x, HORIZONTAL_CATHETUS, VERTICAL_CATHETUS);

                if ((Program.engine.abs(COLLIDING_CIRCLE_POINT.x-triangleRightVertex.x)+ Program.engine.abs(COLLIDING_CIRCLE_POINT.y-triangleRightVertex.y))<RELATIVE_VALUE && (circlePosition.x < triangleRightVertex.x) && ((circlePosition.y) < triangleRightVertex.y) ||
                        ((circlePosition.y > triangleThirdVertex.y) && (circlePosition.y < triangleRightVertex.y) && (circlePosition.x<(triangleRightVertex.x+radius)) && (circlePosition.x >= triangleRightVertex.x)) ||
                        ((circlePosition.x < triangleRightVertex.x) && (circlePosition.x > triangleSecondVertex.x) && (circlePosition.y<(triangleRightVertex.y+radius)) && (circlePosition.y >= triangleRightVertex.y)) ||
                        (Program.engine.dist(circlePosition.x, circlePosition.y, triangleRightVertex.x, triangleRightVertex.y)<=radius) ||
                        Program.engine.dist(circlePosition.x, circlePosition.y, triangleSecondVertex.x, triangleSecondVertex.y)<=radius ||
                        Program.engine.dist(circlePosition.x, circlePosition.y, triangleThirdVertex.x, triangleThirdVertex.y)<=radius
                ){  // SOUTH_EAST

                    return true;
                }
                else return false;
            }

            // Right!
            else if (orientation == SOUTH_WEST_ORIENTED_TRIANGLE_OBSTACLE){      // NEUE ORIENTATION
                final float ISOMETRIC_ANGLE = Program.engine.atan(Program.engine.abs(triangleSecondVertex.y-triangleRightVertex.y)/ Program.engine.abs(triangleRightVertex.x-triangleThirdVertex.x));
                final PVector COLLIDING_CIRCLE_POINT = new PVector(circlePosition.x-radius* Program.engine.sin(ISOMETRIC_ANGLE), circlePosition.y+radius* Program.engine.cos(ISOMETRIC_ANGLE));
                final float HORIZONTAL_CATHETUS = Program.engine.abs(triangleRightVertex.x-triangleThirdVertex.x);
                final float VERTICAL_CATHETUS = Program.engine.abs(triangleRightVertex.y-triangleSecondVertex.y);
                final float RELATIVE_VALUE = Program.engine.map(COLLIDING_CIRCLE_POINT.x, triangleThirdVertex.x, triangleRightVertex.x, HORIZONTAL_CATHETUS, VERTICAL_CATHETUS);
                if (( ( ( (COLLIDING_CIRCLE_POINT.x-triangleRightVertex.x)+(triangleRightVertex.y-COLLIDING_CIRCLE_POINT.y))<RELATIVE_VALUE)  && (circlePosition.x > triangleRightVertex.x) && (circlePosition.y < triangleRightVertex.y)) || //Kreuzung mit der Hypotenuse
                        ((circlePosition.y > triangleSecondVertex.y) && (circlePosition.y < triangleRightVertex.y) && (circlePosition.x>(triangleRightVertex.x-radius)) && (circlePosition.x <= triangleRightVertex.x)) ||  // Kreuzung mit der linken Kathete
                        ((circlePosition.x > triangleSecondVertex.x) && (circlePosition.x < triangleThirdVertex.x) && (circlePosition.y<(triangleRightVertex.y+radius)) && (circlePosition.y >= triangleRightVertex.y)) ||  // Kreuzung mit der unteren Kathete
                        (Program.engine.dist(circlePosition.x, circlePosition.y, triangleRightVertex.x, triangleRightVertex.y)<=radius) ||
                        (Program.engine.dist(circlePosition.x, circlePosition.y, triangleSecondVertex.x, triangleSecondVertex.y)<=radius) ||
                        (Program.engine.dist(circlePosition.x, circlePosition.y, triangleThirdVertex.x, triangleThirdVertex.y)<=radius)){  // SOUTH_WEST

                    //println("In the triangle to SOUTH_WEST");
                    return true;
                }
                else return false;
            }
            return false;
        }

        public static PVector getDeepOfIntersectionBetweenCircleAndTriangle(PVector circlePosition, int radius, PVector triangleRightVertex, PVector triangleSecondVertex, PVector triangleThirdVertex){ // triangle coordinates from the right corner in clockwise direction
            byte orientation = getRightTriangleType(triangleRightVertex, triangleSecondVertex, triangleThirdVertex);
            //final float ISOMETRIC_ANGLE = PI/4;
            PVector deepVector = new PVector(0,0);
            //right!
            if (orientation == SOUTH_EAST_ORIENTED_TRIANGLE_OBSTACLE){  //else hinzufuegen  und ins Ende umstellen
                final float ISOMETRIC_ANGLE = Program.engine.atan(Program.engine.abs(triangleThirdVertex.y-triangleRightVertex.y)/ Program.engine.abs(triangleRightVertex.x-triangleSecondVertex.x));
                final PVector COLLIDING_CIRCLE_POINT = new PVector(circlePosition.x+radius* Program.engine.sin(ISOMETRIC_ANGLE), circlePosition.y+radius* Program.engine.cos(ISOMETRIC_ANGLE));
                final float HORIZONTAL_CATHETUS = Program.engine.abs(triangleRightVertex.x-triangleSecondVertex.x);
                final float VERTICAL_CATHETUS = Program.engine.abs(triangleRightVertex.y-triangleThirdVertex.y);
                final float RELATIVE_VALUE = Program.engine.map(COLLIDING_CIRCLE_POINT.x, triangleSecondVertex.x, triangleRightVertex.x, HORIZONTAL_CATHETUS, VERTICAL_CATHETUS);

                if ((Program.engine.abs(COLLIDING_CIRCLE_POINT.x-triangleRightVertex.x)+ Program.engine.abs(COLLIDING_CIRCLE_POINT.y-triangleRightVertex.y))<RELATIVE_VALUE && (circlePosition.x < triangleRightVertex.x) && ((circlePosition.y) < triangleRightVertex.y)) {
                    deepVector.x = -Program.engine.abs((Program.engine.abs((triangleRightVertex.x-COLLIDING_CIRCLE_POINT.x))+ Program.engine.abs((triangleRightVertex.y-COLLIDING_CIRCLE_POINT.y)))-RELATIVE_VALUE)* Program.engine.sin(ISOMETRIC_ANGLE);
                    deepVector.y = -Program.engine.abs((Program.engine.abs((triangleRightVertex.x-COLLIDING_CIRCLE_POINT.x))+ Program.engine.abs((triangleRightVertex.y-COLLIDING_CIRCLE_POINT.y)))-RELATIVE_VALUE)* Program.engine.cos(ISOMETRIC_ANGLE);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleRightVertex.x, triangleRightVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleRightVertex, radius);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleSecondVertex.x, triangleSecondVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleSecondVertex, radius);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleThirdVertex.x, triangleThirdVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleThirdVertex, radius);
                }
                else if ((circlePosition.y < triangleRightVertex.y) && (circlePosition.y > triangleThirdVertex.y) && (circlePosition.x<(triangleRightVertex.x+radius)) && (circlePosition.x >= triangleRightVertex.x)){
                    deepVector.x = triangleRightVertex.x+radius-circlePosition.x;
                }
                else if ((circlePosition.x > triangleSecondVertex.x) && (circlePosition.x < triangleRightVertex.x) && (circlePosition.y<(triangleRightVertex.y+radius)) && (circlePosition.y >= triangleRightVertex.y)){
                    deepVector.y = (triangleRightVertex.y+radius)-circlePosition.y;
                }
            }

            if (orientation == NORTH_WEST_ORIENTED_TRIANGLE_OBSTACLE){
                // Right!
                final float ISOMETRIC_ANGLE = Program.engine.atan(Program.engine.abs(triangleThirdVertex.y-triangleRightVertex.y)/ Program.engine.abs(triangleSecondVertex.x-triangleRightVertex.x));
                final PVector COLLIDING_CIRCLE_POINT = new PVector(circlePosition.x-radius* Program.engine.sin(ISOMETRIC_ANGLE), circlePosition.y-radius* Program.engine.cos(ISOMETRIC_ANGLE));
                final float HORIZONTAL_CATHETUS = Program.engine.abs(triangleSecondVertex.x-triangleRightVertex.x);
                final float VERTICAL_CATHETUS = Program.engine.abs(triangleThirdVertex.y-triangleRightVertex.y);
                final float RELATIVE_VALUE = Program.engine.map(COLLIDING_CIRCLE_POINT.x, triangleRightVertex.x, triangleSecondVertex.x, VERTICAL_CATHETUS, HORIZONTAL_CATHETUS);
                if ((Program.engine.abs(COLLIDING_CIRCLE_POINT.x-triangleRightVertex.x)+ Program.engine.abs(COLLIDING_CIRCLE_POINT.y-triangleRightVertex.y))<RELATIVE_VALUE && ((circlePosition.x) > triangleRightVertex.x) && ((circlePosition.y) > triangleRightVertex.y)) {
                    deepVector.x = Program.engine.abs((Program.engine.abs((COLLIDING_CIRCLE_POINT.x)-triangleRightVertex.x)+ Program.engine.abs(COLLIDING_CIRCLE_POINT.y-triangleRightVertex.y))-RELATIVE_VALUE)* Program.engine.sin(ISOMETRIC_ANGLE);
                    deepVector.y = Program.engine.abs((Program.engine.abs((COLLIDING_CIRCLE_POINT.x)-triangleRightVertex.x)+ Program.engine.abs(COLLIDING_CIRCLE_POINT.y-triangleRightVertex.y))-RELATIVE_VALUE)* Program.engine.cos(ISOMETRIC_ANGLE);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleRightVertex.x, triangleRightVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleRightVertex, radius);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleSecondVertex.x, triangleSecondVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleSecondVertex, radius);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleThirdVertex.x, triangleThirdVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleThirdVertex, radius);
                }
                else if (circlePosition.y > triangleRightVertex.y && circlePosition.y < triangleThirdVertex.y && circlePosition.x>(triangleRightVertex.x-radius) && circlePosition.x < triangleRightVertex.x){
                    deepVector.x = (triangleRightVertex.x-radius)-circlePosition.x;
                }
                else if ((circlePosition.x > triangleRightVertex.x) && (circlePosition.x < triangleSecondVertex.x) && (circlePosition.y>(triangleRightVertex.y-radius)) && (circlePosition.y <= triangleRightVertex.y)){
                    deepVector.y = (triangleRightVertex.y-radius)-circlePosition.y;
                }
            }
            // Right!
            else if (orientation == NORTH_EAST_ORIENTED_TRIANGLE_OBSTACLE){
                final float ISOMETRIC_ANGLE = Program.engine.atan(Program.engine.abs(triangleSecondVertex.y-triangleRightVertex.y)/ Program.engine.abs(triangleRightVertex.x-triangleThirdVertex.x));
                final PVector COLLIDING_CIRCLE_POINT = new PVector(circlePosition.x+radius* Program.engine.sin(ISOMETRIC_ANGLE), circlePosition.y-radius* Program.engine.cos(ISOMETRIC_ANGLE));
                final float HORIZONTAL_CATHETUS = Program.engine.abs(triangleRightVertex.x-triangleThirdVertex.x);
                final float VERTICAL_CATHETUS = Program.engine.abs(triangleSecondVertex.y-triangleRightVertex.y);
                final float RELATIVE_VALUE = Program.engine.map(COLLIDING_CIRCLE_POINT.x, triangleThirdVertex.x, triangleRightVertex.x, HORIZONTAL_CATHETUS, VERTICAL_CATHETUS);
                if ((Program.engine.abs(COLLIDING_CIRCLE_POINT.x-triangleRightVertex.x)+ Program.engine.abs(COLLIDING_CIRCLE_POINT.y-triangleRightVertex.y))<RELATIVE_VALUE && ((circlePosition.x) < triangleRightVertex.x) && (circlePosition.y > triangleRightVertex.y)) {
                    deepVector.x = -Program.engine.abs((Program.engine.abs(COLLIDING_CIRCLE_POINT.x-triangleRightVertex.x)+ Program.engine.abs(COLLIDING_CIRCLE_POINT.y-triangleRightVertex.y)-RELATIVE_VALUE)* Program.engine.sin(ISOMETRIC_ANGLE));
                    deepVector.y = Program.engine.abs((Program.engine.abs(COLLIDING_CIRCLE_POINT.x-triangleRightVertex.x)+ Program.engine.abs(COLLIDING_CIRCLE_POINT.y-triangleRightVertex.y)-RELATIVE_VALUE)* Program.engine.cos(ISOMETRIC_ANGLE));
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleRightVertex.x, triangleRightVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleRightVertex, radius);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleSecondVertex.x, triangleSecondVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleSecondVertex, radius);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleThirdVertex.x, triangleThirdVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleThirdVertex, radius);
                }
                else if ((circlePosition.y > triangleRightVertex.y) && (circlePosition.y < triangleSecondVertex.y) && (circlePosition.x<(triangleRightVertex.x+radius)) && (circlePosition.x >= triangleRightVertex.x)){
                    deepVector.x = triangleRightVertex.x+radius-circlePosition.x;
                }
                else if ((circlePosition.x < triangleRightVertex.x) && (circlePosition.x > triangleThirdVertex.x) && (circlePosition.y>(triangleRightVertex.y-radius)) && (circlePosition.y <= triangleRightVertex.y)){
                    deepVector.y = (triangleRightVertex.y-radius)-circlePosition.y;
                }
            }


            else if (orientation == SOUTH_WEST_ORIENTED_TRIANGLE_OBSTACLE){
                final float ISOMETRIC_ANGLE = Program.engine.atan(Program.engine.abs(triangleSecondVertex.y-triangleRightVertex.y)/ Program.engine.abs(triangleRightVertex.x-triangleThirdVertex.x));
                final PVector COLLIDING_CIRCLE_POINT = new PVector(circlePosition.x-radius* Program.engine.sin(ISOMETRIC_ANGLE), circlePosition.y+radius* Program.engine.cos(ISOMETRIC_ANGLE));
                final float HORIZONTAL_CATHETUS = Program.engine.abs(triangleRightVertex.x-triangleThirdVertex.x);
                final float VERTICAL_CATHETUS = Program.engine.abs(triangleRightVertex.y-triangleSecondVertex.y);
                final float RELATIVE_VALUE = Program.engine.map(COLLIDING_CIRCLE_POINT.x, triangleThirdVertex.x, triangleRightVertex.x, HORIZONTAL_CATHETUS, VERTICAL_CATHETUS);
                if ((Program.engine.abs(COLLIDING_CIRCLE_POINT.x-triangleRightVertex.x)+(triangleRightVertex.y-COLLIDING_CIRCLE_POINT.y))<RELATIVE_VALUE  && (circlePosition.x > triangleRightVertex.x) && (circlePosition.y < triangleRightVertex.y)) {
                    deepVector.x = Program.engine.abs((Program.engine.abs((COLLIDING_CIRCLE_POINT.x)-triangleRightVertex.x)+ Program.engine.abs(triangleRightVertex.y-COLLIDING_CIRCLE_POINT.y))-RELATIVE_VALUE)* Program.engine.sin(ISOMETRIC_ANGLE);
                    deepVector.y = -Program.engine.abs((Program.engine.abs((COLLIDING_CIRCLE_POINT.x)-triangleRightVertex.x)+ Program.engine.abs(triangleRightVertex.y-COLLIDING_CIRCLE_POINT.y))-RELATIVE_VALUE)* Program.engine.cos(ISOMETRIC_ANGLE);

                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleRightVertex.x, triangleRightVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleRightVertex, radius);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleSecondVertex.x, triangleSecondVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleSecondVertex, radius);
                }
                else if (Program.engine.dist(circlePosition.x, circlePosition.y, triangleThirdVertex.x, triangleThirdVertex.y)<=radius){
                    deepVector = getDeepOfIntersectionBetweenTwoCircles(circlePosition, triangleThirdVertex, radius);
                }
                else if ((circlePosition.y < triangleRightVertex.y) && (circlePosition.y > triangleSecondVertex.y) && (circlePosition.x>(triangleRightVertex.x-radius)) && (circlePosition.x <= triangleRightVertex.x)){
                    deepVector.x = triangleRightVertex.x-radius-circlePosition.x;
                }
                else if ((circlePosition.x > triangleRightVertex.x) && (circlePosition.x < triangleThirdVertex.x) && (circlePosition.y<(triangleRightVertex.y+radius)) && (circlePosition.y >= triangleRightVertex.y)){
                    deepVector.y = (triangleRightVertex.y+radius-circlePosition.y);
                }
            }
            return deepVector;
        }

        public static boolean isIntersectionBetweenCircleAndIsometricSquare(int xCircle, int yCircle, int radius, int xRectCenter, int yRectCenter, int squareSide){
            final float halfDiagonal = squareSide/ Program.engine.sqrt(2);
            //final float ISOMETRIC_ANGLE = PI/4;
            if ((Program.engine.abs((xCircle-radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle-radius/ Program.engine.sqrt(2))-yRectCenter))<halfDiagonal){  // SOUTH-EAST
                return true;
            }
            else if ((Program.engine.abs((xCircle+radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle-radius/ Program.engine.sqrt(2))-yRectCenter))<halfDiagonal){  // SOUTH-WEST
                return true;
            }
            if ((Program.engine.abs((xCircle+radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle+radius/ Program.engine.sqrt(2))-yRectCenter))<halfDiagonal){  // NORTH-WEST
                return true;
            }
            if ((Program.engine.abs((xCircle-radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle+radius/ Program.engine.sqrt(2))-yRectCenter))<halfDiagonal){  // NORTH-EAST
                return true;
            }
            else {
                if (Program.engine.dist(xCircle, yCircle, xRectCenter+halfDiagonal, yRectCenter) < radius) return true;  // rechts
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter, yRectCenter+halfDiagonal) < radius) return true;  // unten
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter-halfDiagonal, yRectCenter) < radius) return true;  // links
                else if (Program.engine.dist(xCircle, yCircle, xRectCenter, yRectCenter-halfDiagonal) < radius) return true;  // oben
                else return false;
            }
        }

        public static PVector getDeepOfIntersectionBetweenCircleAndIsometricSquare(int xCircle, int yCircle, int radius, int xRectCenter, int yRectCenter, int squareSide){
            final float halfDiagonal = squareSide/ Program.engine.sqrt(2);
            final float ISOMETRIC_ANGLE = Program.engine.PI/4;
            PVector deepVector = new PVector(0,0);
            if      ((Program.engine.abs((xCircle-radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle-radius/ Program.engine.sqrt(2))-yRectCenter))<halfDiagonal){   // SOUTH-EAST
                deepVector.x = Program.engine.abs((Program.engine.abs((xCircle-radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle-radius/ Program.engine.sqrt(2))-yRectCenter))-halfDiagonal)* Program.engine.cos(ISOMETRIC_ANGLE);
                deepVector.y = Program.engine.abs((Program.engine.abs((xCircle-radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle-radius/ Program.engine.sqrt(2))-yRectCenter))-halfDiagonal)* Program.engine.sin(ISOMETRIC_ANGLE);
            }
            else if ((Program.engine.abs((xCircle+radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle-radius/ Program.engine.sqrt(2))-yRectCenter))<halfDiagonal){     // SOUTH-WEST
                deepVector.x = -1* Program.engine.abs((Program.engine.abs((xCircle+radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle-radius/ Program.engine.sqrt(2))-yRectCenter))-halfDiagonal)* Program.engine.cos(ISOMETRIC_ANGLE);
                deepVector.y = Program.engine.abs((Program.engine.abs((xCircle+radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle-radius/ Program.engine.sqrt(2))-yRectCenter))-halfDiagonal)* Program.engine.sin(ISOMETRIC_ANGLE);
            }
            else if ((Program.engine.abs((xCircle+radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle+radius/ Program.engine.sqrt(2))-yRectCenter))<halfDiagonal){     // NORTH-WEST
                deepVector.x = -1* Program.engine.abs((Program.engine.abs((xCircle+radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle+radius/ Program.engine.sqrt(2))-yRectCenter))-halfDiagonal)* Program.engine.cos(ISOMETRIC_ANGLE);
                deepVector.y = -1* Program.engine.abs((Program.engine.abs((xCircle+radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle+radius/ Program.engine.sqrt(2))-yRectCenter))-halfDiagonal)* Program.engine.sin(ISOMETRIC_ANGLE);
            }
            else if ((Program.engine.abs((xCircle-radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle+radius/ Program.engine.sqrt(2))-yRectCenter))<halfDiagonal){   // NORTH-EAST
                deepVector.x =    Program.engine.abs((Program.engine.abs((xCircle-radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle+radius/ Program.engine.sqrt(2))-yRectCenter))-halfDiagonal)* Program.engine.cos(ISOMETRIC_ANGLE);
                deepVector.y = -1* Program.engine.abs((Program.engine.abs((xCircle-radius/ Program.engine.sqrt(2))-xRectCenter)+ Program.engine.abs((yCircle+radius/ Program.engine.sqrt(2))-yRectCenter))-halfDiagonal)* Program.engine.sin(ISOMETRIC_ANGLE);
            }
            return deepVector;
        }
    }

