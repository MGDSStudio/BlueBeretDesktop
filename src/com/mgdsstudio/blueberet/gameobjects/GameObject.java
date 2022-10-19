package com.mgdsstudio.blueberet.gameobjects;

import com.mgdsstudio.blueberet.gamelibraries.GameMechanics;
import com.mgdsstudio.blueberet.gameobjects.collectableobjects.AbstractCollectable;
import com.mgdsstudio.blueberet.gameprocess.CollisionFilterCreator;
import com.mgdsstudio.blueberet.mainpackage.Program;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;
import com.mgdsstudio.blueberet.mainpackage.PhysicGameWorld;
import com.mgdsstudio.blueberet.weapon.Weapon;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import processing.core.PApplet;
import processing.core.PVector;

import java.io.*;
import java.util.ArrayList;

public abstract class GameObject extends SingleGameElement implements Cloneable, Serializable {

	protected ArrayList<AbstractCollectable> collectableObjects;
	public final static boolean X_Axis = true;
	public final static boolean Y_Axis = false;
	public Body body;
	public boolean isAttacked;
	public static final int IMMORTALY_LIFE = 9999;
	protected int life = IMMORTALY_LIFE;
	protected int maxLife = life;
	protected int angleInDegrees;
	//protected boolean relationshipToDeath
	protected boolean dead = false;
	protected boolean sleeped = false;
	private boolean blocked = false;
	private boolean transferingThroughPortal;
	protected float boundingWidth;	// To determine objets uploading oder hidding from the world
	protected float boundingHeight;	// To determine objets uploading oder hidding from the world
	//protected Vec2 lastPosition = new Vec2();
	
	// Coalision filtering
	public static byte COAlISION_FLOWER_WITH_PIPE_GROUP = -1;
	protected byte COALISION_PLAYER_WITH_BULLET_GROUP = -2;
	protected byte COALISION_ROTATING_STICK_WITH_ROUND_ELEMENTS = -3;
	protected byte COALISION_DRAGON_FIRE_WITH_BOWSER = -4;


	protected boolean appearedInClearingZone = false;



	// Memory consumption variables

	private final static float yFlip = PhysicGameWorld.yFlip;
	private final static float worldScaleFactor = PhysicGameWorld.worldScale;
	private final PVector actualPixelsPosition = new PVector(0,0);
	protected final Vec2 positionInPrevFrame = new Vec2(actualPixelsPosition.x, actualPixelsPosition.y);
	private final PVector onScreenPosition = new PVector(0,0);
	private final Vec2 actualInWorldPosition = new Vec2(0,0);
	private float transX = (float)(Program.engine.width / 2);
	private float transY = (float)(Program.engine.height / 2);
	private int lastPositionUpdatingFrameNumber;

	
	public GameObject(PVector position, float angle){
		//this.position = position;
		//this.angle = angle;
	}
	
	public GameObject(){}


	public float getWidth() {
		return boundingWidth;
	}

	public float getHeight() {
		return boundingHeight;
	}

	protected void setFilterDataForCategory(int category){
		for (Fixture f = body.getFixtureList(); f!=null; f=f.getNext()) {
			f.m_filter.categoryBits = category;
			f.m_filter.maskBits = CollisionFilterCreator.getMaskForCategory(category);
			//f.m_filter.groupIndex = 0;
			//System.out.println("For category: " + category + " is value: " + f.m_filter.groupIndex);
		}
	}

	/*
	public float getAngle() {
		return angle;
	}*/
	
	protected void setLife(int life, int maxLife) {
		this.maxLife = maxLife;
		this.life = life;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/*
	public Object getClone() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream outputArray = new ByteArrayOutputStream();
		ObjectOutputStream output = new ObjectOutputStream(outputArray);
		output.writeObject(this);
		output.close();
		ByteArrayInputStream inputArray = new ByteArrayInputStream(outputArray.toByteArray());
		ObjectInputStream input = new ObjectInputStream(inputArray);
		return input.readObject();
	}
	*/

	public GameObject getClone() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream outputArray = new ByteArrayOutputStream();
		ObjectOutputStream output = new ObjectOutputStream(outputArray);
		output.writeObject(this);
		output.close();
		ByteArrayInputStream inputArray = new ByteArrayInputStream(outputArray.toByteArray());
		ObjectInputStream input = new ObjectInputStream(inputArray);
		GameObject gameObject = (GameObject) input.readObject();
		return gameObject;
	}


	protected void saveActualPosition(){
		/*
		try {
			angle = new PVector(body.getLinearVelocity().x, body.getLinearVelocity().y).heading();
		}
		catch (Exception e){
			System.out.println("can not set angle");
		}
*/
		/*
		try{
			lastPosition.x = getAbsolutePosition().x;
			lastPosition.y = getAbsolutePosition().y;
		}
		catch (Exception e){
			System.out.println(this + " + loose body");
		}
		*/

	}



	public void setNewPosition(Vec2 newPosition){
		if (body.isActive()) {
			body.setActive(false);
			body.setTransform(PhysicGameWorld.controller.vectorPixelsToWorld(newPosition), body.getAngle());
			body.setActive(true);
		}
		else body.setTransform(PhysicGameWorld.controller.vectorPixelsToWorld(newPosition), body.getAngle());
	}

	public ArrayList<AbstractCollectable> getCollectableObjects(){
		return collectableObjects;
	}

	public AbstractCollectable getCollectableObject(int i){
		if (i < collectableObjects.size() && i >= 0) return collectableObjects.get(i);
		else {
			System.out.println("There are no collectable objects by " + this.getClass());
			return null;
		}
	}

	public void removeCollectableObject(int i){
		if (i < collectableObjects.size() && i >= 0) collectableObjects.remove(i);
		else {
			System.out.println("There are no collectable objects by " + this.getClass());
		}
	}

	public void addNewCollectableObject(AbstractCollectable collectableObject){
		if (collectableObjects == null) collectableObjects = new ArrayList<>();
		collectableObjects.add(collectableObject);
	}
	
	void addDebugModel(byte debugModelType, int debugDiameter){
		//debugModel = new DebugModel(debugModelType, debugDiameter);
	}
	
	void showDebugModel(GameCamera gameCamera) {	
		//debugModel.show(gameCamera, position, angle);
	}

	public boolean hasAnyCollectableObjects(){
		if (collectableObjects != null){
			if (collectableObjects.size()>0){
				return true;
			}
			else return false;
		}
		else return false;
	}

	protected boolean isObjectGraphicInVisibleZone(GameCamera gameCamera, int leftSide, int rightSide, int upperSide, int lowerSide){
		if (leftSide < gameCamera.getDisplayBoard(GameCamera.RIGHT_SIDE)&&
				rightSide>gameCamera.getDisplayBoard(GameCamera.LEFT_SIDE)){
			if (upperSide<gameCamera.getDisplayBoard(GameCamera.LOWER_SIDE)&&
					lowerSide>gameCamera.getDisplayBoard(GameCamera.UPPER_SIDE)){
				return true;
			}
			else return false;
		}
		else return false;
	}

	public void resetFilterData() {
		Filter filter = new Filter();
		filter.groupIndex = 0;
		for (Fixture fixture = body.getFixtureList(); fixture!=null; fixture = fixture.getNext()) {
			fixture.setFilterData(filter);
		}
	}

	protected void tintUpdatingBySelecting(){
		if (isSelected()) {
			//System.out.println("Set ting value");
			//sprite.setTint(Game2D.engine.color(255, actualSelectionTintValue));
		}
		if (selectionWasCleared){
			//sprite.resetTint();
			//System.out.println("Selection tint is reset");
			selectionWasCleared = false;
		}
	}

	protected void updatePixelPos(){
		if (body != null) {
			Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
			actualPixelsPosition.x = pos.x;
			actualPixelsPosition.y = pos.y;
		}
	}
	public PVector getPixelPosition() {
		if (Program.engine.frameCount == lastPositionUpdatingFrameNumber){
			return actualPixelsPosition;
		}
		else {
			try {
				Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
				actualPixelsPosition.x = pos.x;
				actualPixelsPosition.y = pos.y;
				lastPositionUpdatingFrameNumber = Program.engine.frameCount;
				return actualPixelsPosition;
			}
			catch (Exception e){
				if (Program.debug) System.out.println("This game object has no body or the body was not builded yet; " + this + " Body: " + body);
				return null;
			}
		}
	}

	public PVector getPixelPositionOld() {
		try {
			Vec2 pos = PhysicGameWorld.getBodyPixelCoord(body);
			actualPixelsPosition.x = pos.x;
			actualPixelsPosition.y = pos.y;
			return actualPixelsPosition;
		}
		catch (Exception e){
			if (Program.debug) System.out.println("This game object has no body or the body was not builded yet; " + this + " Body: " + body);
			return null;
		}
	}

	public Vec2 getActualInPhysicWorldPosition() {
		float worldX = getPixelPosition().x;
		float worldY = getPixelPosition().y;
		actualInWorldPosition.x = PApplet.map(worldX, 0.0F, 1.0F, this.transX, this.transX + worldScaleFactor);
		actualInWorldPosition.y = PApplet.map(PApplet.map(worldY, 0.0F, 1.0F, transY, transY + worldScaleFactor), 0.0F, (float) Program.engine.height, (float) Program.engine.height, 0.0F);
		return actualInWorldPosition;
	}

	public PVector getOnScreenPosition(GameCamera gameCamera) {
		try {
			Vec2 actualPosition = PhysicGameWorld.getBodyPixelCoord(body);
			onScreenPosition.x = ((actualPosition.x-gameCamera.getActualPosition().x)*gameCamera.getScale()+(PhysicGameWorld.SCREEN_CENTER_X));
			onScreenPosition.y = ((actualPosition.y-gameCamera.getActualPosition().y)*gameCamera.getScale()+(PhysicGameWorld.SCREEN_CENTER_Y));

			/*
			Vec2 pos = gameCamera.getOnScreenPosition();
			onScreenPosition.x = pos.x;
			onScreenPosition.y = pos.y;*/
			/*
			onScreenPosition.x = gameCamera.getObjectInGameWorldPosition(actualPosition).x;
			onScreenPosition.y = gameCamera.getObjectInGameWorldPosition(actualPosition).y;
*/
			//gameCamera.getOnScreenPosition()

			//onScreenPosition.y = ((actualPosition.y-gameCamera.getActualPosition().y)*gameCamera.getScale()+(Program.objectsFrame.height/2f));
			/*
			onScreenPosition.x = ((actualPosition.x-gameCamera.getActualPosition().x)*gameCamera.getScale()+(Program.engine.width/2));
			onScreenPosition.y = ((actualPosition.y-gameCamera.getActualPosition().y)*gameCamera.getScale()+(Program.engine.height/2));
			 */


			return onScreenPosition;
		}
		catch (Exception e){
			if (Program.debug) System.out.println("This game object has no body or the body was not builded yet");
			PVector nullVector = new PVector(0f,0f);
			return nullVector;
		}
	}

	private void updateMutablePosition(){

	}







	public PVector getWorldPosition() {
		try {
			Vec2 pos = PhysicGameWorld.controller.getBodyPixelCoord(body);
			actualPixelsPosition.x = pos.x;
			actualPixelsPosition.y = pos.y;
			return actualPixelsPosition;
		}
		catch (Exception e){
			if (Program.debug) System.out.println("This game object has no body or the body was not builded yet; " + this + " Body: " + body);
			return null;
		}



	}

/*
	float worldX = PApplet.map(pixelX, this.transX, this.transX + this.scaleFactor, 0.0F, 1.0F);
	float worldY = pixelY;
        if (this.yFlip == -1.0F) {
		worldY = PApplet.map(pixelY, (float)this.parent.height, 0.0F, 0.0F, (float)this.parent.height);
	}

	worldY = PApplet.map(worldY, this.transY, this.transY + this.scaleFactor, 0.0F, 1.0F);
        return new Vec2(worldX, worldY);

*/
	

	
	public void transferTo(PVector newAbsolutePlace) {		
		body.setTransform(PhysicGameWorld.controller.coordPixelsToWorld(newAbsolutePlace), body.getAngle());
	}
	
	public boolean isImmortal() {
		if (life == IMMORTALY_LIFE) return true;
		else return false;
	}
	
	public void attacked(Bullet bullet) {
		if (!bullet.getOwner().equals(this)) {
			attacked(Weapon.getNormalAttackPower(bullet.fromWeapon));
		}
		else {
			if (Program.debug) System.out.println("This bullet is fall in the owner");
		}

	}
	
	public void attacked(int damageValue) {
		if (damageValue > 0) {
			if (life != IMMORTALY_LIFE) {
				life -= damageValue;
			}
			isAttacked = true;
			if (life <= 0 && !dead) {
				life = 0;
				kill();
			}
		}
	}

	public boolean willBeKilledByAttack(int damageValue) {
		if (life!=IMMORTALY_LIFE) {
			if (life-damageValue > 0) return false;
			else return true;
		}
		else return false;
	}
	
	public void notMoreAttacked() {
		isAttacked = false;
	}
	
	public void kill() {
		dead = true;
		body.setGravityScale((float)(body.getGravityScale()*0.8));
		body.getFixtureList().setDensity(body.getFixtureList().getDensity()/2);
		body.resetMassData();
		if (body.isFixedRotation()) body.setFixedRotation(false);
		if (uniqueId < (-1) || uniqueId > 1){
			if (gameRound != null){
				gameRound.addObjectIdToBeNotMoreUploaded(this);
			}
		}
		if (Program.debug) System.out.println("Object " + this.getClass() +  " is dead") ;
	}
	
	public void killBody() {
		try {
			if (body.isFixedRotation()) body.setFixedRotation(false);
			body.setActive(false);
			System.out.println("This body was set unactive and delete from the world");
			//PhysicGameWorld.clearContactsWithBody(body);
			PhysicGameWorld.controller.destroyBody(body);	
		}
		catch (Exception e) {
			System.out.println("This body was deleted yet " + e);
		}
	}
	
	public boolean isAlive() {	//Was error
		if (dead) return false;
		else return true;
	}
	
	public int getLife() {
		return life;
	}
	
	public boolean isDead() {
		return dead;		
	}
	
	public int getMaxLife() {
		return maxLife;
	}
	
	public boolean isSleeped() {
		/*
		if (body!= null) {
			if (body.isActive()) return false;
			else return true;
		}
		else return true;
		*/



		return sleeped;
	}

	/*
	SetAwake - Sets the b2Body to awaken from sleep or sets it to sleep.  If any other body comes into contact with this body it will awaken by itself.
	SetActive - Sets the b2Body to active or inactive.  If any other body comes into contact with this body it will NOT awake and not be simulated.
	*/

	public void setSleeped(boolean toSleep) {
		if (toSleep){
			sleeped = true;
			body.setActive(false);
			//System.out.println("This body was set sleeped and unactive 1");
		}
		else{
			sleeped = false;
			body.setActive(true);
		}
	}
	
	public void awake() {		// Not slerping only for returning from the shield
		if (sleeped) {
			sleeped = false;
			body.setActive(true);
		}
	}

	public boolean isAppearedInClearingZone(){
		return appearedInClearingZone;
	}

	public void setAppearedInClearingZone(){
		appearedInClearingZone = true;
	}
	
	public void setBlocked(boolean toBlock) {
		blocked = toBlock;
	}
	
	public boolean isBlocked() {
		return blocked;		
	}
	
	public boolean isTransferingThroughPortal() {
		return transferingThroughPortal;
	}
	
	public void setTransferingThroughPortal(boolean toTransfer) {
		if (transferingThroughPortal!= toTransfer) transferingThroughPortal = toTransfer;
	}


	public String getClassName(){
		System.out.println("This class name must be overridden");
		return CLASS_NAME;
	}

	public boolean isPartOfSomeJoint(){
		return false;
	}

	public ArrayList <Body> getJoindedBodies(){
		System.out.println("This object can no joined objects");
		return null;
	}

	public void setJoinedBodiesAsStatic(){
		System.out.println("There are no linked bodies");
	}


	public Vec2 getPositionInPrevFrame() {
		if (positionInPrevFrame != null)	return positionInPrevFrame;
		else return new Vec2(getPixelPosition().x, getPixelPosition().y);
	}

	protected boolean isVisibleOnScreen(GameCamera gameCamera, Vec2 graphicElementCenter){
		return isVisibleOnScreen(gameCamera, graphicElementCenter.x, graphicElementCenter.y);
	}

	/*
	protected boolean isVisibleOnScreen(GameCamera gameCamera, PVector graphicElementCenter){
		return isVisibleOnScreen(gameCamera, graphicElementCenter.x, graphicElementCenter.y);
	}*/

	protected boolean isVisibleOnScreen(GameCamera gameCamera, float x, float y) {
		boolean isVisible = GameMechanics.isIntersectionBetweenAllignedRects(gameCamera.getActualPosition(), x, y, gameCamera.getVisibleZoneWidth(), gameCamera.getVisibleZoneHeight(), boundingWidth, boundingHeight);

		//}
		return isVisible;
	}


	public Vec2 getPositionForReleasedObject() {
		return PhysicGameWorld.controller.getBodyPixelCoord(body);
	}

	public void addJumpAfterAttack(GameObject attackingObject){
		Vec2 vector = null;
		final int xCoef = 11;
		int yCoef = 18;
		float attackingPos = attackingObject.getPixelPosition().x;
		float actualPos = getPixelPosition().x;
		if (attackingPos < actualPos) vector = new Vec2(body.getMass()*xCoef, body.getMass()*yCoef);
		else vector = new Vec2(-body.getMass()*xCoef, body.getMass()*yCoef);
		body.applyLinearImpulse(vector, body.getPosition(), true);
	}

    public boolean canBeAttackedByKick() {
		return true;
    }
}
