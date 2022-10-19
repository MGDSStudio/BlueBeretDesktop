package com.mgdsstudio.blueberet.oldlevelseditor;

import com.mgdsstudio.blueberet.gamecontrollers.MoveablePlatformsController;
import com.mgdsstudio.blueberet.gameobjects.*;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.*;
import com.mgdsstudio.blueberet.gameobjects.data.GameObjectDataForStoreInEditor;
import com.mgdsstudio.blueberet.gameobjects.persons.*;
import com.mgdsstudio.blueberet.gameobjects.portals.PipePortal;
import com.mgdsstudio.blueberet.gameprocess.EndLevelZone;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenAnimation;
import com.mgdsstudio.blueberet.zones.CameraFixationZone;
import com.mgdsstudio.blueberet.zones.ObjectsClearingZone;
import com.mgdsstudio.blueberet.graphic.IndependentOnScreenStaticSprite;
import com.mgdsstudio.blueberet.graphic.background.Background;
import com.mgdsstudio.blueberet.graphic.background.RepeatingBackgroundElement;
import com.mgdsstudio.blueberet.graphic.background.ScrollableAlongXBackground;
import com.mgdsstudio.blueberet.graphic.background.SingleColorBackground;
import com.mgdsstudio.blueberet.oldlevelseditor.submenuaction.PlayerReplacingAction;
import com.mgdsstudio.blueberet.loading.ExternalRoundDataFileController;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import processing.core.PVector;

class NewObjectsCreator {

    NewObjectsCreator(){

    }



    boolean mustBeNewObjectCreated(LevelsEditorProcess levelsEditorProcess) {
        if (levelsEditorProcess.levelsEditorControl.tabsController.mustBeLocalStatementReset(levelsEditorProcess.levelsEditorControl.submenuAction)) {
            return true;
        } else return false;
    }

    void createNewObject(LevelsEditorProcess levelsEditorProcess){
        levelsEditorProcess.levelsEditorControl.tabsController.resetLocalStatementTest();
        createNewObject(levelsEditorProcess, levelsEditorProcess.levelsEditorControl, levelsEditorProcess.getGameRound());

    }

    private void createNewObject(LevelsEditorProcess levelsEditorProcess, LevelsEditorControl levelsEditorControl, GameRound gameRound){
        //System.out.println(levelsEditorControl.objectData.getClassName());
        if (levelsEditorControl.objectData.getClassName() == ExternalRoundDataFileController.roundBoxType){
            RoundElement roundBox = new RoundBox(levelsEditorControl.objectData);
            roundBox.loadSprites(gameRound.getMainGraphicController().getTilesetUnderPath(roundBox.getSprite().getPath()));
            gameRound.addNewGameObject(roundBox);
            levelsEditorControl.objectData.setDataString(roundBox.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
        }
        if (levelsEditorControl.objectData.getClassName() == RoundCircle.CLASS_NAME){
            try {
                System.out.println("New round circle must be created");
                RoundElement roundCircle = new RoundCircle(levelsEditorControl.objectData);
                roundCircle.loadSprites(gameRound.getMainGraphicController().getTilesetUnderPath(roundCircle.getSprite().getPath()));
                gameRound.addNewGameObject(roundCircle);
                levelsEditorControl.objectData.setDataString(roundCircle.getStringData());
                GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor) levelsEditorControl.objectData.clone();
                Editor2D.addDataForNewObject(dataToSave);
                clearFiguresAndPoints(levelsEditorProcess);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        if (levelsEditorControl.objectData.getClassName() == RoundPolygon.CLASS_NAME){
            try {
                System.out.println("New polygon must be created");
                RoundElement roundPolygon = new RoundPolygon(levelsEditorControl.objectData);
                System.out.println("Sprite is null " + (roundPolygon.getSprite() == null));
                roundPolygon.loadSprites(gameRound.getMainGraphicController().getTilesetUnderPath(roundPolygon.getSprite().getPath()));
                gameRound.addNewGameObject(roundPolygon);
                levelsEditorControl.objectData.setDataString(roundPolygon.getStringData());
                GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor) levelsEditorControl.objectData.clone();
                Editor2D.addDataForNewObject(dataToSave);
                clearFiguresAndPoints(levelsEditorProcess);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (levelsEditorControl.objectData.getClassName() == ExternalRoundDataFileController.staticSpriteType){
            System.out.println("New independent static sprite was created in the world");
            IndependentOnScreenStaticSprite sprite = new IndependentOnScreenStaticSprite(levelsEditorControl.objectData);
            sprite.staticSprite.loadSprite(gameRound.getMainGraphicController().getTilesetUnderPath(sprite.staticSprite.getPath()));
            gameRound.addGraphicElement(sprite);
            levelsEditorControl.objectData.setDataString(sprite.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
        }
        else if (levelsEditorControl.objectData.getClassName() == IndependentOnScreenAnimation.CLASS_NAME){
            IndependentOnScreenAnimation animation = new IndependentOnScreenAnimation(levelsEditorControl.objectData);
            animation.spriteAnimation.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(animation.spriteAnimation.getPath()));
            gameRound.addGraphicElement(animation);
            System.out.println("New sprite animation was created in the world with data: " + levelsEditorControl.objectData.getSpriteAnimation().getWidth() + ", " + levelsEditorControl.objectData.getSpriteAnimation().getHeight() );

            levelsEditorControl.objectData.setDataString(animation.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
            animation.update();
        }
        else if (levelsEditorControl.objectData.getClassName() == ExternalRoundDataFileController.objectClearingZoneType){
            ObjectsClearingZone zone = new ObjectsClearingZone(levelsEditorControl.objectData);
            System.out.println("Activating condition: " + zone.activatingCondition + " must be " + levelsEditorControl.objectData.getGoal());
            gameRound.addNewGameObject(zone);
            levelsEditorControl.objectData.setDataString(zone.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
        }
        else if (levelsEditorControl.objectData.getClassName() == EndLevelZone.CLASS_NAME){
            EndLevelZone zone = new EndLevelZone(levelsEditorControl.objectData);
            gameRound.addNewGameObject(zone);
            levelsEditorControl.objectData.setDataString(zone.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
            System.out.println("End level zone was created");
        }
        else if (levelsEditorControl.objectData.getClassName() == CameraFixationZone.CLASS_NAME){
            System.out.println("Try to add a camera fixation zone");
            CameraFixationZone zone = new CameraFixationZone(levelsEditorControl.objectData);
            gameRound.addNewGameObject(zone);
            levelsEditorControl.objectData.setDataString(zone.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
            System.out.println("Camera fixation zone was created");
        }
        else if (levelsEditorControl.objectData.getClassName() == CollectableObjectInNesStyle.CLASS_NAME){
            CollectableObjectInNesStyle collectableObject = new CollectableObjectInNesStyle(levelsEditorControl.objectData, gameRound);
            if (collectableObject.getInWorldPosition() == CollectableObjectInNesStyle.IN_WORLD) collectableObject.loadAnimation(gameRound.getMainGraphicController().getTilesetUnderPath(collectableObject.getSpriteAnimation().getPath()));
            gameRound.addNewGameObject(collectableObject);
            levelsEditorControl.objectData.setDataString(collectableObject.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
            System.out.println("Collectable object was created");
        }
        else if (levelsEditorControl.objectData.getClassName() == SimpleCollectableElement.CLASS_NAME){
            System.out.println("Try to create new collectable element");
            AbstractCollectable collectableObject = null;
            if (levelsEditorControl.objectData.getType() == AbstractCollectable.ABSTRACT_AMMO){
                System.out.println("try to create ammo");
                collectableObject = new WeaponMagazine(levelsEditorControl.objectData, gameRound);
            }
            else if (levelsEditorControl.objectData.getType() == AbstractCollectable.ABSTRACT_COIN || levelsEditorControl.objectData.getType() == AbstractCollectable.ABSTRACT_GEM){
                System.out.println("try to create money");
                collectableObject = new Money(levelsEditorControl.objectData, gameRound);
            }
            else if (levelsEditorControl.objectData.getType() == AbstractCollectable.SYRINGE){
                System.out.println("try to create syringe");
                collectableObject = new Syringe(levelsEditorControl.objectData, gameRound);
            }
            else if (levelsEditorControl.objectData.getType() == AbstractCollectable.ABSTRACT_FRUIT){
                System.out.println("try to create a fruit");
                collectableObject = new Fruit(levelsEditorControl.objectData, gameRound);
            }
            else {
                System.out.println("try to create something else by type " + levelsEditorControl.objectData.getType() + "; and localL: " + levelsEditorControl.objectData.getLocalType());
                collectableObject = new MedicalKit(levelsEditorControl.objectData, gameRound);
            }
            gameRound.addNewGameObject(collectableObject);
            levelsEditorControl.objectData.setDataString(collectableObject.getStringData());
            System.out.println("Data string is: " + collectableObject.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
            System.out.println("Simple collectable object was created");
        }
        else if (levelsEditorControl.objectData.getClassName() == RoundPipe.CLASS_NAME) {
            RoundPipe roundPipe = new RoundPipe(levelsEditorControl.objectData);
            roundPipe.loadSprites(gameRound.getMainGraphicController().getTilesetUnderPath(roundPipe.getSprite().getPath()));
            gameRound.addNewGameObject(roundPipe);
            levelsEditorControl.objectData.setDataString(roundPipe.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
            System.out.println("Round pipe was created");

        }
        else if (levelsEditorControl.objectData.getClassName() == PipePortal.CLASS_NAME){
            PipePortal portal = new PipePortal(levelsEditorControl.objectData);
            gameRound.addNewGameObject(portal);
            levelsEditorControl.objectData.setDataString(portal.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
            System.out.println("Portal was created");
            levelsEditorProcess.figures.clear();
            levelsEditorProcess.pointsOnMap.clear();
        }
        else if (levelsEditorControl.objectData.getClassName() == Bridge.CLASS_NAME){
            Bridge bridge = new Bridge(gameRound, levelsEditorControl.objectData);
            gameRound.addNewGameObject(bridge);
            levelsEditorControl.objectData.setDataString(bridge.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
            System.out.println("Bridge was created");
        }
        else if (levelsEditorControl.objectData.getClassName() == MoveablePlatformsController.CLASS_NAME){
            MoveablePlatformsController controller = new MoveablePlatformsController(levelsEditorControl.objectData);
            System.out.println("Graphic zone: " + levelsEditorControl.objectData.getGraphicWidth() + " x " + levelsEditorControl.objectData.getGraphicHeight());
            System.out.println("Graphic width is always 0!!!! It can be ");
            controller.loadSprites(gameRound.getMainGraphicController().getTilesetUnderPath(controller.getSprite().getPath()));
            gameRound.addNewGameObject(controller);
            levelsEditorControl.objectData.setDataString(controller.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor)levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
            System.out.println("Platforms were created");
        }

        else if (levelsEditorControl.objectData.getClassName() == ExternalRoundDataFileController.soldierType){
            GameObject player = gameRound.getPlayer();
            PlayerReplacingAction playerSubmenu = (PlayerReplacingAction)levelsEditorControl.submenuAction;
            levelsEditorControl.objectData.setDataString(player.getStringData());
            GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor) levelsEditorControl.objectData.clone();
            Editor2D.addDataForNewObject(dataToSave);
            System.out.println("Soldier was replaced");
            levelsEditorProcess.writeObjectsDataForLastObject();
        }
        else if (levelsEditorControl.objectData.getClassName() == Background.CLASS_NAME){
            Background background = null;
            boolean backgroundSuccesfullyDetermined = true;
            if (levelsEditorControl.objectData.getType() == Background.SINGLE_COLOR_BACKGROUND) background = new SingleColorBackground(levelsEditorControl.objectData);
            else if (levelsEditorControl.objectData.getType() == Background.SCROLLABLE_PICTURE_BACKGROUND) background = new ScrollableAlongXBackground(levelsEditorControl.objectData);
            else if (levelsEditorControl.objectData.getType() == Background.REPEATING_BACKGROUND_ELEMENTS) background = new RepeatingBackgroundElement(levelsEditorControl.objectData);

            else {
                backgroundSuccesfullyDetermined = false;
            }
            if (backgroundSuccesfullyDetermined) {
                levelsEditorControl.objectData.setDataString(background.getStringData());
                GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor) levelsEditorControl.objectData.clone();
                Editor2D.addDataForNewObject(dataToSave);
                System.out.println("Background saved!");
            }
        }
        else if (levelsEditorControl.objectData.getClassName() == Gumba.CLASS_NAME || levelsEditorControl.objectData.getClassName() == Bowser.CLASS_NAME || levelsEditorControl.objectData.getClassName() == Koopa.CLASS_NAME || levelsEditorControl.objectData.getClassName() == Spider.CLASS_NAME || levelsEditorControl.objectData.getClassName() == Snake.CLASS_NAME) {
            {
                Person gameObject = null;
                if (levelsEditorControl.objectData.getClassName() == Gumba.CLASS_NAME) {
                    gameObject = new Gumba(new PVector(levelsEditorControl.objectData.getPosition().x, levelsEditorControl.objectData.getPosition().y), levelsEditorControl.objectData.getLife(), levelsEditorControl.objectData.getDimension());

                } else if (levelsEditorControl.objectData.getClassName() == Bowser.CLASS_NAME) {
                    gameObject = new Bowser(new PVector(levelsEditorControl.objectData.getPosition().x, levelsEditorControl.objectData.getPosition().y), levelsEditorControl.objectData.getLife(), levelsEditorControl.objectData.getDimension());
                }
                else if (levelsEditorControl.objectData.getClassName() == Koopa.CLASS_NAME) {
                    gameObject = new Koopa(new PVector(levelsEditorControl.objectData.getPosition().x, levelsEditorControl.objectData.getPosition().y), levelsEditorControl.objectData.getAI_Model(),  levelsEditorControl.objectData.getLife(), levelsEditorControl.objectData.getDimension());
                }
                else if (levelsEditorControl.objectData.getClassName() == Spider.CLASS_NAME) {
                    gameObject = new Spider(new PVector(levelsEditorControl.objectData.getPosition().x, levelsEditorControl.objectData.getPosition().y), levelsEditorControl.objectData.getAI_Model(),  levelsEditorControl.objectData.getLife(), levelsEditorControl.objectData.getDimension());
                }
                else if (levelsEditorControl.objectData.getClassName() == Snake.CLASS_NAME) {
                    gameObject = new Snake(new PVector(levelsEditorControl.objectData.getPosition().x, levelsEditorControl.objectData.getPosition().y), levelsEditorControl.objectData.getAI_Model(),  levelsEditorControl.objectData.getLife(), levelsEditorControl.objectData.getDimension());
                }
                gameRound.addNewGameObject(gameObject);
                levelsEditorControl.objectData.setDataString(gameObject.getStringData());
                GameObjectDataForStoreInEditor dataToSave = (GameObjectDataForStoreInEditor) levelsEditorControl.objectData.clone();
                Editor2D.addDataForNewObject(dataToSave);
                System.out.println("New person were created " + levelsEditorControl.objectData.getClassName());
            }
        }
        System.out.println("One world step is made");
        PhysicGameWorld.controller.step(0.000001f, 20,20);
        PhysicGameWorld.makeAllBodiesInactive();
        //levelsEditorProcess.figures.clear();
    }

    private void clearFiguresAndPoints(LevelsEditorProcess levelsEditorProcess) {
        levelsEditorProcess.pointsOnMap.clear();
        levelsEditorProcess.figures.clear();
    }
}
