package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

public class SMS {

    private String text;
    private String endMessageText;
    private PortraitPicture abonent;
    private int timeToClose = 2000;

    public SMS(String text, String endMessageText, PortraitPicture abonent) {
        this.text = text;
        this.endMessageText = endMessageText;
        this.abonent = abonent;
    }

    public SMS(String text, String endMessageText, PortraitPicture abonent, int timeToClose) {
        this.text = text;
        this.endMessageText = endMessageText;
        this.abonent = abonent;
        if (timeToClose>0) this.timeToClose = timeToClose;
    }

    public boolean isMessageSelfClosable(){

        if (timeToClose<0) return false;
        else return true;
    }

    public String getText() {
        return text;
    }

    public String getEndMessageText() {
        return endMessageText;
    }

    public PortraitPicture getAbonent() {
        return abonent;
    }

    public void setAbonent(PortraitPicture abonent) {
        this.abonent = abonent;
    }

    public static PortraitPicture getPortraitPictureForDigit(int digitValue){
        if (digitValue == 0) return PortraitPicture.PlAYER_FACE;
        else if (digitValue == 1) return PortraitPicture.SMARTPHONE_WITH_UNREAD_MESSAGE;
        else if (digitValue == 2) return PortraitPicture.SMARTPHONE_WITH_READ_MESSAGE;
        else if (digitValue == 3) return PortraitPicture.SMARTPHONE_WITH_SUBSCRIBER;
        else if (digitValue == 4) return PortraitPicture.SMARTPHONE_WITH_BLOCKED_SCREEN;
        else if (digitValue == 5) return PortraitPicture.SMARTPHONE_WITH_BLACK_SCREEN;
        else if (digitValue == 6) return PortraitPicture.SMARTPHONE_WITH_INCOMMING_CALL;
        else if (digitValue == 7) return PortraitPicture.NO_SUBSCRIBER;
        else if (digitValue == 8) return PortraitPicture.PLAYER_FACE_WITH_HALF_LIFE;    //not in level data. Only by activating
        else if (digitValue == 9) return PortraitPicture.PLAYER_FACE_WITH_SMALL_LIFE;   //not in level data. Only by activating
        else if (digitValue == 10) return PortraitPicture.PLAYER_SPEAKING_FACE_WITH_SMALL_LIFE; //not in level data. Only by activating
        else if (digitValue == 11) return PortraitPicture.PLAYER_SPEAKING_FACE_WITH_HALF_LIFE;  //not in level data. Only by activating
        else  if (digitValue == 12) return PortraitPicture.PLAYER_SPEAKING_FACE;
        else  if (digitValue == 13) return PortraitPicture.BOSS_BOAR;
        else  if (digitValue == 14) return PortraitPicture.BOSS_BOAR_WITH_BLOOD;
        else {
            System.out.println("This value has not data about portrait");
            return PortraitPicture.NO_SUBSCRIBER;
        }
    }

    public int getTimeToClose() {
        return timeToClose;
    }
}
