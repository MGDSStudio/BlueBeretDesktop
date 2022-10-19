package com.mgdsstudio.blueberet.gamecontrollers;

public class PhysicContactsController {

    public PhysicContactsController(){}

    /*
    public void update(GameRound gameRound){
        //System.out.println("Updating. Size: " + PhysicGameWorld.getBeginContacts().size() + "; End: " + PhysicGameWorld.endContacts.size());
        //System.out.println("Size: " + PhysicGameWorld.beginContacts.size()) ;
        for (int i = (PhysicGameWorld.beginContacts.size()-1); i>=0; i--) {
            Fixture f1 = PhysicGameWorld.beginContacts.get(i).getFixtureA();
            Fixture f2 = PhysicGameWorld.beginContacts.get(i).getFixtureB();
            Body b1 = f1.getBody();
            Body b2 = f2.getBody();
            updateRotationStickWithRoundElements(gameRound, b1,b2);

        }
    }

    private void updateRotationStickWithRoundElements(GameRound gameRound, Body b1, Body b2){
        GameObject gameObject1 = PhysicGameWorld.getGameObjectByBody(gameRound, b1);
        if (gameObject1 != null) {
            GameObject gameObject2 = PhysicGameWorld.getGameObjectByBody(gameRound, b2);
            if (gameObject2 != null) {
                if (PhysicGameWorld.getGameObjectByBody(gameRound, b1).getClass() == RoundRotatingStick.class) {
                    //System.out.println("Stick 1");
                    if (PhysicGameWorld.getGameObjectByBody(gameRound, b2).getClass() == RoundBox.class) {
                        //System.out.println("Box 1");
                        //System.out.println("Object was released");
                        if (b2.getType() == BodyType.STATIC || b2.getType() == BodyType.KINEMATIC) {
                            RoundElement roundElement = (RoundElement) PhysicGameWorld.getGameObjectByBody(gameRound, b2);
                            roundElement.setFilterData(b2.getFixtureList().getShape(), (byte) -3);
                            //System.out.println("Object was released");
                        }
                    }
                } else if (PhysicGameWorld.getGameObjectByBody(gameRound, b2).getClass() == RoundRotatingStick.class) {
                    //System.out.println("Stick 2");
                    if (PhysicGameWorld.getGameObjectByBody(gameRound, b1).getClass() == RoundBox.class) {
                        //System.out.println("Box 2");
                        if (b1.getType() == BodyType.STATIC || b1.getType() == BodyType.KINEMATIC) {
                            //System.out.println("Object was released");
                            RoundElement roundElement = (RoundElement) PhysicGameWorld.getGameObjectByBody(gameRound, b1);
                            roundElement.setFilterData(b1.getFixtureList().getShape(), (byte) -3);
                            //System.out.println("Object was released");
                        }
                    }
                }
            }
        }

    }
*/
}
