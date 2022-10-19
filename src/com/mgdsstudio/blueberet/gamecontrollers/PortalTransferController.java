package com.mgdsstudio.blueberet.gamecontrollers;

import com.mgdsstudio.blueberet.gameobjects.Flag;
import com.mgdsstudio.blueberet.gameobjects.GameObject;
import com.mgdsstudio.blueberet.gameobjects.portals.PipePortal;
import com.mgdsstudio.blueberet.gameobjects.portals.PortalWithVisibleMoving;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.gameprocess.sound.SoundsInGame;
import com.mgdsstudio.blueberet.gameprocess.PlayerControl;
import org.jbox2d.common.Vec2;

import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;

import processing.core.PVector;

public class PortalTransferController {
	private PlayerControl playerControl;
	public PortalWithVisibleMoving portal;
	public GameObject object;
	private boolean ended = false;
	private boolean started = false;
	private boolean objectJumped = false;
	private float gravityScale = 0f;
	
	public PortalTransferController(PlayerControl playerControl, PipePortal portal, GameObject object) {
		this.portal = portal;
		this.object = object;
		this.playerControl = playerControl;
		System.out.println("Enter: " + portal.getEnterCenterPos().x + "; exit: " + portal.getExitCenterPos().x);
		System.out.println("Object on " + object.getPixelPosition().x);
	}

	private void changePersonDirectionAccordingToPortalExit(){
		/*
		System.out.println("Player direction was changed");
		if (object instanceof Person){
			Person person = (Person) object;
			if (portal.getExit().getDirection() == Flag.TO_RIGHT){
				person.setSightDirection(Person.TO_RIGHT);
				person.setWeaponAngle(0);	//Only for player
			}
			else if (portal.getExit().getDirection() == Flag.TO_LEFT){
				person.setSightDirection(Person.TO_LEFT);
				person.setWeaponAngle(180);	//Only for player
			}
		}

		 */
		//System.out.println("Player direction was not changed");
	}
	
	public void update(GameRound gameRound) {
		if (!started) start(gameRound);
		if (ended == false) {
			if (object.isTransferingThroughPortal()) {	
				if (object.isTransferingThroughPortal()) {
					if (!playerControl.isPlayerCanEnterPortal()) {
						//playerControl.hideButton(PlayerControl.BUTTON_PORTAL, true);
						System.out.println("Button is hidden");
						playerControl.setPortalButtonMustBeShown(false);
					}
					else {

					}
				}
				byte portalActivatedStage = portal.isPersonInPortal(object);
				if (portalActivatedStage == PipePortal.IN_PORTAL_ENTER_ZONE) {
					movementThroughPortal(portal.getDirection(PipePortal.ENTER));
					if (!object.isBlocked()) {
						gravityScale = object.body.getGravityScale();
						object.setBlocked(true);
						object.body.setAngularVelocity(0);
						object.body.setLinearVelocity(new Vec2(0,0));
						object.body.setActive(false);
						playerControl.hideAllButtons(true);
						System.out.println("Object on " + object.getPixelPosition().x);
					}
				}
				else if (portalActivatedStage == PipePortal.IN_TRANSFER_ZONE) {
					changePersonDirectionAccordingToPortalExit();
					object.setSleeped(true);
					if (!objectJumped) {
						object.transferTo(portal.getExitCenterPos());
						objectJumped = true;
						object.body.setGravityScale(0);
					}
					else{
						object.setBlocked(true);
						object.body.setActive(false);
						movementThroughPortal(portal.getDirection(PipePortal.EXIT));
					}
				}				
				else if (portalActivatedStage == PipePortal.IN_PORTAL_EXIT_ZONE) {
					object.setBlocked(false);
					object.body.setActive(true);
					object.body.setGravityScale(gravityScale);
					object.setSleeped(false);
					changePersonDirectionAccordingToPortalExit();
				}
				else if (portalActivatedStage == PipePortal.TRANSPORTATION_ENDED) {
					System.out.println("Transportation completed");
					object.setBlocked(false);
					object.body.setActive(true);
					object.body.setGravityScale(gravityScale);
					object.setSleeped(false);
					object.setBlocked(false);						
					object.body.setActive(true);
					playerControl.hideAllButtons(false);
					if (!playerControl.isPlayerCanEnterPortal()) {
						playerControl.setPortalButtonMustBeShown(false);
						System.out.println("Button is hidden");
					}
					object.setTransferingThroughPortal(false);
					ended = true;
					if (portal.getUsingRepeateability() == PipePortal.DISPOSABLE) {

					}
					changePersonDirectionAccordingToPortalExit();
				}
			}
		}
		if (ended) {
			if (portal.getUsingRepeateability() == PipePortal.REUSEABLE) {
				if (portal.isPersonInPortal(object) == PipePortal.WAITING_FOR_NEXT_TRANSPORTATION) {
					
				}
			}
		}
	}
	
	public void clearPortalAndFlags() {
		portal.enter  = null;
		portal.exit  = null;
		portal = null;
	}
	
	public boolean isEnded() {
		return ended;
	}
	
	public boolean mustBePortalDeleted() {
		return portal.getUsingRepeateability();
	}
	
	private void start(GameRound gameRound) {
		object.setTransferingThroughPortal(true);
		jumpToPortalCenter(object, portal.getDirection(PipePortal.ENTER), portal.getEnterCenterPos());
		object.body.applyForceToCenter(new Vec2(0,0));
		started = true;
		gameRound.getSoundController().setAndPlayAudio(SoundsInGame.JUMP_IN_PORTAL);
	}
	
	private void jumpToPortalCenter(GameObject object,  byte direction, PVector portalCenter) {
		if (direction == Flag.TO_UP || direction == Flag.TO_DOWN) {
			object.body.setTransform(new Vec2(PhysicGameWorld.controller.coordPixelsToWorld(portalCenter).x, object.body.getPosition().y) , 0);
		}
		else if (direction == Flag.TO_LEFT || direction == Flag.TO_RIGHT){
			object.body.setTransform(new Vec2( object.body.getPosition().x, PhysicGameWorld.controller.coordPixelsToWorld(portalCenter).y), 0);
		}
	}
	
	private void movementThroughPortal(byte direction) {	
		//System.out.println("Direction: " + direction);
		float normalVelocity = PipePortal.NORMAL_TRANSFERING_SPEED;
		Vec2 newDeltaPosition = new Vec2(0,0);
		if (direction == Flag.TO_RIGHT) newDeltaPosition.x = normalVelocity;
		else if (direction == Flag.TO_LEFT) newDeltaPosition.x = -normalVelocity;
		else if (direction == Flag.TO_UP) newDeltaPosition.y = -normalVelocity;
		else newDeltaPosition.y = normalVelocity;

		newDeltaPosition.x/= Program.engine.frameRate;
		newDeltaPosition.y/= Program.engine.frameRate;
		Vec2 newPosition = new Vec2(object.body.getPosition());		
		Vec2 addingVector = new Vec2(PhysicGameWorld.controller.vectorPixelsToWorld(newDeltaPosition));		

		newPosition.x+=addingVector.x;
		newPosition.y+=addingVector.y;

		//System.out.println("Delta: " + newDeltaPosition + " Vector: " + addingVector)  ;
		object.body.setTransform(new Vec2(newPosition.x, newPosition.y), object.body.getAngle());
	}
	
}
