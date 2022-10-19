package com.mgdsstudio.blueberet.mainpackage;

import java.io.File;
import java.io.InputStream;

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

    void OpenKeyboard(boolean flag);

    void copy(File source, File output);
}
