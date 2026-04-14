package com.domain.system;

public class SystemController {

    public void bootSystem(){
        System.out.println("시스템 부팅....");
        System.out.println("시스템 부팅완료...");
    }

    public boolean exit(boolean running){
        System.out.println("시스템 종료...");
        running = false;
        return running;
    }
}
