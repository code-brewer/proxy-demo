package com.fangminx.chain;

/**
 * 责任链第一种方式，每个handler依赖另外一个handler，耦合度高
 */
public class Client {
    static class HandlerA extends Handler{
        @Override
        protected void handleProcess() {
            System.out.println("handle by a");
        }
    }
    static class HandlerB extends Handler{
        @Override
        protected void handleProcess() {
            System.out.println("handle by b");
        }
    }
    static class HandlerC extends Handler{
        @Override
        protected void handleProcess() {
            System.out.println("handle by c");
        }
    }

    public static void main(String[] args){
        Handler handlerA = new HandlerA();
        Handler handlerB = new HandlerB();
        Handler handlerC = new HandlerC();

        handlerA.setSucessor(handlerB);
        handlerB.setSucessor(handlerC);

        handlerA.execute();
    }
}
