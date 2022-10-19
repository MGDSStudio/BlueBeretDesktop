package com.mgdsstudio.blueberet.desktop;

public class ConsoleReader extends Thread{
    private boolean active;
    private final DesktopLauncher desktopLauncher;

    ConsoleReader(DesktopLauncher engine){
        this.desktopLauncher = engine;
    }

    ConsoleReader(boolean start, DesktopLauncher desktopLauncher){
        this.active = start;
        this.desktopLauncher = desktopLauncher;

        //if (active) run();
    }

    @Override
    public void run() {
        while (active){

            System.out.println("Updating");
            readConsole();
            try {
                sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readConsole(){
            try {
                int inChar = System.in.read();
                String data = "";
                while (System.in.available() > 0) {
                    data += (char) inChar;
                    inChar = System.in.read();
                }
                if (data != null && data.length() > 0) {
                    System.out.println("Got data: " + data);

                }
            } catch (Exception e) {
                //e.printStackTrace();
                //e.printStackTrace();
            } finally {

            }


    }
}
