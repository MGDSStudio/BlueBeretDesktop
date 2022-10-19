package com.mgdsstudio.blueberet.graphic;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.graphic.controllers.PersonAnimationController;
import com.mgdsstudio.blueberet.graphic.simplegraphic.Image;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.Program;
import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.data.IntList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import processing.core.

public class EnemiesAnimationController extends PersonAnimationController {
    // I can pack all enemies sprites in a same tileset and make hitted tileset as static
    private HashMap <SpriteAnimation, Integer> animations;
    private HashMap <StaticSprite, Integer> sprites;
    private ArrayList<AnimationData> animationData;
    private final boolean withWhiteHittedSprite = true;
    private Tileset mainTileset, hittedTileset;
    private final ArrayList <Tileset> hittedTilesets = new ArrayList<>(3);
    private int actualHittedTilesetNumber = 0;
    private Timer whiteColorTimer, subColorTimer;
    private final static int COLOR_CHANGING_STAGES = 10;
    private final static int WHITE_COLOR_TIME = 500;
    private boolean dead;
    private int DEAD_SPRITE = 36;
    public int ANIMATION_TYPE_FOR_DEAD_SPRITE = GO;
    private int actualAnimationType = GO;
    private int lastDrawnAnimationType = GO;
    private boolean useManyColoredHittedSpritesheet = true;


    private ArrayList <SpriteAnimation> spriteAnimationsToBeReturned;   // for memory

    public EnemiesAnimationController(){
        animationData = new ArrayList<>();
        animations = new HashMap<SpriteAnimation, Integer>();
        sprites = new HashMap<StaticSprite, Integer>();
        spriteAnimationsToBeReturned = new ArrayList<>(2);
    }



    public void addNewAnimationData(AnimationData animationData){
        this.animationData.add(animationData);
    }

    public void setDataForSprite(){

    }

    public void addNewAnimation(SpriteAnimation spriteAnimation, int animationType){
        animations.put(spriteAnimation, animationType);
    }

    public void addNewSprite(StaticSprite staticSprite, int spriteType){
        sprites.put(staticSprite, spriteType);
    }

    public void updateAnimationData(byte newStatement){
        if (newStatement == AnimationData.GO){
            for (Map.Entry animation :  animations.entrySet()) {
                if ((int)animation.getValue() == ANIMATION_TYPE_FOR_DEAD_SPRITE){
                    for (AnimationData data : animationData) {
                        if (data.getType() == newStatement) {
                            SpriteAnimation spriteAnimation = (SpriteAnimation) animation.getKey();
                            spriteAnimation.setStartSprite(data.getStartSprite());
                            spriteAnimation.setLastSprite(data.getEndSprite());
                            spriteAnimation.setUpdateFrequency(data.getSpriteFrequency());
                        }
                    }
                }
            }
            resetAnimation(GO);
        }
        if (newStatement == AnimationData.FLY){
            for (Map.Entry animation :  animations.entrySet()) {
                if ((int)animation.getValue() == ANIMATION_TYPE_FOR_DEAD_SPRITE){
                    for (AnimationData data : animationData) {
                        if (data.getType() == newStatement) {
                            SpriteAnimation spriteAnimation = (SpriteAnimation) animation.getKey();
                            spriteAnimation.setStartSprite(data.getStartSprite());
                            spriteAnimation.setLastSprite(data.getEndSprite());
                            spriteAnimation.setUpdateFrequency(data.getSpriteFrequency());
                        }
                    }
                }
            }
            resetAnimation(GO);
        }
    }

    private void drawSingleSpriteForCorpse(GameCamera gameCamera, int spriteType, Vec2 pos, float angle, boolean flip){
        if (spriteType == ANIMATION_TYPE_FOR_DEAD_SPRITE){
            if (animations.containsValue(ANIMATION_TYPE_FOR_DEAD_SPRITE)){
                for (Map.Entry animation :  animations.entrySet()) {
                    if ((int)animation.getValue() == ANIMATION_TYPE_FOR_DEAD_SPRITE){
                        SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                        if (withWhiteHittedSprite) selectTileset(spriteAnimation);
                        spriteAnimation.drawSingleSprite(gameCamera, pos, PApplet.degrees(angle), flip, DEAD_SPRITE);
                    }
                }
            }
        }
        else {
            System.out.println("There are no dead animation");
        }
    }

    public void drawDeadSprite(GameCamera gameCamera, Vec2 pos, float angle, boolean flip){
        drawSingleSpriteForCorpse(gameCamera, ANIMATION_TYPE_FOR_DEAD_SPRITE, pos, angle, flip);
        lastDrawnAnimationType = ANIMATION_TYPE_FOR_DEAD_SPRITE;
    }

    public void setTint(int tint){
        for (Map.Entry animation :  animations.entrySet()) {
            if ((int)animation.getValue() == actualAnimationType){
                SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                spriteAnimation.setTint(tint);
            }
        }
    }

    public void drawAnimation(GameCamera gameCamera, int type, Vec2 pos, float angle, boolean flip){
        if (animations.containsValue(type)){
            for (Map.Entry animation :  animations.entrySet()) {
                if ((int)animation.getValue() == type){
                    SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                    if (withWhiteHittedSprite) selectTileset(spriteAnimation );
                    spriteAnimation.draw(gameCamera, pos, PApplet.degrees(angle), flip);
                    spriteAnimation.update();
                    lastDrawnAnimationType = type;
                    break;
                }
            }
        }
        else System.out.println("There are no animation of this type");
    }

    public void drawAnimation(GameCamera gameCamera, int type, Vec2 pos, float angleInRadians, boolean flipX, boolean flipY){
        if (animations.containsValue(type)){
            for (Map.Entry animation :  animations.entrySet()) {
                if ((int)animation.getValue() == type){
                    SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                    if (withWhiteHittedSprite)  selectTileset(spriteAnimation);
                    spriteAnimation.draw(gameCamera, pos, PApplet.degrees(angleInRadians), flipX, flipY);
                    spriteAnimation.update();
                    lastDrawnAnimationType = type;
                    break;
                }
            }
        }
        else System.out.println("There are no animation of this type");
    }

    public void drawAnimation(GameCamera gameCamera, int type, Vec2 pos, float angle){
        if (animations.containsValue(type)){
            for (Map.Entry animation :  animations.entrySet()) {
                if ((int)animation.getValue() == type){
                    SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                    if (withWhiteHittedSprite)  selectTileset(spriteAnimation);
                    spriteAnimation.draw(gameCamera, pos, PApplet.degrees(angle), graphicFlip);
                    spriteAnimation.update();
                    lastDrawnAnimationType = type;
                }
            }
        }
        else System.out.println("There are no animation of this type");
    }

    public void drawSingleSpriteFromAnimation(GameCamera gameCamera, int type, Vec2 pos, float angle, boolean flip, int spriteNumber){
        if (animations.containsValue(type)){
            for (Map.Entry animation :  animations.entrySet()) {
                if ((int)animation.getValue() == type){
                    SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                    if (withWhiteHittedSprite) selectTileset(spriteAnimation);
                    spriteAnimation.drawSingleSprite(gameCamera, pos, PApplet.degrees(angle), flip, spriteNumber);
                    lastDrawnAnimationType = type;
                    break;
                }
            }
        }
        else System.out.println("There are no animation of this type");
    }

    public void drawSprite(GameCamera gameCamera, int type, Vec2 pos, float angle, boolean flip){
        if (sprites.containsValue(type)){
            for (Map.Entry sprite :  sprites.entrySet()) {
                if ((int)sprite.getValue() == type){
                    StaticSprite singleSprite = (StaticSprite) sprite.getKey();
                    if (withWhiteHittedSprite) selectTileset(singleSprite);
                    singleSprite.draw(gameCamera, pos, angle, flip);
                    break;
                    //lastDrawnAnimationType = type;
                }
            }
        }
        else System.out.println("There are no sprite of this type");
    }

    public void drawSprite(GameCamera gameCamera, int type, Vec2 pos, float angle, boolean flipY, boolean flipX){
        if (sprites.containsValue(type)){
            for (Map.Entry sprite :  sprites.entrySet()) {
                if ((int)sprite.getValue() == type){
                    StaticSprite singleSprite = (StaticSprite) sprite.getKey();
                    if (withWhiteHittedSprite) selectTileset(singleSprite);
                    singleSprite.draw(gameCamera, pos, angle, flipY, flipX);

                    break;
                    //lastDrawnAnimationType = type;
                }
            }
        }
        else System.out.println("There are no sprite of this type");
    }


    public SpriteAnimation getSpriteAnimation(int type){
        SpriteAnimation spriteAnimation;
        for (Map.Entry animation :  animations.entrySet()) {
            if ((int)animation.getValue() == type){
                spriteAnimation = (SpriteAnimation)animation.getKey();
                return spriteAnimation;
            }
        }
        return null;
    }

    public void loadSprites(MainGraphicController mainGraphicController) {
        for (Map.Entry animation :  animations.entrySet()) {
            SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
            spriteAnimation.loadAnimation(mainGraphicController.getTilesetUnderPath(spriteAnimation.getPath()));
            if (withWhiteHittedSprite){
                if (mainTileset != null) {
                    mainTileset = spriteAnimation.getTileset();
                    if (mainTileset != null){
                        createHittedTileset();
                    }
                }
            }
        }
    }

    private void createHittedTileset() {
        if (mainTileset == null){
            for (Map.Entry animation :  animations.entrySet()) {
                SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                mainTileset = spriteAnimation.getTileset();
                break;
            }
        }
        if (mainTileset!= null && hittedTileset == null) {
            if (useManyColoredHittedSpritesheet) createHittedTilesetArray();
            else createSingleHittedTileset();
        }
        else {
            System.out.println("Main " + (mainTileset == null) + "; hit: " + (hittedTileset == null));
        }
    }



    private void createHittedTilesetArray(){
        if (mainTileset == null){
            for (Map.Entry animation :  animations.entrySet()) {
                SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                mainTileset = spriteAnimation.getTileset();
                break;
            }
        }
        String path = mainTileset.getPath();
        for (Tileset tileset : hittedTilesets){
            if (tileset.getPath() == path || tileset.getPath().equals(path)){
                System.out.println("This tileset under path "+ path +" already exists and the link was got");
                hittedTileset = tileset;
            }
        }
        if (hittedTilesets.size() == 0) {
            Tileset blueTileset = (Tileset) mainTileset.clone();
            Tileset redTileset = (Tileset) mainTileset.clone();
            Tileset yellowTileset = (Tileset) mainTileset.clone();
            //makeSpriteWhite(whiteTileset);
            float coef = 2.0f;
            float neutralCoef = 0.55f;
            blueTileset.makeSpriteWithSpecificTint(neutralCoef,neutralCoef,coef);
            redTileset.makeSpriteWithSpecificTint(coef,neutralCoef,neutralCoef);
            yellowTileset.makeSpriteWithSpecificTint(coef,coef,coef);
            hittedTilesets.add(blueTileset);
            hittedTilesets.add(redTileset);
            hittedTilesets.add(yellowTileset);
            System.out.println("*** Every enemy must have one single tilesets list  "+ path +"");
        }
    }

    private void createSingleHittedTileset(){
        String path = mainTileset.getPath();
        for (Tileset tileset : hittedTilesets){
            if (tileset.getPath() == path || tileset.getPath().equals(path)){
                System.out.println("This tileset under path "+ path +" already exists and the link was got");
                hittedTileset = tileset;
            }
        }
        if (hittedTileset == null) {
            hittedTileset = (Tileset) mainTileset.clone();
            makeSpriteWhite(hittedTileset);
            makeBlackConture(hittedTileset);
            hittedTilesets.add(hittedTileset);
            System.out.println("New tileset must be created for path  "+ path +"");
        }
    }

    /*
    private void makeSpriteWithSpecificTint(Tileset hittedTileset, int red, int green, int blue){
        boolean pixelsArrayIsNull = false;
        int color;
        int alpha;
        PImage source = mainTileset.getPicture().getImage();
        PImage hittedImage = Program.engine.createImage(source.width, source.height, PConstants.ARGB);
        Image image = new Image(hittedTileset.getPath(), hittedImage);
        hittedTileset.picture = image;
        int arrayLength = hittedImage.width*hittedImage.height;
        hittedImage.loadPixels();
        hittedTileset.picture.image.loadPixels();
        source.loadPixels();
        if (source.pixels == null) {
            pixelsArrayIsNull = true;
        }
        else {
            for (int i = 0; i < arrayLength; i++) {
                color = source.pixels[i];
                alpha = (color >> 24) & 0xFF;
                if (alpha > 125) {
                    hittedImage.loadPixels();
                    hittedImage.pixels[i] = Program.engine.color(255);
                    hittedImage.updatePixels();
                } else {
                    hittedImage.pixels[i] = Program.engine.color(0, 0);
                }
            }
        }
        hittedImage.updatePixels();
        source.updatePixels();
        if (pixelsArrayIsNull){
            this.hittedTileset = null;
            System.out.println("Can not create white tileset. Sources pixels array is null");
        }
    }
*/
    private void makeSpriteWhite(Tileset hittedTileset){
        boolean pixelsArrayIsNull = false;
        int color;
        int alpha;
        PImage source = mainTileset.getPicture().getImage();
        PImage hittedImage = Program.engine.createImage(source.width, source.height, PConstants.ARGB);
        Image image = new Image(hittedTileset.getPath(), hittedImage);
        hittedTileset.picture = image;
        int arrayLength = hittedImage.width*hittedImage.height;
        hittedImage.loadPixels();
        hittedTileset.picture.image.loadPixels();
        source.loadPixels();
        if (source.pixels == null) {
            pixelsArrayIsNull = true;
        }
        else {
            for (int i = 0; i < arrayLength; i++) {
                color = source.pixels[i];
                alpha = (color >> 24) & 0xFF;
                if (alpha > 125) {
                    hittedImage.loadPixels();
                    hittedImage.pixels[i] = Program.engine.color(255);
                    hittedImage.updatePixels();
                } else {
                    hittedImage.pixels[i] = Program.engine.color(0, 0);
                }
            }
        }
        hittedImage.updatePixels();
        source.updatePixels();
        if (pixelsArrayIsNull){
            this.hittedTileset = null;
            System.out.println("Can not create white tileset. Sources pixels array is null");
        }
    }

    private void makeBlackConture(Tileset hittedTileset){
        hittedTileset.picture.image.loadPixels();
        int arrayLength = hittedTileset.picture.image.width*hittedTileset.picture.image.height;
        IntList pixelsToBeBlack = new IntList();
        int coloredPixels = 0;
        int color;
        int alpha;
        for (int i = 0; i < arrayLength; i++){
            color = hittedTileset.picture.image.pixels[i];
            alpha = (color >> 24) & 0xFF;
            if (alpha < 5){
                //Clear pixel
            }
            else {
                coloredPixels++;
                int [] nearestPixelsPos = getNearestPixels(i, hittedTileset.picture.image.width, hittedTileset.picture.image.height);
                //printPixelData();
                boolean somePixelsAreAlpha = false;
                for (int j = 0; j < nearestPixelsPos.length; j++){
                    if (nearestPixelsPos[j] >= 0){
                        int nearestPixelColor = hittedTileset.picture.image.pixels[nearestPixelsPos[j]];

                        int nearestAlpha  = (nearestPixelColor >> 24) & 0xFF;
                        if (nearestAlpha < 125){
                            somePixelsAreAlpha = true;
                            break;
                            //Clear pixel
                        }

                    }
                }
                if ( somePixelsAreAlpha ) {
                    // System.out.println("Pixel: " + convertPixelNumberToCoordinate(i,  hittedTileset.picture.image.width, true) +"x" + convertPixelNumberToCoordinate(i,  hittedTileset.picture.image.width, false));
                    pixelsToBeBlack.append(i);
                }
            }
        }
        //System.out.println("Colored pixels: " + coloredPixels +". Black pixels " + pixelsToBeBlack.size() + " all pixels " + hittedImage.pixels.length);
        for (int i = 0; i < pixelsToBeBlack.size(); i++){
            hittedTileset.picture.image.pixels[pixelsToBeBlack.get(i)] = Program.engine.color(0);
        }
        hittedTileset.picture.image.updatePixels();
    }

    private int [] getNearestPixels(int pixelAbsNumber, int alongX, int alongY){
        int [] nearest = new int[8];
        int nullPixelValue = -1;
        for (int i = 0; i < nearest.length; i++){
            nearest[i] = nullPixelValue;
        }
        int x = (pixelAbsNumber%alongX);
        int y = PApplet.floor(pixelAbsNumber/alongX);
        //Upper Line
        nearest[0] = convertPixelCoordinateToNumber(x - 1, y - 1, alongX);
        nearest[1] = convertPixelCoordinateToNumber(x, y - 1, alongX);
        nearest[2] = convertPixelCoordinateToNumber(x + 1, y - 1, alongX);

        //Central Line
        nearest[3] = convertPixelCoordinateToNumber(x-1, y, alongX);
        //hier atcual pixel is
        nearest[4] = convertPixelCoordinateToNumber(x+1, y-1, alongX);
        //Lower Line

        nearest[5] = convertPixelCoordinateToNumber(x-1, y+1, alongX);
        nearest[6] = convertPixelCoordinateToNumber(x, y+1, alongX);
        nearest[7] = convertPixelCoordinateToNumber(x+1, y+1, alongX);
        if (x == 0){
            nearest[0] = nullPixelValue;
            nearest[3] = nullPixelValue;
            nearest[5] = nullPixelValue;
        }
        else if (x == (alongX-1)){
            nearest[2] = nullPixelValue;
            nearest[4] = nullPixelValue;
            nearest[7] = nullPixelValue;
        }
        if (y == 0){
            nearest[0] = nullPixelValue;
            nearest[1] = nullPixelValue;
            nearest[2] = nullPixelValue;
        }
        else if (y == (alongY-1)){
            nearest[5] = nullPixelValue;
            nearest[6] = nullPixelValue;
            nearest[7] = nullPixelValue;
        }

        return nearest;
    }

    private int convertPixelNumberToCoordinate(int number, int alongX, boolean needX){
        if (needX){
            return number%alongX;
        }
        else {
            return PApplet.floor(number/alongX);
        }
    }

    private int convertPixelCoordinateToNumber(int x, int y, int alongX){
        return (x+alongX*y);
    }


    /*

    private void selectTileset(SpriteAnimation spriteAnimation) {
        if (!useManyColoredHittedSpritesheet) {
            if (hittedTileset == null) {
                createHittedTileset();
            }
            if ((whiteColorTimer != null && !whiteColorTimer.isTime())) {
                spriteAnimation.setTileset(hittedTileset);
            } else {
                spriteAnimation.setTileset(mainTileset);
            }
        }
        else {
            if (hittedTilesets.size() == 0) {
                createHittedTilesetArray();
            }
            if ((whiteColorTimer != null && !whiteColorTimer.isTime())) {
                if (subColorTimer == null){
                    subColorTimer = new Timer(WHITE_COLOR_TIME/COLOR_CHANGING_STAGES);
                    actualHittedTilesetNumber = 0;
                }
                else if (subColorTimer.isTime()){
                    subColorTimer.setNewTimer(WHITE_COLOR_TIME/COLOR_CHANGING_STAGES);
                    actualHittedTilesetNumber++;
                    if (actualHittedTilesetNumber >(hittedTilesets.size()-1)) actualHittedTilesetNumber = 0;
                }
                System.out.println("Actual drawn sprite " + actualHittedTilesetNumber);
                spriteAnimation.setTileset(hittedTilesets.get(actualHittedTilesetNumber));
            } else {
                spriteAnimation.setTileset(mainTileset);
            }
        }
    }*/

    private void selectTileset(SingleGraphicElement sprite) {
        /*if (dead){
            sprite.setTileset(mainTileset);
        }
        else {*/
        if (!useManyColoredHittedSpritesheet) {
            if (hittedTileset == null) {
                createHittedTileset();
            }
            if ((whiteColorTimer != null && !whiteColorTimer.isTime())) {
                sprite.setTileset(hittedTileset);
            } else {
                sprite.setTileset(mainTileset);
            }
        }
        else {
            if (hittedTilesets.size() == 0) {
                createHittedTilesetArray();
            }
            if ((whiteColorTimer != null && !whiteColorTimer.isTime())) {
                if (subColorTimer == null) {
                    subColorTimer = new Timer(WHITE_COLOR_TIME / COLOR_CHANGING_STAGES);
                    actualHittedTilesetNumber = 0;
                } else if (subColorTimer.isTime()) {
                    subColorTimer.setNewTimer(WHITE_COLOR_TIME / COLOR_CHANGING_STAGES);
                    actualHittedTilesetNumber++;
                    if (actualHittedTilesetNumber > (hittedTilesets.size() - 1)) actualHittedTilesetNumber = 0;
                }
                //System.out.println("Actual drawn sprite " + actualHittedTilesetNumber);

                sprite.setTileset(hittedTilesets.get(actualHittedTilesetNumber));
            } else {
                sprite.setTileset(mainTileset);
            }
        }
        //}
    }

    public void setDeadSprite(int animationType, int deadSpriteNumber) {
        ANIMATION_TYPE_FOR_DEAD_SPRITE = animationType;
        DEAD_SPRITE = deadSpriteNumber;
    }

    public void resetAnimation(int actualAnimationType) {
        for (Map.Entry animation :  animations.entrySet()) {
            if ((int)animation.getValue() == actualAnimationType){
                SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                spriteAnimation.reset();
            }
        }
    }

    public void resetTint() {
        for (Map.Entry animation :  animations.entrySet()) {
            if ((int)animation.getValue() == actualAnimationType){
                SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                spriteAnimation.resetTint();
            }
        }
    }

    public void setNewDimention(int dimention){
        for (Map.Entry animation :  animations.entrySet()) {
            if ((int)animation.getValue() == actualAnimationType) {
                SpriteAnimation spriteAnimation = (SpriteAnimation) animation.getKey();
                spriteAnimation.setWidth((int) dimention);
                spriteAnimation.setHeight((int) dimention);
                //System.out.println("Dimension was set !!! ");
            }
        }
    }

    public void setNewDimensionForAllAnimations(int dimention){
        for (Map.Entry animation :  animations.entrySet()) {
                SpriteAnimation spriteAnimation = (SpriteAnimation) animation.getKey();
                spriteAnimation.setWidth( dimention);
                spriteAnimation.setHeight(dimention);
                //System.out.println("Dimension was set !!! ");
        }
    }
    /*
    public void setNewDimention(int dimention){
        for (Map.Entry animation :  animations.entrySet()) {
            if ((int)animation.getValue() == actualAnimationType){
                SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                spriteAnimation.setWidth((int)dimention);
                spriteAnimation.setHeight((int)dimention);
            }
        }
    }
     */


    public void setElementWidth(int newDiameter) {
        for (Map.Entry animation :  animations.entrySet()) {
            if ((int)animation.getValue() == actualAnimationType){
                SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                spriteAnimation.setWidth((int)newDiameter);
            }
        }
    }


    public void setElementHeight(int newDiameter) {
        for (Map.Entry animation :  animations.entrySet()) {
            if ((int)animation.getValue() == actualAnimationType){
                SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
                spriteAnimation.setHeight(newDiameter);
            }
        }
    }

    public int getLastDrawnAnimationType() {
        return lastDrawnAnimationType;
    }

    @Override
    public ArrayList<SpriteAnimation> getAnimationsList() {
        spriteAnimationsToBeReturned.clear();
        for (Map.Entry animation :  animations.entrySet()) {
            SpriteAnimation spriteAnimation = (SpriteAnimation)animation.getKey();
            spriteAnimationsToBeReturned.add(spriteAnimation);
        }
        return spriteAnimationsToBeReturned;
    }

    @Override
    public SpriteAnimation getAnimationForType(int type) {
            for (Map.Entry animation :  animations.entrySet()) {
                if ((int)animation.getValue() == type){
                    SpriteAnimation spriteAnimation = (SpriteAnimation) animation.getKey();
                    return spriteAnimation;
                }
            }
            System.out.println("This animation was not found");
            return null;
    }

    @Override
    public void setUnderAttack(boolean underAttack) {
        if (underAttack){
            if (withWhiteHittedSprite && !dead){
                if (whiteColorTimer == null){
                    whiteColorTimer = new Timer(WHITE_COLOR_TIME);
                }
                else whiteColorTimer.setNewTimer(WHITE_COLOR_TIME);
                System.out.println("Color set as white");
            }
            /*else if (dead){
                if (previousDead == false) previousDead = dead;
            }*/
        }

    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }


    public StaticSprite getSprite(int type) {
        for (Map.Entry sprite :  sprites.entrySet()) {
            if ((int)sprite.getValue() == type){
                StaticSprite spriteAnimation = (StaticSprite) sprite.getKey();
                return spriteAnimation;
            }
        }
        System.out.println("This sprite was not found");
        return null;

    }


}
