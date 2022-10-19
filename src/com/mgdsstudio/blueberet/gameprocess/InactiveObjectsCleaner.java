package com.mgdsstudio.blueberet.gameprocess;

import com.mgdsstudio.blueberet.gameobjects.RoundPipe;
import com.mgdsstudio.blueberet.gameobjects.persons.Soldier;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;

class InactiveObjectsCleaner {
    private int maxPoolForSplashes = -1;
    private final int UPDATING_FREQUENCY = 8;
    private final int UPDATING_STEP_TO_DELETE_PART = UPDATING_FREQUENCY /4;

    InactiveObjectsCleaner(){
        if (GameRound.withSplashesPool){
            maxPoolForSplashes = 8;
        }
        else maxPoolForSplashes = -1;
    }

    public void update(GameRound gameRound){
        if (Program.engine.frameCount % UPDATING_FREQUENCY == 0) {
            clearEndedExplosions(gameRound);
            clearRedundantRoundElements(gameRound);
            clearEndedIndependedOnScreenAnimations(gameRound);
            updateEndLevelCondition(gameRound);
            clearObjectsInClearingZones(gameRound);
            clearPortalControllers(gameRound);
        }
        else if (Program.engine.frameCount % UPDATING_FREQUENCY == UPDATING_STEP_TO_DELETE_PART){
            clearEndedSplashed(gameRound);
            clearSecondaryRoundElements(gameRound);
            clearDeadEnemies(gameRound);
            clearPlants(gameRound);
            if (Program.WITH_LIFE_LINES) clearHUD_LifeLines(gameRound);
            clearFallingGraphicElements(gameRound);
            clearDissolvedTexts(gameRound);
            if (Program.debug) clearDebugGraphic(gameRound);
        }
    }

    private void clearPortalControllers(GameRound gameRound) {
        for (int i = (gameRound.portalTransferControllers.size()-1); i >= 0; i--){
            if (gameRound.portalTransferControllers.get(i).isEnded()){
                gameRound.portalTransferControllers.remove(i);
            }
        }
    }

    private void clearDissolvedTexts(GameRound gameRound) {
        for (int i = (gameRound.getDissolvingAndUpwardsMovingTexts().size()-1); i>= 0; i--){
            if (gameRound.getDissolvingAndUpwardsMovingTexts().get(i).canBeDeleted()){
                gameRound.getDissolvingAndUpwardsMovingTexts().remove(i);
            }
        }
    }

    private void clearFallingGraphicElements(GameRound gameRound) {

    }

    private void clearDebugGraphic(GameRound gameRound) {
        for (int i = (gameRound.getDebugGraphics().size()-1); i>= 0; i--){
            if (gameRound.getDebugGraphics().get(i).canBeDeleted()){
                gameRound.getDebugGraphics().remove(i);
            }
        }
    }

    private void clearPlants(GameRound gameRound) {
        for (RoundPipe roundPipe: gameRound.roundPipes){
            if (roundPipe.hasFlower()){
                if (roundPipe.getPlantController()!=null){
                    if (roundPipe.getPlantController().isAllPlantPartsKilled()){
                        roundPipe.deletePlantController();
                    }
                }
            }
        }
    }

    private void clearDeadEnemies(GameRound gameRound) {
        for (int i = gameRound.persons.size()-1; i >= 0; i--){
            if (gameRound.persons.get(i).isDead()){
                try{
                    if (!gameRound.persons.get(i).body.isActive() && !gameRound.persons.get(i).isAlive() && !gameRound.persons.get(i).equals(gameRound.getPlayer())) {
                        PhysicGameWorld.controller.world.destroyBody(gameRound.persons.get(i).body);
                        gameRound.persons.remove(gameRound.persons.get(i));
                        System.out.println("Person was successfully deleted from the world");
                    }
                }
                catch (Exception e){
                    System.out.println("Can not delete round element from map " + e);
                }
            }
        }
    }

    private void clearSecondaryRoundElements(GameRound gameRound) {
        for (int i = gameRound.roundElements.size()-1; i >= 0; i--){
            if (gameRound.roundElements.get(i).isSecondary()){
                try{
                    if (!gameRound.roundElements.get(i).body.isActive()) {
                        PhysicGameWorld.controller.world.destroyBody(gameRound.roundElements.get(i).body);
                        gameRound.roundElements.remove(gameRound.roundElements.get(i));
                        System.out.println("Round element was successfully deleted from the world");
                    }
                }
                catch (Exception e){
                    System.out.println("Can not delete round element from map " + e);
                }
            }
        }
    }

    private void clearObjectsInClearingZones(GameRound gameRound) {
        for (int i = (gameRound.roundElements.size()-1); i>= 0; i--){
            if (gameRound.roundElements.get(i).isAppearedInClearingZone()){
                gameRound.roundElements.remove(i);
            }
        }
        for (int i = (gameRound.persons.size()-1); i>= 0; i--){
            if (gameRound.persons.get(i).isAppearedInClearingZone()){
                if (gameRound.persons.get(i).getClass() != Soldier.class) {
                    gameRound.persons.remove(i);
                }
            }
        }
    }

    private void updateEndLevelCondition(GameRound gameRound){
        if (gameRound.areThereActiveTitles()){
            for (int i = (gameRound.titles.size()-1); i>=0; i--) {
                if (gameRound.titles.get(i).isEnded()) {
                    gameRound.titles.remove(gameRound.titles.get(i));
                }
            }
        }
    }

    private void clearHUD_LifeLines(GameRound gameRound){   // Used ananlog in controller
        for (int i = (gameRound.getHud_lifeLinesController().getHud_lifeLines().size()-1); i >= 0; i--){
            if (gameRound.getHud_lifeLinesController().getHud_lifeLines().get(i).canBeDeleted()){
                gameRound.getHud_lifeLinesController().getHud_lifeLines().remove(i);
            }
        }
    }

    private void clearEndedSplashed(GameRound gameRound) {
        if (maxPoolForSplashes > 0) {
            if (gameRound.splashes.size() >= maxPoolForSplashes) {
                for (int i = (gameRound.splashes.size() - 1); i >= 0; i--) {

                    if (gameRound.splashes.get(i).canBeDeleted()) {
                        gameRound.splashes.remove(gameRound.splashes.get(i));
                    }
                    if (gameRound.splashes.size() < maxPoolForSplashes) break;
                }
            }
        }
        else {
            for (int i = (gameRound.splashes.size() - 1); i >= 0; i--) {
                if (gameRound.splashes.get(i).canBeDeleted()) {
                    gameRound.splashes.remove(gameRound.splashes.get(i));
                }
            }
        }

    }

    private void clearEndedIndependedOnScreenAnimations(GameRound gameRound) {
        for (int i = (gameRound.independentOnScreenAnimations.size()-1); i >= 0; i--) {
            if (gameRound.independentOnScreenAnimations.get(i).canBeDeleted()) {
                gameRound.independentOnScreenAnimations.remove(gameRound.independentOnScreenAnimations.get(i));
                if (Program.debug) System.out.println("Animation: " + i + " was deleted");
            }
        }
    }

    private void clearEndedExplosions(GameRound gameRound) {
        for (int i = (gameRound.explosions.size()-1); i >= 0; i--) {
            if (gameRound.explosions.get(i).isEnded() == true) {
                gameRound.explosions.remove(gameRound.explosions.get(i));

            }
        }
    }


    /*
    private void clearEndedIndependentOnScreenAnimations(GameRound gameRound){
        for (IndependentOnScreenAnimation independentOnScreenAnimation : gameRound.independentOnScreenAnimations) {
            if (independentOnScreenAnimation.canBeDeleted()){
                gameRound.getIndependentOnScreenAnimations().remove(independentOnScreenAnimation);
            }
        }
    }*/

    private void clearRedundantRoundElements(GameRound gameRound) {
        for (int i = 0; i < gameRound.roundElements.size(); i++) {
            if (gameRound.roundElements.get(i).getLife() <= 0) {
                System.out.println("Killed!");
                gameRound.roundElements.remove(i);
                return;
            }
        }
    }
}
