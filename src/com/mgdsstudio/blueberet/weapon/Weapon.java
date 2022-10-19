package com.mgdsstudio.blueberet.weapon;

import com.mgdsstudio.blueberet.gameobjects.persons.Person;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.mainpackage.GameCamera;

import processing.core.PVector;

public abstract class Weapon {
	public final static int NORMAL_CARBINE_SHOT_POWER = 70;	//90
	public final static int NORMAL_SHOTGUN_SHOT_POWER = 45;	//55
	public final static int NORMAL_ASSAULT_RIFFLE_SHOT_POWER = 70;	//90
	public final static int USUAL_ATTACK_POWER = 55;
	
	// Weapon types
	public final static int HANDGUN = 101;
	public final static int SHOTGUN = 102;
	public final static int SMG = 103;
	public final static int GREENADE_LAUNCHER = 104;
	public final static int REVOLVER = 105;
	public final static int SO_SHOTGUN = 106;
	public final static int HAND_GREENADE = 107;


	public int type;




    public void attack(GameRound gameRound) {
		// TODO Auto-generated method stub
		
	}


	public boolean canAttack() {
		// TODO Auto-generated method stub
		return false;
	}

	public void drawInHands(GameCamera gameCamera, Person person) {
		// TODO Auto-generated method stub
		
	}
	
	public int getWeaponLength(float angle) {
		System.out.println("The weapon length is undefined");
		return 100;
	}

	public int getWeaponLength(Person person) {
		System.out.println("The weapon length is undefined");
		return 100;
	}

	public static byte getNormalAttackPower(int weaponType) {
		if (weaponType == HANDGUN) return NORMAL_CARBINE_SHOT_POWER;
		else if (weaponType == SHOTGUN) return NORMAL_SHOTGUN_SHOT_POWER;
		else if (weaponType == SMG) return NORMAL_ASSAULT_RIFFLE_SHOT_POWER;
		else return USUAL_ATTACK_POWER;
	}
	
	
	 public PVector getBulletEndPlace(Weapon weapon, float angle) {
		 System.out.println("There are no data about weapon length");
		 return null;
		 /*
			final float weaponLength = FirearmsWeapon.nominalWeaponLength;
			PVector startPlace = new PVector(weaponLength*Game2D.engine.cos((float)(Math.toRadians(angle))), 
											 weaponLength*Game2D.engine.sin((float)(Math.toRadians(angle))));
			return startPlace;*/
		}


	public void reload() {
		System.out.println("This weapon has no reload ability");
	}


	public boolean canBeWeaponReload() {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean isReloadCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isReloadCompletedByTimer() {
		// TODO Auto-generated method stub
		return false;
	}


	public abstract WeaponType getWeaponType();
	
	public final static int getWeaponCodeForType(WeaponType weaponType){
		if (weaponType == WeaponType.HANDGUN) return HANDGUN;
		else if (weaponType == WeaponType.SHOTGUN) return SHOTGUN;
		else if (weaponType == WeaponType.SMG) return SMG;
		else if (weaponType == WeaponType.M79) return GREENADE_LAUNCHER;
		else if (weaponType == WeaponType.REVOLVER) return REVOLVER;
		else if (weaponType == WeaponType.GRENADE) return HAND_GREENADE;
		else if (weaponType == WeaponType.SAWED_OFF_SHOTGUN) return SO_SHOTGUN;
		else {
			System.out.println("This weapon is not known. Can not get code for type " + weaponType);
			return -1;
		}
	}

	public final static WeaponType getWeaponTypeForCode(int weaponType){
		if (weaponType == HANDGUN) return WeaponType.HANDGUN;
		else if (weaponType == SHOTGUN) return  WeaponType.SHOTGUN;
		else if (weaponType == SMG) return WeaponType.SMG;
		else if (weaponType == GREENADE_LAUNCHER) return WeaponType.M79;

		else if (weaponType == HAND_GREENADE) return WeaponType.GRENADE;
		else if (weaponType == SO_SHOTGUN) return WeaponType.SAWED_OFF_SHOTGUN;
		else if (weaponType == REVOLVER) return WeaponType.REVOLVER;
		else {
			System.out.println("This weapon with type " + weaponType +"  is not known");
			return null;
		}
	}

	public int getType() {
		return type;
	}
}
