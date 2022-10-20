package com.mgdsstudio.blueberet.mainpackage;

import com.mgdsstudio.blueberet.gamecontrollers.Rectangular;
import com.mgdsstudio.blueberet.gamelibraries.Coordinate;
import com.mgdsstudio.blueberet.gameprocess.OnScreenButton;
import org.jbox2d.common.Vec2;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public interface IEngine {

    Object getMainActivity();

    void stopMusicForAdd();

    void exitWithoutActivitySaving();

    void initFullScreenAdd();

    void initRewardedAdd();

    boolean isRewardedAddStartedToUpload();

    boolean isRewardedAppFailedToUpload();

    boolean isPlayerMustBeRewarded();

    boolean isRewardedAppIsDismissed();

    void resetFullScreenAdd();

    boolean isFullScreenAdStartedToUpload();

    void resetRewardAdd();

    boolean isFullScreenAddCompleted();

    void addToastMessage(String text);

    boolean isSdkMoreOrSameAsNeeded();


    int getRewardValue();

    String getPathInCache(String additionalPath);

    String [] getAssets(String path);

    File getExternalFilesDir(String childDirName);

    InputStream createInputStreamInAssetsForFile(String file);

    void copyDataToClipboard(String MAIL, String path);

    void openKeyboard(boolean flag);

    void copy(File source, File output);

    void addStringToClippboard(String dataString);

    ArrayList<String> getFilesListInAssets();

    String getSketchPath();

    void copyUserLevelsToCache();

    Vec2 getCenterTouchPosition(Vec2 basicPosition, int maxZoneRadius);

    void fillOnStickTouchesArray(ArrayList<Coordinate> onStickTouchesArray, ArrayList<Coordinate> mutStickTouchesPool, Rectangular allStickHoleZone);

    boolean isButtonTouched(OnScreenButton onScreenButton);

    ArrayList<String> getFilesListInCache();

    ArrayList<String> getFilesListInAssetsFolder();


}
