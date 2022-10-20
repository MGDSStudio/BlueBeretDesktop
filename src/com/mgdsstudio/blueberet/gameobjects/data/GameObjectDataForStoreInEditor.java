package com.mgdsstudio.blueberet.gameobjects.data;

import com.mgdsstudio.blueberet.gameobjects.collectableobjects.CollectableObjectInNesStyle;
import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.SimpleCollectableElement;
import com.mgdsstudio.blueberet.graphic.*;
import com.mgdsstudio.blueberet.graphic.*;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;
import processing.core.PVector;

public class GameObjectDataForStoreInEditor extends GameObjectData implements Cloneable{

    private Object object;

    // Data for round box
    private Vec2 position, secondPosition;
    private int width, height, secondWidth, secondHeight;
    private int angle = 270;
    private int life;
    private boolean withSpring;
    private BodyType bodyType;
    private boolean flip;
    private StaticSprite staticSprite;
    private SpriteAnimation spriteAnimation;
    private byte fill;
    private Flag flag, flag2;
    private byte AI_Model;
    private byte layer;

    private boolean usingRepeatability;
    private boolean transferDirection;
    private byte firstFlagDirection, secondFlagDirection;
    private byte mission = Flag.CLEAR_OBJECTS;

    //for triangle
    private Vec2 firstPoint, secondPoint, thirdPoint;


    //for platforms
    private int platformWidth, platformThickness, platformsNumber;
    private int platformVelocity;
    private boolean movementParameter;

    //For flower
    private byte behaviourModel ;

    //For collectable objects
    private byte type = CollectableObjectInNesStyle.ABSTRACT_COIN;
    private int dimension = 35;

    //for pipes
    private byte flowerBehaviour = 0;
    private PVector leftUpperCorner;
    private int diameter = 0;   //For jaws


    //For graphic
    private String pathToTexture;
    private Vec2 leftUpperGraphicCorner;
    private Vec2 rightLowerGraphicCorner;
    private int graphicWidth;
    private int graphicHeight;
    private int rowsNumber = 1;
    private int collumnsNumber = 1;
    private int animationFrequency = 66;

    //For background
    private int redValue = 0;
    private int greenValue = 0;
    private int blueValue = 0;
    private int relativeVelocity = 50;
    private int step = 0;
    private boolean withUpperLine = true;
    private boolean withLowerLine = true;
    private ImageZoneSimpleData imageZoneSimpleData;

    //For collectable
    private int localType;
    private boolean dimensionType;
    private String colorType;
    private int keyValue;
    private String currency;
    private String weaponName;
    private String dimensionStringName;
    private boolean noGravity;

    //For zones
    private byte activatedBy;
    private int deactivatedBy;
    private boolean repeateability;
    private int concentratingPointX, concentratingPointY;

    //For camera
    private boolean cameraScale;

    //Bridge data
    private int segmentsNumber;

    private SingleGraphicElement singleGraphicElement;

    public GameObjectDataForStoreInEditor() {
    }

    public GameObjectDataForStoreInEditor(String dataString) {
        this.dataString = dataString;
    }


    public int getRelativeVelocity() {
        return relativeVelocity;
    }

    public void setRelativeVelocity(int relativeVelocity) {
        this.relativeVelocity = relativeVelocity;
    }

    public int getPlatformVelocity() {
        return platformVelocity;
    }

    public void setPlatformVelocity(int platformVelocity) {
        this.platformVelocity = platformVelocity;
    }

    public byte getMovementParameter() {
        if (movementParameter) return 1;
        else return 0;
    }

    public int getRowsNumber() {
        return rowsNumber;
    }

    public void setRowsNumber(int rowsNumber) {
        this.rowsNumber = rowsNumber;
    }

    public int getCollumnsNumber() {
        return collumnsNumber;
    }

    public void setCollumnsNumber(int collumnsNumber) {
        this.collumnsNumber = collumnsNumber;
    }

    public int getAnimationFrequency() {
        return animationFrequency;
    }

    public void setAnimationFrequency(int animationFrequency) {
        this.animationFrequency = animationFrequency;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setMovementParameter(boolean movementParameter) {
        this.movementParameter = movementParameter;
    }



    public int getPlatformsNumber() {
        return platformsNumber;
    }

    public void setPlatformsNumber(int platformsNumber) {
        this.platformsNumber = platformsNumber;
    }

    public byte getFirstFlagDirection() {
        return firstFlagDirection;
    }

    public void setFirstFlagDirection(byte firstFlagDirection) {
        this.firstFlagDirection = firstFlagDirection;
    }

    public byte getSecondFlagDirection() {
        return secondFlagDirection;
    }

    public void setSecondFlagDirection(byte secondFlagDirection) {
        this.secondFlagDirection = secondFlagDirection;
    }

    public byte getLayer() {
        return layer;
    }

    public void setLayer(byte layer) {
        this.layer = layer;
    }

    //Graphic
    private int graphicLeftX, graphicUpperY, graphicRightX, graphicLowerY;
    private String pathToGraphic;
    private byte goal;


    public byte getMission() {
        return mission;
    }

    public void setMission(byte mission) {
        this.mission = mission;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public StaticSprite getStaticSprite() {
        return staticSprite;
    }

    public void setStaticSprite(StaticSprite staticSprite) {
        this.staticSprite = staticSprite;
    }



    public byte getAI_Model() {
        return AI_Model;
    }

    public void setAI_Model(byte AI_Model) {
        this.AI_Model = AI_Model;
    }







    public void generateDataString(){

    }

    public void setObject(Object object){
        this.object = object;
    }

    public void setPosition(Vec2 position){
        this.position = new Vec2(position.x, position.y);
    }

    public void setSecondPosition(Vec2 position){
        this.secondPosition = new Vec2(position.x, position.y);
    }


    public void setAngle(int angle){
        this.angle = angle;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setSecondWidth(int width){
        this.secondWidth = width;
    }

    public void setSecondHeight(int height){
        this.secondHeight = height;
    }

    public void setLife(int life){
        this.life = life;
    }

    public void setDiameter(int diameter){
        this.diameter = diameter;
    }

    public int getDiameter(){
         return diameter;
    }

    public void setBodyType(BodyType bodyType){
        this.bodyType = bodyType;
    }

    public void setWithSpring(boolean withSpring){
        this.withSpring = withSpring;
    }

    public void setPathToTexture(String pathToTexture){
        this.pathToTexture = pathToTexture;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public void setLeftUpperGraphicCorner(Vec2 leftUpperGraphicCorner) {
        this.leftUpperGraphicCorner = new Vec2(leftUpperGraphicCorner.x, leftUpperGraphicCorner.y);
        graphicLeftX = (int)leftUpperGraphicCorner.x;
        graphicUpperY = (int)leftUpperGraphicCorner.y;

    }

    public void setRightLowerGraphicCorner(Vec2 rightLowerGraphicCorner) {
        this.rightLowerGraphicCorner = new Vec2(rightLowerGraphicCorner.x, rightLowerGraphicCorner.y);
        graphicRightX = (int)rightLowerGraphicCorner.x;
        graphicLowerY = (int)rightLowerGraphicCorner.y;
    }

    public void setGraphicWidth(int graphicWidth) {
        this.graphicWidth = graphicWidth;
    }



    public void setGraphicHeight(int graphicHeight) {
        this.graphicHeight = graphicHeight;
    }



    public String getClassName() {
        return className;
    }

    @Override
    public void createDataString() {
        System.out.println("Function is not implemented");
    }

    public String getDataString() {
        if (dataString.length()<1){
            System.out.println("Data was not generated yet. Use generate before get");
        }
        return dataString;
    }

    public Vec2 getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getAngle() {
        return angle;
    }

    public int getLife() {
        return life;
    }

    public boolean isWithSpring() {
        return withSpring;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public String getPathToTexture() {
        return pathToTexture;
    }

    public Vec2 getLeftUpperGraphicCorner() {
        return leftUpperGraphicCorner;
    }

    public Vec2 getRightLowerGraphicCorner() {
        return rightLowerGraphicCorner;
    }

    public int getGraphicWidth() {
        return graphicWidth;
    }

    public int getGraphicHeight() {
        return graphicHeight;
    }

    public byte isFill() {
        return fill;
    }

    public void setFill(boolean fill){
        if (fill) this.fill = (byte)1;
        else this.fill = (byte)0;
    }
    public void setFill(byte fill) {
        this.fill = fill;
    }

    public byte getFill(){
        return fill;
    }

    public void setStaticTextureData(TextureDataToStore data) {
        pathToGraphic = data.getPath();
        graphicLeftX = data.getGraphicLeftX();
        graphicUpperY = data.getGraphicLeftY();
        graphicRightX = data.getGraphicRightX();
        graphicLowerY = data.getGraphicRightY();
    }

    public void setStaticSpriteByTextureData(TextureDataToStore data){
        pathToGraphic = data.getPath();
        graphicLeftX = data.getGraphicLeftX();
        graphicUpperY = data.getGraphicLeftY();
        graphicRightX = data.getGraphicRightX();
        graphicLowerY = data.getGraphicRightY();
        boolean fill = false;
        if (this.fill != 0) fill = true;
        System.out.println("Sprite created width dimensions: " + width + "x" + height + " and fill " + fill);
        staticSprite = new StaticSprite(pathToGraphic, graphicLeftX, graphicUpperY, graphicRightX, graphicLowerY, width, height, fill);
    }

    public void setSpriteAnimationByTextureData(AnimationDataToStore data){
        pathToGraphic = data.getPath();
        graphicLeftX = data.getLeftUpperCorner()[0];
        graphicUpperY = data.getLeftUpperCorner()[1];
        graphicRightX = data.getRightLowerCorner()[0];
        graphicLowerY = data.getRightLowerCorner()[1];
        width = data.getGraphicWidth();
        height = data.getGraphicHeight();
        /*rowsNumber = data.getCollumnsNumber();
        collumnsNumber = data.getRowsNumber();
        */
        rowsNumber = data.getCollumnsNumber();
        collumnsNumber = data.getRowsNumber();
        /*
        rowsNumber = data.getRowsNumber();
        collumnsNumber = data.getCollumnsNumber();
         */


        animationFrequency = data.getFrequency();
        boolean fill = false;
        if (this.fill != 0) fill = true;
        System.out.println("Animation created width dimensions: " + width + "x" + height + " and fill " + fill + " and path: " + pathToGraphic + " and rows: " + rowsNumber + " and collumns: " + collumnsNumber);
        spriteAnimation = new SpriteAnimation (pathToGraphic, graphicLeftX, graphicUpperY, graphicRightX,graphicLowerY, width, height, (byte)rowsNumber, (byte)collumnsNumber, animationFrequency);
    }

    public int getGraphicLeftX() {
        return graphicLeftX;
    }

    public int getGraphicUpperY() {
        return graphicUpperY;
    }

    public int getGraphicRightX() {
        return graphicRightX;
    }

    public int getGraphicLowerY() {
        return graphicLowerY;
    }

    public String getPathToGraphic() {
        if (pathToGraphic != null) return pathToGraphic;
        else return pathToTexture;
    }

    public void calculateGraphicDimentionsForRoundBox() {
        graphicWidth = (int)PApplet.ceil(width);
        graphicHeight = (int)PApplet.ceil(height);
        if (graphicWidth == 50 || graphicHeight == 50){
            if (graphicWidth == 50){
                graphicWidth = 52;
            }
            else graphicHeight = 52;
        }
        else {
            if (graphicWidth > 50) {
                graphicWidth = (int) (width + width / 50f);
            }
            if (graphicHeight > 50){
                graphicHeight = (int) (height + height / 50f);
            }
        }
    }

    public void calculateGraphicDimentionsForMoveablePlatform() {
        graphicWidth = (int)(platformWidth);
        graphicHeight = (int)(platformThickness);
        System.out.println("Dime of pl: " + graphicWidth + "x " + graphicHeight);

    }

    public void setGoal(byte goal) {
        this.goal = goal;
    }

    public byte getGoal(){
        return goal;
    }

    public Flag getFlag() {
        if (flag == null){
            flag = new Flag(new PVector(position.x, position.y), width, height, mission);
        }
        return flag;
    }

    public void clear(){
        className = "No_data";
    }
    /*public void setFlag(Flag flag){
        this.flag = flag;
    }*/

    public Object clone(){
        try {
            return super.clone();
        }
        catch (Exception e){
            System.out.println("Can not clone object " + e);
            return this;
        }
    }

    public Flag getFirstFlag() {
        flag = new Flag(new PVector(position.x, position.y), width, height, Flag.PORTAL_ENTER_ZONE, firstFlagDirection);
        return flag;
    }

    public Flag getSecondFlag(){
        if (secondPosition == null) System.out.println("secondPosition = null");
        flag2 = new Flag(new PVector(secondPosition.x, secondPosition.y), secondWidth, secondHeight, Flag.PORTAL_EXIT_ZONE, secondFlagDirection);
        return flag2;
    }

    public byte getActivatedBy() {
        return activatedBy;
    }

    public void setActivatedBy(byte activatedBy){
        this.activatedBy = activatedBy;
    }

    public void setUsingRepeatability(boolean flag){
        usingRepeatability = flag;
    }

    public boolean getUsingRepeateability() {
        return usingRepeatability;
    }

    public void setTransferDirection(boolean flag){
        transferDirection = flag;
    }

    public boolean getTransferDirection() {
        return transferDirection;
    }

    public void setPlatformWidth(int platformWidth) {
        this.platformWidth = platformWidth;
    }

    public void setPlatformThickness(int platformThickness) {
        this.platformThickness = platformThickness;
    }

    public int getPlatformWidth() {
        return this.platformWidth;
    }

    public int getPlatformThickness() {
        return this.platformThickness;
    }

    /*public void setFlowerBehaviourModel(byte behaviourModel) {
        this.behaviourModel = behaviourModel;
    }*/

    public byte getFlowerBehaviour() {
        return flowerBehaviour;
    }

    public void setFlowerBehaviour(byte flowerBehaviour) {
        this.flowerBehaviour = flowerBehaviour;
    }

    public PVector getLeftUpperCorner() {
        return leftUpperCorner;
    }

    public void setLeftUpperCorner(PVector leftUpperCorner) {
        this.leftUpperCorner = leftUpperCorner;
    }

    public int getRedValue() {
        return redValue;
    }

    public void setRedValue(int redValue) {
        this.redValue = redValue;
    }

    public int getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(int greenValue) {
        this.greenValue = greenValue;
    }

    public int getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(int blueValue) {
        this.blueValue = blueValue;
    }

    public void setStep(int value) {
        this.step = value;
    }

    public int getStep(){
        return step;
    }

    public SingleGraphicElement getGraphicElement() {
        return singleGraphicElement;
    }

    public SingleGraphicElement getSingleGraphicElement() {
        return singleGraphicElement;
    }

    public boolean getDimensionType() {
        return dimensionType;
    }

    public void setDimensionType(boolean dimensionType) {
        this.dimensionType = dimensionType;
    }

    public String getColorOrMaterialType() {
        return colorType;
    }

    public void setColorOrMaterialType(String colorType) {
        this.colorType = colorType;
    }


    public int getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(int keyValue) {
        this.keyValue = keyValue;
    }

    public int getLocalType() {
        return localType;
    }

    public void setLocalType(int localType) {
        this.localType = localType;
    }

    public int getFixationType() {
        if (bodyType == BodyType.STATIC){
            return SimpleCollectableElement.STATIC_BODY;
        }
        else if (bodyType == BodyType.DYNAMIC && withSpring){
            return SimpleCollectableElement.BODY_WITH_SPRING;
        }
        else if (bodyType == BodyType.DYNAMIC){
            // It works!
            if (noGravity) return SimpleCollectableElement.DYNAMIC_BODY_WITH_0_GRAVITY;
            else return SimpleCollectableElement.DYNAMIC_BODY_WITH_NORMAL_GRAVITY;
        }

        else {
            System.out.println("This fixation type is not known");
            return SimpleCollectableElement.DYNAMIC_BODY_WITH_0_GRAVITY;
        }
    }

    /*
    public void updateLocalTypeByColorAndDimension() {
        if (colorType == AddingNewCollectableObjectAction.BLUE_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_BLUE_STONE;
            else localType = AbstractCollectable.SMALL_BLUE_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.RED_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_RED_STONE;
            else localType = AbstractCollectable.SMALL_RED_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.GREEN_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_GREEN_STONE;
            else localType = AbstractCollectable.SMALL_GREEN_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.WHITE_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_WHITE_STONE;
            else localType = AbstractCollectable.SMALL_WHITE_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.YELLOW_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_YELLOW_STONE;
            else localType = AbstractCollectable.SMALL_YELLOW_STONE;
        }
    }

    public void updateLocalTypeByCurrency() {
        if (colorType == AddingNewCollectableObjectAction.BLUE_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_BLUE_STONE;
            else localType = AbstractCollectable.SMALL_BLUE_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.RED_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_RED_STONE;
            else localType = AbstractCollectable.SMALL_RED_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.GREEN_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_GREEN_STONE;
            else localType = AbstractCollectable.SMALL_GREEN_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.WHITE_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_WHITE_STONE;
            else localType = AbstractCollectable.SMALL_WHITE_STONE;
        }
        else if (colorType == AddingNewCollectableObjectAction.YELLOW_GEM){
            if (dimensionType == AddingNewCollectableObjectAction.BIG_DIMENSION) localType = AbstractCollectable.BIG_YELLOW_STONE;
            else localType = AbstractCollectable.SMALL_YELLOW_STONE;
        }
    }*/


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;
    }

    public String getDimensionStringName() {
        return dimensionStringName;
    }

    public void setDimensionStringName(String dimensionStringName) {
        this.dimensionStringName = dimensionStringName;
    }

    public boolean isWithUpperLine() {
        return withUpperLine;
    }

    public void setWithUpperLine(boolean withUpperLine) {
        this.withUpperLine = withUpperLine;
    }

    public boolean isWithLowerLine() {
        return withLowerLine;
    }

    public void setWithLowerLine(boolean withLowerLine) {
        this.withLowerLine = withLowerLine;
    }

    public ImageZoneSimpleData getImageZoneSimpleData() {
        return imageZoneSimpleData;
    }

    public void setImageZoneSimpleData(ImageZoneSimpleData imageZoneSimpleData) {
        this.imageZoneSimpleData = imageZoneSimpleData;
    }

    public boolean isNoGravity() {
        return noGravity;
    }

    public void setNoGravity(boolean noGravity) {
        this.noGravity = noGravity;
    }

    public int getSegmentsNumber() {
        return segmentsNumber;
    }

    public void setSegmentsNumber(int segmentsNumber) {
        this.segmentsNumber = segmentsNumber;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public SpriteAnimation getSpriteAnimation() {
        if (spriteAnimation == null) System.out.println("Sprite animation is null");
        return spriteAnimation;
    }

    /*
    public void setSpriteAnimation(SpriteAnimation spriteAnimation) {
        this.spriteAnimation = spriteAnimation;
    }*/

    public Vec2 getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(Vec2 firstPoint) {
        this.firstPoint = firstPoint;
    }

    public Vec2 getSecondPoint() {
        return secondPoint;
    }

    public void setSecondPoint(Vec2 secondPoint) {
        this.secondPoint = secondPoint;
    }

    public Vec2 getThirdPoint() {
        return thirdPoint;
    }

    public void setThirdPoint(Vec2 thirdPoint) {
        this.thirdPoint = thirdPoint;
    }

    public int getDeactivatedBy() {
        return deactivatedBy;
    }

    public void setDeactivatedBy(int deactivatedBy) {
        this.deactivatedBy = deactivatedBy;
    }

    public boolean isCameraScale() {
        return cameraScale;
    }

    public void setCameraScale(boolean cameraScale) {
        this.cameraScale = cameraScale;
    }


    public boolean getRepeateability() {
        return repeateability;
    }

    public void setRepeateability(boolean repeateability) {
        this.repeateability = repeateability;
    }

    public int getConcentratingPointX() {
        return concentratingPointX;
    }

    public void setConcentratingPointX(int concentratingPointX) {
        this.concentratingPointX = concentratingPointX;
    }

    public int getConcentratingPointY() {
        return concentratingPointY;
    }

    public void setConcentratingPointY(int concentratingPointY) {
        this.concentratingPointY = concentratingPointY;
    }
}
