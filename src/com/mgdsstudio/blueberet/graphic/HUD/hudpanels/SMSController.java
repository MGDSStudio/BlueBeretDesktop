package com.mgdsstudio.blueberet.graphic.HUD.hudpanels;

import com.mgdsstudio.blueberet.gamelibraries.Timer;
import com.mgdsstudio.blueberet.gameprocess.GameRound;
import com.mgdsstudio.blueberet.graphic.HUD.HeadsUpDisplay;
import com.mgdsstudio.blueberet.graphic.controllers.PhotoDrawingController;
import com.mgdsstudio.blueberet.mainpackage.Program;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.ArrayList;

public class SMSController {
    //private ArrayList <String> text;
    private PFont textFont;
    public static String FORWARD_TEXT = "Forward...";
    public static String MARK_AS_READ = "Mark as read ";
    public static String CLOSE = "Close ";
    private Timer messageEndTimer;
    //private Timer nextSymbolTimer
    public final static int TIME_TO_SHOW_SINGLE_SCREEN = 5000;
    public final static int TIME_TO_FILL_ONE_STRING = 400;
    private int stringsAlongY = 4;

    public final static int HIDDEN = 0;
    public final static int STRINGS_WRITING = 1;
    public final static int NEXT_CLICK_WAITING = 2;
    public final static int END_OR_MARK_CLICK_WAITING = 3;
    private int statement = HIDDEN;
    //private Timer timerToShowMessage;

    private EightPartsFrameImage messageFrame, portraitFrame;
    private ArrayList <SMS> messages;
    //private ArrayList <String> messageText;
    private TextInMessageFrameDrawingController textInMessageFrameDrawingController;
    private PhotoDrawingController photoDrawingController;
    public final static boolean CLOSED = false;
    public final static boolean SHOWN = true;
    private final UpperPanel upperPanel;
    private static boolean alreadyTranslated;
    private GameRound gameRound;


    public SMSController(UpperPanel upperPanel, EightPartsFrameImage messageFrame, EightPartsFrameImage portraitFrame, SMS message, PFont textFont){
        this.upperPanel = upperPanel;
        this.portraitFrame = portraitFrame;
        messages = new ArrayList<>();
        this.textFont = textFont;
        if (message != null) {
            messages.add(message);
            photoDrawingController = new PhotoDrawingController(portraitFrame, message.getAbonent());
            textInMessageFrameDrawingController = new TextInMessageFrameDrawingController(message, messageFrame, 4, (int) (messageFrame.getHeight()*0.1f), textFont, HeadsUpDisplay.graphic, photoDrawingController);

        }
        //messageText = new ArrayList<>(4);
        this.messageFrame = messageFrame;
        initLanguageSpecific();
    }

    public void setMessage(SMS message) {
        messages.add(message);
        statement = HIDDEN;
    }

    protected static void initLanguageSpecific() {
        if (!alreadyTranslated)
            if (Program.LANGUAGE == Program.RUSSIAN && !Program.USE_ALWAYS_ENGLISH_IN_MENU) {
                FORWARD_TEXT = "далее...";
                MARK_AS_READ = "пометить как прочтенное";
                CLOSE = "закрыть";
            } else {
            }

        alreadyTranslated = true;
    }

    private void update(){
        if (messages.size() > 0) {
            if (messages.get(0).isMessageSelfClosable()){
                if (textInMessageFrameDrawingController != null) {
                    textInMessageFrameDrawingController.update();
                    if (textInMessageFrameDrawingController.isTimeEnds()) {
                        messages.remove(0);
                        //if (gameRound != null && Background.withUnfocusing)
                       // System.out.println("Unfocused!");
                            gameRound.getBackgroundFocusMaster().setGraphicAsNormalInOneStep();
                        if (messages.size() > 0) {
                             photoDrawingController = new PhotoDrawingController(portraitFrame, messages.get(0).getAbonent());
                             textInMessageFrameDrawingController = new TextInMessageFrameDrawingController(messages.get(0), messageFrame, 4, (int) (messageFrame.getHeight() * 0.1f), textFont, HeadsUpDisplay.graphic, photoDrawingController);
                        }
                        else {
                            System.out.println("Message ends");
                            upperPanel.setRedrawUpperHudOnNextFrame(true);
                            photoDrawingController.setChangeabilityType(getPhotoDrawingController().SUBSCRIBER_STOPS_TO_SPEAK);
                        }
                    }
                    else {
                        //System.out.println("Time is not ends " + textInFrameDrawingController.getRestTime());
                    }
                    //System.out.println("Time is not ends " + textInFrameDrawingController.jumpToNextPortionOfStringsToShow());
                }
            }
        }


    }

    public void startToShowMessage(){
        if (statement == HIDDEN) statement = STRINGS_WRITING;
    }

    public void writeText(PGraphics graphics){
        update();
        if (messages.size() > 0) {
            if (textInMessageFrameDrawingController != null) {
                textInMessageFrameDrawingController.draw(graphics);
                if (textInMessageFrameDrawingController.isEnded() || textInMessageFrameDrawingController.graduallyTextAppearingController.isFullAppeared()){
                    photoDrawingController.setChangeabilityType(getPhotoDrawingController().SUBSCRIBER_STOPS_TO_SPEAK);
                }
            }
            else System.out.println("Controller is null");
            if (photoDrawingController != null) drawFoto(graphics);
            else System.out.println("Photo controller is null");
        }
    }

    private void drawFoto(PGraphics graphics) {
        photoDrawingController.draw(graphics);
    }

    public static String getEndMessageStringForDigit(int digitValueInRoundFile){
        initLanguageSpecific();
        if (digitValueInRoundFile == 0) return FORWARD_TEXT;
        else if (digitValueInRoundFile == 1) return MARK_AS_READ;
        else if (digitValueInRoundFile == 2) return CLOSE;
        else{
            System.out.println("This value has not data about end level string;");
            return "";
        }
    }

    public void addMessage(SMS message, GameRound gameRound) {
        if (messages.size()==0){
            messages.add(message);
            photoDrawingController = new PhotoDrawingController(portraitFrame, messages.get(0).getAbonent());
            textInMessageFrameDrawingController = new TextInMessageFrameDrawingController(message, messageFrame, 4, (int) (messageFrame.getHeight()*0.1f), textFont, HeadsUpDisplay.graphic, photoDrawingController);
            this.gameRound = gameRound;
         }
        else{
            messages.add(message);
        }

    }

    public PhotoDrawingController getPhotoDrawingController() {
        return photoDrawingController;
    }

    public boolean areThereMessages() {
        if (messages.size()>0) return true;
        else return false;
    }

    public boolean areThereShownMessages() {
        if (textInMessageFrameDrawingController == null) return false;
        else {
            if (messages.size()>0) return true;
            return false;
        }
    }

    public boolean isMessageFrameClicked() {
        if (messageFrame.isClicked(Program.engine.mouseX, Program.engine.mouseY)){
            return true;
        }
        else return false;
    }

    public boolean closeMessageFrameIfPossible() {
        boolean ended = SHOWN;
        textInMessageFrameDrawingController.frameWasClicked();

        if (messages.size() > 0){
            if (textInMessageFrameDrawingController.isEnded()){
                ended = true;
            }
            //ended = textInFrameDrawingController.jumpToNextPortionOfStringsToShow();
        }
        if (messages.size() == 0) return true;
        else return ended;

    }
}
